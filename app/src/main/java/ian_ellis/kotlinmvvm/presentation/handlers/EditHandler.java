package ian_ellis.kotlinmvvm.presentation.handlers;

import android.text.TextWatcher;

public class EditHandler {
  public TextWatcher nameChanged;
  public TextWatcher descriptionChanged;

  public EditHandler(TextWatcher nameWatcher, TextWatcher descriptionWatcher) {
    nameChanged = nameWatcher;
    descriptionChanged = descriptionWatcher;
  }
}
