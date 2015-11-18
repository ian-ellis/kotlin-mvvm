package ian_ellis.kotlinmvvm.presentation

import android.support.v7.util.SortedList
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.util.SortedListAdapterCallback
import ian_ellis.kotlinmvvm.domain.UiToDoListItem


class ToDoSortedList : SortedList<UiToDoListItem> {

  constructor(adapter: RecyclerView.Adapter<*>):super(
    UiToDoListItem::class.java, object : SortedListAdapterCallback<UiToDoListItem>(adapter) {

    override fun areContentsTheSame(oldItem: UiToDoListItem, newItem: UiToDoListItem): Boolean {
      return oldItem.name.equals(newItem.name) && oldItem.done == newItem.done
    }

    override fun areItemsTheSame(item1: UiToDoListItem, item2: UiToDoListItem): Boolean {
      return item1.id.equals(item2.id)
    }

    override fun compare(o1: UiToDoListItem, o2: UiToDoListItem): Int {
      return o1.id.compareTo(o2.id)
    }
  })

  public fun set(items: List<UiToDoListItem>){
    //get removed items
    val current = (0..size()-1).map {get(it)}
    val removed:MutableList<Int> = arrayListOf()
    current.forEachIndexed {i,trip->
      if(items.indexOf(trip) == -1){
        removed.add(i)
      }
    }
    beginBatchedUpdates()
    removed.forEachIndexed { i, index ->
      removeItemAt(index-i)
    }
    addAll(items)
    endBatchedUpdates()
  }
}