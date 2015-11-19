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
  private SelectedListener listener;

  public SelectedHandler(T item, SelectedListener<T> listener){
    this.item = item;
    this.listener = listener;
  }

  public void selected(View v){
    listener.selected(item);
  }

  public interface SelectedListener<T> {
    void selected(@NonNull T item);
  }
}
