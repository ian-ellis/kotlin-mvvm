package ian_ellis.androidmvvm.domain


data class UiToDoListItem(val id: Long,
                          val name: String,
                          var done: Boolean = false
)
