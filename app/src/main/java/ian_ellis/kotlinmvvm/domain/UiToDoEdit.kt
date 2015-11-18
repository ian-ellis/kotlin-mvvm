package ian_ellis.kotlinmvvm.domain

import android.databinding.ObservableField


data class UiToDoEdit(val id:Long, val nameStr: String, val descriptionStr:String){
  public val name = ObservableField<String>(nameStr)
  public val description = ObservableField<String>(descriptionStr)
}