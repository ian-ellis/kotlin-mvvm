package ian_ellis.kotlinmvvm.presentation

import android.app.Activity
import android.databinding.DataBindingUtil
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
import ian_ellis.kotlinmvvm.databinding.ActivityToDosBinding
import ian_ellis.kotlinmvvm.domain.ToDosViewModel
import ian_ellis.kotlinmvvm.presentation.handlers.CreateHandler
import ian_ellis.kotlinmvvm.presentation.adapters.ToDosAdapter
import org.jetbrains.anko.onClick
import org.jetbrains.anko.startActivity

public class MainActivity : Activity() {

  protected val binder = RxBinderUtil(this)

  protected lateinit var recycler: RecyclerView
  protected lateinit var nameText: EditText
  protected lateinit var createBtn: FloatingActionButton
  protected lateinit var adapter: ToDosAdapter
  protected lateinit var viewModel: ToDosViewModel

  protected lateinit var binding:ActivityToDosBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, R.layout.activity_to_dos)
    adapter = ToDosAdapter(this)
    viewModel = ToDosViewModel(this)
    binding.handler = CreateHandler(viewModel)
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
      binding.data = it
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
    //set up recycler
    recycler.adapter = adapter
    recycler.layoutManager = LinearLayoutManager(this)
    recycler.addItemDecoration(SimpleRecyclerDivider(this))
  }

}
