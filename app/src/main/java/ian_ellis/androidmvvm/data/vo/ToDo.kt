package ian_ellis.androidmvvm.data.vo

/**
 * Created by ianellis on 24/07/15.
 */
public data class ToDo(val name:String,
                       val description:String = "",
                       val done:Boolean = false,
                       val id:Long = -1)