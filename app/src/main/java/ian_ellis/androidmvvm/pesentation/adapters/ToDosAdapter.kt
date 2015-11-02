package ian_ellis.androidmvvm.pesentation.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.graphics.Paint
import ian_ellis.androidmvvm.R
import ian_ellis.androidmvvm.domain.UiToDoListItem
import ian_ellis.androidmvvm.pesentation.SwipeDetector
import org.jetbrains.anko.layoutInflater
import org.jetbrains.anko.onClick
import java.util.ArrayList

/**
 * Created by ianellis on 24/07/15.
 */
public class ToDosAdapter(val context: Context) : RecyclerView.Adapter<ToDosAdapter.ViewHolder>() {

  private val data: ArrayList<UiToDoListItem> = ArrayList()

  public var deletedHandler: ((UiToDoListItem) -> Unit)? = null
  public var doneHandler: ((UiToDoListItem) -> Unit)? = null

  public fun update(items: List<UiToDoListItem>) {
    if (data.size == 0) {
      newDataSet(items)

    } else {

      val changed: ArrayList<Int> = arrayListOf()
      val added: ArrayList<Int> = arrayListOf()
      val deleted: ArrayList<Int> = arrayListOf()

      //check if any are any deleted
      val itemsById = items.groupBy { it.id }
      val dataById = data.groupBy { it.id }

      data.forEachIndexed { i, it ->
        if (!itemsById.contains(it.id)) {
          deleted.add(i)
        }
      }

      items.forEachIndexed { i, it ->
        val oldItem: List<UiToDoListItem>? = dataById[it.id]
        if (oldItem != null && oldItem.size > 0) {
          if (oldItem[0].done != it.done || oldItem[0].name != it.name) {
            changed.add(i)
          }
        } else {
          added.add(i)
        }
      }

      if (changed.size == 0 && added.size == 0 && deleted.size == 0) {
        newDataSet(items)
      } else {
        data.clear()
        data.addAll(items)
        changed.forEach {
          notifyItemChanged(it)
        }

        deleted.forEach {
          notifyItemRemoved(it)
        }

        added.forEach {
          notifyItemInserted(it)
        }
      }
    }
  }

  protected fun newDataSet(items: List<UiToDoListItem>) {
    data.clear()
    data.addAll(items)
    notifyDataSetChanged()
  }

  override fun getItemCount(): Int {
    return data.size
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.title.text = data[position].name
    if (data[position].done) {
//      holder.done.text = context.getText(R.string.to_do_item_btn_undo)
      holder.title.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
    } else {
//      holder.done.text = context.getText(R.string.to_do_item_btn_done)
      holder.title.paintFlags = Paint.LINEAR_TEXT_FLAG
    }
    holder.item = data[position]
  }

  override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? {
    return ViewHolder(context.layoutInflater.inflate(R.layout.view_to_do_item, parent, false))
  }

  inner class ViewHolder : RecyclerView.ViewHolder {
    val title: TextView
//    val done: Button
//    val delete: Button
    lateinit var item: UiToDoListItem

    constructor(view: View) : super(view) {
      title = view.findViewById(R.id.text_to_do_item_name) as TextView
      view.setOnTouchListener(object: SwipeDetector(context) {
        override fun onSwipeRight() {
          if(item.done){
            doneHandler?.invoke(item)
          }
        }

        override fun onSwipeLeft() {
          if(item.done){
            deletedHandler?.invoke(item)
          }else{
            doneHandler?.invoke(item)
          }
        }
      })
    }
  }

}
