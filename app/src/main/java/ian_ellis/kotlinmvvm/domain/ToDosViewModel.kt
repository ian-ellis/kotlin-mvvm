package ian_ellis.kotlinmvvm.domain

import android.content.Context
import ian_ellis.kotlinmvvm.data.store.ToDoSQLHelper
import ian_ellis.kotlinmvvm.data.vo.ToDo
import rx.Observable
import rx.Subscription
import rx.subjects.BehaviorSubject
import java.util.*

public class ToDosViewModel(val context: Context) {

  private val db: ToDoSQLHelper = ToDoSQLHelper(context)
  private val behavior: BehaviorSubject<List<ToDo>>  = BehaviorSubject.create()
  private var refreshSubscription: Subscription? = null

  public fun getToDos(): Observable<UiToDoListUpdate> {

    return behavior.scan(Pair<List<ToDo>,List<ToDo>>(listOf(),listOf()),
      {previous, next ->
        Pair(previous.second,next)
      }
    ).map{
      val previous = it.first
      val next = it.second

      val changed: ArrayList<Int> = arrayListOf()
      val added: ArrayList<Int> = arrayListOf()
      val deleted: ArrayList<Int> = arrayListOf()

      if(previous.size > 0) {
        val itemsById = next.groupBy { it.id }
        val dataById = previous.groupBy { it.id }

        previous.forEachIndexed { i, it ->
          if (!itemsById.contains(it.id)) {
            deleted.add(i)
          }
        }

        next.forEachIndexed { i, it ->
          val oldItem: List<ToDo>? = dataById[it.id]
          if (oldItem != null && oldItem.size > 0) {
            if (oldItem[0].done != it.done || oldItem[0].name != it.name) {
              changed.add(i)
            }
          } else {
            added.add(i)
          }
        }
      }

      val items = next.map {
        UiToDoListItem(id = it.id,
          name = it.name,
          done = it.done)
      }

      UiToDoListUpdate(items,changed.toList(),added.toList(),deleted.toList())

    }.asObservable()

  }

  public fun go() {
    refresh()
  }

  public fun stop(){
    refreshSubscription?.unsubscribe()
  }

  protected fun refresh(){
    refreshSubscription?.unsubscribe()
    refreshSubscription = db.getAll().subscribe {
      behavior.onNext(it.reversed())
    }
  }

  public fun add(title:String){
    db.add(ToDo(title)).subscribe {
      refresh()
    }
  }

  public fun done(toDo:UiToDoListItem,done:Boolean){
    db.markAsDone(toDo.id,done).subscribe {
      refresh()
    }
  }

  public fun remove(toDo:UiToDoListItem) {
    db.delete(toDo.id).subscribe {
      refresh()
    }

  }
}