package ian_ellis.kotlinmvvm.presentation

import android.app.Activity
import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import ian_ellis.kotlinmvvm.utils.RxBinderUtil
import ian_ellis.kotlinmvvm.R
import ian_ellis.kotlinmvvm.domain.ToDosViewModel
import ian_ellis.kotlinmvvm.presentation.adapters.ToDosAdapter
import org.jetbrains.anko.onClick
import org.jetbrains.anko.startActivity

public class MainActivity : Activity() {

  protected val binder = RxBinderUtil(this)

  protected lateinit var recycler: RecyclerView
  protected lateinit var nameText: EditText
  protected lateinit var createBtn: FloatingActionButton
  protected lateinit var collapsingToolbar: CollapsingToolbarLayout
  protected lateinit var toolbar: Toolbar

  protected lateinit var adapter: ToDosAdapter
  protected lateinit var viewModel: ToDosViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_to_dos)
    adapter = ToDosAdapter(this)
    viewModel = ToDosViewModel(this)
    initView()

    adapter.deletedHandler = {
      viewModel.remove(it)
    }

    adapter.doneHandler = {
      viewModel.done(it, !it.done)
    }

    adapter.editHandler = {
      startActivity<EditActivity>(EditActivity.ID to it.id)
    }
  }

  override fun onResume() {
    super.onResume()
    viewModel.go();
    binder.bindProperty(viewModel.getToDos(), {
      adapter.update(it.items);
    })
  }

  override fun onPause() {
    super.onPause()
    binder.clear()
    viewModel.stop()
  }

  protected fun initView() {
    //get reference to views
    recycler = findViewById(R.id.recycler_to_dos) as RecyclerView
    nameText = findViewById(R.id.edt_name) as EditText
    createBtn = findViewById(R.id.fab_add) as FloatingActionButton
    toolbar = findViewById(R.id.toolbar) as Toolbar
    collapsingToolbar = findViewById(R.id.collapsing_toolbar) as CollapsingToolbarLayout
    //set up toolbar
    toolbar.title = resources.getString(R.string.to_dos_title)
    collapsingToolbar.setTitle(resources.getString(R.string.to_dos_title))
    //set up recycler
    recycler.adapter = adapter
    recycler.layoutManager = LinearLayoutManager(this)
    recycler.addItemDecoration(SimpleRecyclerDivider(this))
    //listen for change in name / create text

    nameText.addTextChangedListener(object : TextWatcher {
      override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

      override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

      override fun afterTextChanged(p0: Editable?) {
        //after each change, check if blank and enable  disable
        createBtn.isEnabled = (nameText.text.toString() != "")
      }

    })
    //enable / disable the text
    createBtn.isEnabled = (nameText.text.toString() != "")
    //add click listener
    createBtn.onClick {
      viewModel.add(nameText.text.toString())
      nameText.setText("")
    }
  }

}
