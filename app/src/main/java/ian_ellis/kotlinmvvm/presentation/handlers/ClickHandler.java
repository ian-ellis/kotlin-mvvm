package ian_ellis.kotlinmvvm.presentation.handlers;

import android.databinding.BindingAdapter;
import android.view.View;

public class ClickHandler {

  @BindingAdapter(value="app:selected")
  public static void onClick(View v, View.OnClickListener listener){
    v.setOnClickListener(listener);
  }

  public void selected(View v){
    String tmp = "";
  }

}
