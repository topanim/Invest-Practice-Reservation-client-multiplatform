package app.what.reservation.core.foundation.ui

fun <T> List<T>.toggle(item: T): List<T> =
    if (this.contains(item)) this - item else this + item

fun <T> MutableList<T>.toggle(item: T) =
    if (item in this) remove(item) else add(item)