package ian_ellis.kotlinmvvm.presentation.handlers;

import android.databinding.BindingAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class EditHandler {

//  @BindingAdapter("app:bindEditText")
//  public static void bindEditText(EditText editText, CharSequence value) {
//    if (!editText.getText().toString().equals(value.toString())) {
//      editText.setText(value);
//    }
//  }

  @BindingAdapter("app:bindEditText")
  public static void bindEditText(EditText editText, TextWatcher listener) {
    editText.addTextChangedListener(listener);
  }

  public TextWatcher nameChanged = new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      String tmp = "";
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
      String tmp = "";
    }

    @Override
    public void afterTextChanged(Editable s) {
      String tmp = "";
    }
  };
}
