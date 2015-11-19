package ian_ellis.kotlinmvvm.presentation

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.EditText
import android.widget.TextView
import com.jakewharton.rxbinding.widget.RxTextView
import ian_ellis.kotlinmvvm.R
import ian_ellis.kotlinmvvm.databinding.ActivityEditToDoBinding
import ian_ellis.kotlinmvvm.domain.EditViewModel
import ian_ellis.kotlinmvvm.presentation.handlers.EditHandler
import ian_ellis.kotlinmvvm.utils.RxBinderUtil
import rx.Observable
import rx.Subscription


class EditActivity : AppCompatActivity() {

  companion object {
    @JvmStatic public val ID: String = "id"
  }

  protected val binder = RxBinderUtil(this)
  protected val viewModel = EditViewModel(this)
  protected var id: Long = 0


//  protected lateinit var toolbar: Toolbar
//  protected lateinit var nameText: EditText
//  protected lateinit var descriptionText: EditText
//  protected lateinit var updateObservable: Observable<Pair<String,String>>
  protected lateinit var binding: ActivityEditToDoBinding

  protected var updateSubscription: Subscription? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)


    binding = DataBindingUtil.setContentView(this,R.layout.activity_edit_to_do)
    binding.handler = EditHandler()

    id = intent.extras?.getLong(ID) ?: throw Error("No Id Passed")

    val toolbar = findViewById(R.id.toolbar) as Toolbar
//    nameText = findViewById(R.id.edt_to_do_name) as EditText
//    descriptionText = findViewById(R.id.edt_to_do_description) as EditText
    setSupportActionBar(toolbar)

    supportActionBar.setDisplayHomeAsUpEnabled(true)
    toolbar.setNavigationOnClickListener { finish() }

//    updateObservable = Observable.combineLatest(
//      RxTextView.textChanges(nameText),
//      RxTextView.textChanges(descriptionText),{name,description ->
//        Pair(name.toString(),description.toString())
//      }
//    )

  }

  override fun onResume() {
    super.onResume()
    viewModel.go(id)
    binder.bindProperty(viewModel.getToDo(), {toDo->
      binding.toDo = toDo

//      updateSubscription?.unsubscribe()

//      toolbar.title = "Edit: ${toDo.name}"
//
//      updateText(nameText,toDo.name)
//      updateText(descriptionText,toDo.description)

//      updateSubscription = updateObservable.debounce(300, TimeUnit.MILLISECONDS).subscribe({
//        if(it.first != toDo.name || it.second != toDo.description) {
//          viewModel.update(toDo.id, it.first, it.second)
//        }
//      })
    })
  }

  protected fun updateText(textField:EditText,text:String){
    if(textField.text.toString() != text){
      val selection =
      if(textField.text.toString() == ""){
        text.length
      }else{
        textField.selectionStart
      }
      textField.setText(text, TextView.BufferType.EDITABLE)
      textField.setSelection(selection)

    }
  }

  override fun onPause() {
    super.onPause()
    binder.clear()
    updateSubscription?.unsubscribe()
  }


}

