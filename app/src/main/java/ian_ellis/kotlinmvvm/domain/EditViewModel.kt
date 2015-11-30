package ian_ellis.kotlinmvvm.domain

import android.content.Context
import ian_ellis.kotlinmvvm.data.store.ToDoSQLHelper
import ian_ellis.kotlinmvvm.data.vo.ToDo
import rx.Observable
import rx.subjects.BehaviorSubject
import rx.subscriptions.CompositeSubscription
import java.util.concurrent.TimeUnit


class EditViewModel(context: Context) {

  private val db: ToDoSQLHelper = ToDoSQLHelper(context)
  private val subscriptions: CompositeSubscription = CompositeSubscription()
  //outgoing streams
  private var behavior: BehaviorSubject<UiToDoEdit> = BehaviorSubject.create()
  //incoming streams
  protected val nameBehavior: BehaviorSubject<String> = BehaviorSubject.create()
  protected val descriptionBehavior: BehaviorSubject<String> = BehaviorSubject.create()
  protected val updateObservable: Observable<Pair<String,String>>

  init {
    updateObservable = Observable.combineLatest(
      nameBehavior,
      descriptionBehavior,
      { name, description ->
        Pair(name, description)
      }
    ).debounce(300, TimeUnit.MILLISECONDS)
  }

  public fun go(id:Long){
    subscriptions.clear()

    subscriptions.add(updateObservable.flatMap {
      val (name,description)  = it
      db.update(id, name, description).map {
        UiToDoEdit(id,name,description)
      }
    }subscribe {
      behavior.onNext(it)
    })

    subscriptions.add(
      db.getToDo(id).subscribe {
        nameBehavior.onNext(it.name)
        descriptionBehavior.onNext(it.description)
      }
    )

  }

  public fun getToDo(): Observable<UiToDoEdit> {
    return behavior.asObservable()
      .distinctUntilChanged { it.name + it.description }
  }

  public fun nameChanged(name:String){
    nameBehavior.onNext(name)
  }

  public fun descriptionChanged(description:String){
    descriptionBehavior.onNext(description)
  }

  public fun update(id: Long, name:String, description:String) {
    subscriptions.add(
      db.update(id, name, description).subscribe {
        behavior.onNext(behavior.value.copy(name = name, description = description))
      }
    )
  }



  public fun stop() {
    subscriptions.clear()
  }
}