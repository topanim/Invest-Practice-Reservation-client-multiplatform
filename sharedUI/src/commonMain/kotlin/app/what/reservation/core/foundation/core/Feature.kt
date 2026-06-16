package app.what.reservation.core.foundation.core

typealias Listener<T> = (T) -> Any?

abstract class Feature<Ctrl : UIController<*, *, Event>, Event : Any> :
    UIComponent {
    protected abstract val controller: Ctrl
    protected val listener: Listener<Event> by lazy { controller::obtainEvent }
}