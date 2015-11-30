package ian_ellis.kotlinmvvm.presentation;

import android.databinding.BindingAdapter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class KotlinBindings {

  @BindingAdapter("kbind:onTextChanged")
  public static void bindEditText(EditText editText, TextWatcher listener) {
    editText.addTextChangedListener(listener);
  }

  @BindingAdapter({"kbind:onClick"})
  public static void onClick(View v,View.OnClickListener listener){
    v.setOnClickListener(listener);
  }

  @BindingAdapter("kbind:editText")
  public static void bindEditText(EditText editText, String text) {
    String currentText = editText.getText().toString();
    if(text != null && (currentText.equals("") ||  !currentText.equals(text))) {
      editText.setText(text, TextView.BufferType.EDITABLE);
      editText.setSelection(text.length());
    }
  }
}
