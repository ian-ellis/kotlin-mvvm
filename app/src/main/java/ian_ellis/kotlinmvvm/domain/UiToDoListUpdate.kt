package ian_ellis.kotlinmvvm.domain


data class UiToDoListUpdate(val items: List<UiToDoListItem>,
                            val title: String,
                            val createTitle: String)