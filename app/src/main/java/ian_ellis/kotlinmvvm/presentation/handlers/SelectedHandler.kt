package ian_ellis.kotlinmvvm.presentation.handlers

import android.content.DialogInterface
import android.databinding.BindingAdapter
import android.view.View
import ian_ellis.kotlinmvvm.domain.UiToDoListItem


//class SelectedHandler (val item:UiToDoListItem,val editHandler: ((UiToDoListItem) -> Unit)?){
class SelectedHandler{
  companion object{

    @BindingAdapter(value="app:selected")
    @JvmStatic
    public fun onClick(v:View,listener:View.OnClickListener){
      v.setOnClickListener(listener)
    }
  }

  public fun selected(v:View){
    val tmp = ""
//    editHandler?.invoke(item)
  }
}