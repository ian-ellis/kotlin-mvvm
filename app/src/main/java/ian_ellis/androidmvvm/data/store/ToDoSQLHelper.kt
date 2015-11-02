package ian_ellis.androidmvvm.data.store


import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import ian_ellis.androidmvvm.data.vo.ToDo
import rx.Observable
import rx.schedulers.Schedulers
import java.util.*


val DB_NAME = "ToDoDB"
val VERSION = 1
val TABLE_NAME = "todos"
val KEY_ID = "id"
val KEY_NAME = "name"
val KEY_DESCRIPTION = "description"
val KEY_DONE = "done"
val COLUMNS = arrayOf(KEY_ID, KEY_NAME, KEY_DESCRIPTION, KEY_DONE);

public class ToDoSQLHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, VERSION) {

  override fun onCreate(db: SQLiteDatabase) {
    val CREATE_TABLE = """CREATE TABLE $TABLE_NAME
      ( id INTEGER PRIMARY KEY AUTOINCREMENT,
        $KEY_NAME TEXT,
        $KEY_DESCRIPTION TEXT,
        $KEY_DONE INTEGER)"""

    db.execSQL(CREATE_TABLE);
  }

  override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    // Drop older books table if existed
    val sql = "DROP TABLE IF EXISTS $TABLE_NAME"
    db.execSQL(sql);
    // create fresh books table
    this.onCreate(db);
  }

  public fun add(toDo: ToDo): Observable<ToDo> {
    return doAsych {
      val db = this.writableDatabase
      val values = getContentValues(toDo)
      val id = db.insert(TABLE_NAME, null, values)

      db.close()
      ToDo(id = id, name = toDo.name, description = toDo.description, done = toDo.done)
    }
  }

  public fun delete(id: Long): Observable<Unit> {
    return doAsych {
      val db = this.writableDatabase
      db.delete(TABLE_NAME, "$KEY_ID  = ?", arrayOf(id.toString()))
      db.close();
      Unit
    }
  }

  public fun markAsDone(id: Long, done: Boolean): Observable<Unit> {
    return doAsych {
      val db = this.writableDatabase;
      val values = ContentValues()
      values.put(KEY_DONE, if (done) {
        1
      } else {
        0
      })
      db.update(TABLE_NAME, values, "$KEY_ID = ?", arrayOf(id.toString()))
      db.close();
      Unit
    }
  }

  public fun update(toDo: ToDo): Observable<ToDo> {
    return doAsych {
      val db = this.writableDatabase;
      val values = getContentValues(toDo)
      db.update(TABLE_NAME, values, "$KEY_ID = ?", arrayOf(toDo.id.toString()))
      db.close();
      toDo
    }
  }


  public fun getAll(): Observable<List<ToDo>> {
    return doAsych {
      val toDos = LinkedList<ToDo>();
      val query = "SELECT  * FROM $TABLE_NAME"
      val db = this.writableDatabase;
      val cursor = db.rawQuery(query, null);

      if (cursor.moveToFirst()) {

        do {
          toDos.add(ToDo(
            cursor.getString(1),
            cursor.getString(2),
            if (cursor.getInt(3) == 1) {
              true
            } else {
              false
            },
            cursor.getLong(0)
          ))
        } while (cursor.moveToNext())
      }
      toDos
    }
  }


  public fun getToDo(id: Long): ToDo {
    val db = this.readableDatabase

    val cursor = db.query(TABLE_NAME,
      COLUMNS, // column names
      " id = ?", // selections
      arrayOf(id.toString()), // selections args
      null, // group by
      null, // having
      null, // order by
      null); // limit

    cursor?.moveToFirst();

    val toDo = ToDo(
      cursor.getString(0),
      cursor.getString(1),
      if (cursor.getInt(2) == 1) {
        true
      } else {
        false
      }
    );

    return toDo;
  }

  protected fun <T> doAsych(action: () -> T): Observable<T> {
    return Observable.create<T>() { subscriber ->
      try {
        val result = action.invoke()
        subscriber.onNext(result)
        subscriber.onCompleted()
      } catch(e: Exception) {
        subscriber.onError(e)
      }

    }.subscribeOn(Schedulers.io())
  }


  protected fun getContentValues(toDo: ToDo): ContentValues {
    val values = ContentValues()
    values.put(KEY_NAME, toDo.name)
    values.put(KEY_DESCRIPTION, toDo.description)
    values.put(KEY_DONE, if (toDo.done) {
      1
    } else {
      0
    })

    return values
  }

}