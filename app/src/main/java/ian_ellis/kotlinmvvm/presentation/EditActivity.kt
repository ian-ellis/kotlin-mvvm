package ian_ellis.kotlinmvvm.presentation

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
import rx.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit


class EditActivity : AppCompatActivity() {

  companion object {
    @JvmStatic public val ID: String = "id"
  }

  protected val binder = RxBinderUtil(this)
  protected lateinit var binding: ActivityEditToDoBinding
  protected val viewModel = EditViewModel(this)
  protected var id: Long = 0


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_to_do)
    binding.handler = EditHandler(nameChanged, descriptionChanged)

    id = intent.extras?.getLong(ID) ?: throw Error("No Id Passed")

    val toolbar = findViewById(R.id.toolbar) as Toolbar
    setSupportActionBar(toolbar)

    supportActionBar.setDisplayHomeAsUpEnabled(true)
    toolbar.setNavigationOnClickListener { finish() }

  }

  override fun onResume() {
    super.onResume()
    viewModel.go(id)
    binder.bindProperty(viewModel.getToDo(), { toDo ->
      binding.toDo = toDo
    })
  }

  override fun onPause() {
    super.onPause()
    binder.clear()
  }

  protected val nameChanged = object : TextWatcher {
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
      viewModel.nameChanged(s.toString())
    }
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun afterTextChanged(s: Editable?) {}
  }

  protected val descriptionChanged = object : TextWatcher {
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
      viewModel.descriptionChanged(s.toString())
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun afterTextChanged(s: Editable?) {}
  }

}

