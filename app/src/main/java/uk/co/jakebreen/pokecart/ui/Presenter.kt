package uk.co.jakebreen.pokecart.ui

abstract class Presenter<V> {

    private var view: V? = null

    fun attach(view: V) {
        this.view = view
        onAttach(view)
    }

    fun detach() {
        this.view = null
        onDetach()
    }

    abstract fun onAttach(view: V)
    abstract fun onDetach()

}