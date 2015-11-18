package ian_ellis.kotlinmvvm.presentation

import android.databinding.BaseObservable
import android.databinding.BindingAdapter
import android.databinding.BindingConversion
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import ian_ellis.kotlinmvvm.R
import java.util.*


class BindableString : BaseObservable(){

  companion object{


  }
//
//  @BindingConversion
//  public fun convertBindableToString(bindableString: BindableString):String {
//    return bindableString.get();
//  }
//
//
//  @BindingAdapter("app:binding")
//  fun bindEditText(view: EditText, bindableString: BindableString) {
//
//    if (view.getTag(R.id.binded) == null) {
//
//      view.setTag(R.id.binded, true);
//
//      view.addTextChangedListener(object: TextWatcher {
//        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//          bindableString.set(s.toString());
//        }
//
//        override fun afterTextChanged(s: Editable) {}
//
//        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//      })
//
//    }
//    val newValue: String = bindableString.get();
//    if (!view.text.toString().equals(newValue)) {
//      view.setText(newValue);
//    }
//  }

  private var value:String? = null;

  public fun get():String {
    return value ?: ""
  }

  public fun set(value:String) {
    if (!Objects.equals(this.value, value)) {
      this.value = value;
      notifyChange();
    }
  }

  public fun isEmpty():Boolean {
    return value == null || value!!.isEmpty();
  }
}