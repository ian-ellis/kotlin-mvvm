package ian_ellis.kotlinmvvm.presentation

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import ian_ellis.kotlinmvvm.R
import kotlin.properties.Delegates


public class SimpleRecyclerDivider : RecyclerView.ItemDecoration {

  private var divider: Drawable by Delegates.notNull();

  constructor(context: Context) {
    divider = context.resources.getDrawable(R.drawable.recycler_divider);
  }


  override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
    var left = parent.paddingLeft;
    var right = parent.width - parent.paddingRight;
    // if we are indented we will have sides on our items, we need to add 1 dp to account for this
    val oneDp = dpsToPx(1F, parent.context).toInt()

    val childCount = Math.min(parent.childCount, parent.adapter.itemCount)

    for (i in 0..childCount - 1) {
      val child = parent.getChildAt(i);

      val params = child.layoutParams as RecyclerView.LayoutParams

      divider.alpha = (255 * child.alpha).toInt()
      var top = (child.bottom + params.bottomMargin - oneDp + child.translationY).toInt();
      var bottom = top + divider.intrinsicHeight;

      divider.setBounds(left, top, right, bottom);
      divider.draw(c);



    }
  }
  protected fun dpsToPx(dp: Float, context: Context): Float {
    val r = context.resources
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.displayMetrics)
  }
}
