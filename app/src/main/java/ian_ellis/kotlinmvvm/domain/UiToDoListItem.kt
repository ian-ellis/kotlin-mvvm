package ian_ellis.kotlinmvvm.domain


data class UiToDoListItem(val id: Long,
                          val name: String,
                          var done: Boolean = false)
