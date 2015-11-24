package ian_ellis.kotlinmvvm.presentation.handlers;

import android.databinding.BindingAdapter;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

public class EditHandler {
  public TextWatcher nameChanged;
  public TextWatcher descriptionChanged;

  public EditHandler(TextWatcher nameWatcher, TextWatcher descriptionWatcher) {
    nameChanged = nameWatcher;
    descriptionChanged = descriptionWatcher;
  }

  @BindingAdapter("app:onTextChanged")
  public static void bindEditText(EditText editText, TextWatcher listener) {
    editText.addTextChangedListener(listener);
  }

  @BindingAdapter("app:editText")
  public static void bindEditText(EditText editText, String text) {
    String currentText = editText.getText().toString();
    if((!editText.isFocused() || currentText.equals("")) && text != null && !currentText.equals(text)) {
      editText.setText(text, TextView.BufferType.EDITABLE);
      editText.setSelection(text.length());
    }


  }

}
