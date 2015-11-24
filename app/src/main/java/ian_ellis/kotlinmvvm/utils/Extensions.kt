package ian_ellis.kotlinmvvm.utils

import android.content.Context
import android.graphics.PorterDuff
import android.widget.PopupMenu
import org.jetbrains.anko.itemsSequence
import java.lang.reflect.Field

fun PopupMenu.forceShowIcons(){
  val fields = javaClass.declaredFields;
  for(field: Field in fields){
    if ("mPopup".equals(field.name)) {
      field.isAccessible = true;
      val menuPopupHelper = field.get(this);
      val classPopupHelper = Class.forName(menuPopupHelper.javaClass.name);
      val setForceIcons = classPopupHelper.getMethod("setForceShowIcon", kotlin.Boolean::class.java);
      setForceIcons.invoke(menuPopupHelper, true);
      break;
    }
  }
}

fun PopupMenu.tintIcons(context: Context, colorRes:Int) {
  this.menu.itemsSequence().forEach {item ->
    item.icon?.let{ icon->
      icon.setColorFilter(context.resources.getColor(colorRes),PorterDuff.Mode .SRC_IN)
    }
  }
}




