package ian_ellis.androidmvvm.pesentation.fragments

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.app.Fragment
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import ian_ellis.androidmvvm.R
import ian_ellis.androidmvvm.domain.ToDosViewModel
import ian_ellis.androidmvvm.data.vo.ToDo
import ian_ellis.androidmvvm.pesentation.adapters.ToDosAdapter
import ian_ellis.androidmvvm.utils.RxBinderUtil
import ian_ellis.presentation.SimpleRecyclerDivider
import org.jetbrains.anko.onClick

public class ToDosFragment : Fragment() {

  object factory {
    public fun create(): ToDosFragment {
      return ToDosFragment();
    }
  }

  protected val binder = RxBinderUtil(this)

  protected lateinit var recycler: RecyclerView
  protected lateinit var nameText: EditText
  protected lateinit var createBtn: FloatingActionButton
  protected lateinit var collapsingToolbar:CollapsingToolbarLayout
  protected lateinit var toolbar: Toolbar

  protected lateinit var adapter: ToDosAdapter
  protected lateinit var viewModel: ToDosViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    adapter= ToDosAdapter(activity)
    viewModel = ToDosViewModel(activity)
    viewModel.go();
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_to_dos, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    recycler = view.findViewById(R.id.recycler_to_dos) as RecyclerView
    nameText = view.findViewById(R.id.edt_name) as EditText
    createBtn = view.findViewById(R.id.fab_add) as FloatingActionButton
    toolbar = view.findViewById(R.id.toolbar) as Toolbar
    collapsingToolbar = view.findViewById(R.id.collapsing_toolbar) as CollapsingToolbarLayout

    toolbar.title = activity.resources.getString(R.string.to_dos_title)
    collapsingToolbar.setTitle(activity.resources.getString(R.string.to_dos_title))

    recycler.adapter = adapter
    recycler.layoutManager = LinearLayoutManager(activity)
    recycler.addItemDecoration(SimpleRecyclerDivider(activity))

    nameText.addTextChangedListener(object : TextWatcher {
      override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

      override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

      override fun afterTextChanged(p0: Editable?) {
        createBtn.isEnabled = (nameText.text.toString() != "")
      }

    })

    createBtn.isEnabled = (nameText.text.toString() != "")
    createBtn.onClick {
      viewModel.add(nameText.text.toString())
      nameText.setText("")
    }

  }

  override fun onResume() {
    super.onResume()
    binder.bindProperty(viewModel.getToDos(), {
      adapter.update(it);
    })

    adapter.deletedHandler = {
      viewModel.remove(it)
    }

    adapter.doneHandler = {
      viewModel.done(it,!it.done)
    }
  }

  override fun onPause() {
    super.onPause()
    binder.clear()
  }

//  private fun editToDo(toDo: ToDo) {
//    Toast.makeText(getActivity(), "Edit To Do #{toDo.name}", Toast.LENGTH_LONG).show();
//  }

}
