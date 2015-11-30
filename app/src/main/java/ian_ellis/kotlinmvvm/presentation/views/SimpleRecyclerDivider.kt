package ian_ellis.kotlinmvvm.presentation

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import ian_ellis.kotlinmvvm.R
import org.jetbrains.anko.childrenSequence
import kotlin.properties.Delegates


public class SimpleRecyclerDivider(val context:Context) : RecyclerView.ItemDecoration() {

  private var divider: Drawable = context.resources.getDrawable(R.drawable.recycler_divider);
  private var oneDp:Int = dpsToPx(1F).toInt()

  override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
    var left = parent.paddingLeft;
    var right = parent.width - parent.paddingRight;
    //loop through each visible child in the recycler
    parent.childrenSequence().forEach { child ->
      val params = child.layoutParams as RecyclerView.LayoutParams
      //set the dividers alpha to match the recycler child
      divider.alpha = (255 * child.alpha).toInt()
      //get the top and bottom of the divider
      var top = (child.bottom + params.bottomMargin - oneDp + child.translationY).toInt();
      var bottom = top + divider.intrinsicHeight;
      //draw the recycler onto the canvas
      divider.setBounds(left, top, right, bottom);
      divider.draw(c);
    }
  }
  protected fun dpsToPx(dp: Float): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics)
  }
}
