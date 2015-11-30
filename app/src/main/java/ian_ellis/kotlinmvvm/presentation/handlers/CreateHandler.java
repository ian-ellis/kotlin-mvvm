package ian_ellis.kotlinmvvm.presentation.handlers;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import ian_ellis.kotlinmvvm.domain.ToDosViewModel;

public class CreateHandler {

  protected ToDosViewModel viewModel;
  public CreateHandler(ToDosViewModel viewModel){
   this.viewModel = viewModel;
  }

  public void create(View v){
    viewModel.addClicked();
  }

  public TextWatcher titleChanged = new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override
    public void afterTextChanged(Editable s) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
      viewModel.createTitleChanged(s.toString());
    }
  };

}
