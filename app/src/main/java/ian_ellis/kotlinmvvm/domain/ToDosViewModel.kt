package ian_ellis.kotlinmvvm.domain

import android.content.Context
import ian_ellis.kotlinmvvm.R
import ian_ellis.kotlinmvvm.data.store.ToDoSQLHelper
import ian_ellis.kotlinmvvm.data.vo.ToDo
import rx.Observable
import rx.Subscription
import rx.subjects.BehaviorSubject

public class ToDosViewModel(val context: Context) {

  private val db: ToDoSQLHelper = ToDoSQLHelper(context)
  private val toDosBehavior: BehaviorSubject<List<ToDo>>  = BehaviorSubject.create(listOf())
  private val createTitleBehavior: BehaviorSubject<String>  = BehaviorSubject.create("")
  private val createButtonBehavior: BehaviorSubject<Unit>  = BehaviorSubject.create()

  private var refreshSubscription: Subscription? = null
  private var createSubscription: Subscription? = null
  private val title = context.getString(R.string.to_dos_title)

  public fun go() {
    createSubscription = createButtonBehavior.flatMap {
      createTitleBehavior.first()
    }.filter {
      it != ""
    }.flatMap {
      db.add(ToDo(it))
    }.doOnNext{
      createTitleBehavior.onNext("")
    }.subscribe {
      refresh()
    }

    refresh()
  }

  public fun stop(){
    createSubscription?.unsubscribe()
    refreshSubscription?.unsubscribe()
  }

  public fun getToDos(): Observable<UiToDoListUpdate> {
    return Observable.combineLatest(toDosBehavior,createTitleBehavior,{ toDos, createTitle ->
      val items = toDos.map {
        UiToDoListItem(it.id, it.name, it.done)
      }
      UiToDoListUpdate(items,title,createTitle)
    })
  }

  public fun addClicked(){
    createButtonBehavior.onNext(Unit)
  }

  public fun createTitleChanged(title:String) {
    createTitleBehavior.onNext(title)
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

  protected fun refresh(){
    refreshSubscription?.unsubscribe()
    refreshSubscription = db.getAll().subscribe {
      toDosBehavior.onNext(it)
    }
  }
}
