package ian_ellis.kotlinmvvm.domain

import android.content.Context
import ian_ellis.kotlinmvvm.data.store.ToDoSQLHelper
import ian_ellis.kotlinmvvm.data.vo.ToDo
import rx.Observable
import rx.subjects.BehaviorSubject
import rx.subscriptions.CompositeSubscription


class EditViewModel(context: Context) {

  private val db: ToDoSQLHelper = ToDoSQLHelper(context)
  private var behavior: BehaviorSubject<ToDo> = BehaviorSubject.create()
  private val subscriptions: CompositeSubscription = CompositeSubscription()

  public fun getToDo(): Observable<UiToDoEdit> {
    return behavior.asObservable()
      .map {
        UiToDoEdit(it.id, it.name, it.description)
      }.distinctUntilChanged { it.name + it.description }
  }

  public fun update(id: Long, name:String, description:String) {
    subscriptions.add(
      db.update(id, name, description).subscribe {
        behavior.onNext(behavior.value.copy(name = name, description = description))
      }
    )
  }

  public fun go(id: Long) {
    subscriptions.clear()
    subscriptions.add(
      db.getToDo(id).subscribe {
        behavior.onNext(it)
      }
    )
  }

  public fun stop() {
    subscriptions.clear()
  }
}