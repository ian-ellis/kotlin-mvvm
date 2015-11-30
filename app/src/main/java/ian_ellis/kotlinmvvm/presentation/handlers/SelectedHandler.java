package ian_ellis.kotlinmvvm.presentation.handlers;

import android.databinding.BindingAdapter;
import android.support.annotation.NonNull;
import android.view.View;

public class SelectedHandler<T> {

  @BindingAdapter({"app:selected"})
  public static void onClick(View v,View.OnClickListener listener){
    v.setOnClickListener(listener);
  }

  private T item;
  private SelectedListener selectedListener;
  private SelectedListener actionListener;

  public SelectedHandler(T item, SelectedListener<T> selectedListener, SelectedListener<T> actionListener){
    this.item = item;
    this.selectedListener = selectedListener;
    this.actionListener = actionListener;
  }

  public void selected(View v){
    selectedListener.selected(item, v);
  }

  public void action(View v){
    actionListener.selected(item, v);
  }

  public interface SelectedListener<T> {
    void selected(@NonNull T item, @NonNull View v);
  }


}
