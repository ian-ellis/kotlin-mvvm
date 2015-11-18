package ian_ellis.kotlinmvvm.domain


data class UiToDoListUpdate(val items: List<UiToDoListItem>,
                            val changed: List<Int>,
                            val added: List<Int>,
                            val deleted: List<Int>)