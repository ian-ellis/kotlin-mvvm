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



}
