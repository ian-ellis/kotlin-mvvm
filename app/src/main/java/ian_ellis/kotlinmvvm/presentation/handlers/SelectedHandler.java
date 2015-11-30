package ian_ellis.kotlinmvvm.presentation.handlers;

import android.support.annotation.NonNull;
import android.view.View;

public class SelectedHandler {
  
  private SelectedListener selectedListener;
  private SelectedListener actionListener;

  public SelectedHandler(SelectedListener selectedListener, SelectedListener actionListener){
    this.selectedListener = selectedListener;
    this.actionListener = actionListener;
  }

  public void selected(View v){
    selectedListener.selected(v);
  }

  public void action(View v){
    actionListener.selected(v);
  }

  public interface SelectedListener {
    void selected(@NonNull View v);
  }


}
