package ian_ellis.androidmvvm.domain

import android.content.Context
import ian_ellis.androidmvvm.data.store.ToDoSQLHelper
import ian_ellis.androidmvvm.data.vo.ToDo
import rx.Observable
import rx.Subscription
import rx.subjects.BehaviorSubject
import java.util.*

public class ToDosViewModel(val context: Context) {

  private val db: ToDoSQLHelper = ToDoSQLHelper(context)
  private var behavior: BehaviorSubject<List<ToDo>> = BehaviorSubject.create()

  private var refreshSubscription: Subscription? = null

  public fun getToDos(): Observable<List<UiToDoListItem>> {
    return behavior.asObservable().map {
      it.map {
        UiToDoListItem(id = it.id,
          name = it.name,
          done = it.done)
      }
    }
  }

  public fun go() {
    refresh()
  }

  protected fun refresh(){
    refreshSubscription?.unsubscribe()
    refreshSubscription = db.getAll().subscribe {
      behavior.onNext(it)
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