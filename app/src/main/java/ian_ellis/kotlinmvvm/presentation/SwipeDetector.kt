package ian_ellis.kotlinmvvm.presentation

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View


open class SwipeDetector : View.OnTouchListener {
  private val gestureDetector: GestureDetector

  constructor(context: Context) {
    gestureDetector = GestureDetector(context, GestureListener())
  }

  override fun onTouch(view: View?, event: MotionEvent?): Boolean {
    return gestureDetector.onTouchEvent(event);
  }

  public open fun onSwipeRight(){}

  public open fun onSwipeLeft(){}

  public open fun onClick(){}

  private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {


    private val SWIPE_DISTANCE_THRESHOLD: Int = 100
    private val SWIPE_VELOCITY_THRESHOLD: Int = 100


    override public fun onDown(e:MotionEvent): Boolean {
      return true;
    }

    override public fun onSingleTapUp(e: MotionEvent?): Boolean {
      onClick()
      return true
    }

    override public fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
      val distanceX = e2.x - e1.x;
      val distanceY = e2.y - e1.y;
      if (Math.abs(distanceX) > Math.abs(distanceY) && Math.abs(distanceX) > SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
        if (distanceX > 0)
          onSwipeRight();
        else
          onSwipeLeft();
        return true;
      }
      return false;
    }
  }
}