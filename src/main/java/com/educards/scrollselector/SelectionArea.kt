package com.educards.scrollselector

typealias SelectionAreaChangedListener = (SelectionArea) -> Unit

/**
 * [SelectionArea] represents a subarea of the scrollable view (such as [RecyclerView]) where the selection is allowed.
 * As a consequence all the computations are done relative to [SelectionArea] rather then the bounds of scrollable view.
 *
 * This design allows us to shrink and move the area where selection can occur without changing the dimension (height)
 * of a scrollable view.
 */
class SelectionArea(

    /**
     * Start of selection area relative to the height of scrollable view.
     */
    ratioFrom: Double = 0.0,

    /**
     * End of selection area relative to the height of scrollable view.
     */
    ratioTo: Double = 1.0

) {

    private val selectionAreaChangedListeners = mutableListOf<SelectionAreaChangedListener>()

    fun addSelectionAreaListeners(listener: SelectionAreaChangedListener): SelectionAreaChangedListener {
        selectionAreaChangedListeners.add(listener)
        return listener
    }

    fun removeSelectionAreaListener(listener: SelectionAreaChangedListener) = selectionAreaChangedListeners.remove(listener)

    var ratioFrom = ratioFrom
        set(value) {
            field = value
            checkInterval()
            notifySelectionAreaChanged()
        }

    var ratioTo = ratioTo
        set(value) {
            field = value
            checkInterval()
            notifySelectionAreaChanged()
        }

    private fun checkInterval() {
        if (ratioFrom < 0.0 || ratioTo > 1.0) {
            throw RuntimeException("Interval must be from range [0.0, 1.0], but is [$ratioFrom, $ratioTo]")
        } else if (ratioFrom >= ratioTo) {
            throw RuntimeException("Interval must not be empty, but is [$ratioFrom, $ratioTo]")
        }
    }

    private fun notifySelectionAreaChanged() {
        selectionAreaChangedListeners.forEach {
            it.invoke(this)
        }
    }

    /**
     * @return Absolute start boundary (Y) of the [SelectionArea] relative to scrollable view.
     */
    fun getFromY(scrollableViewHeight: Int): Double {
        return scrollableViewHeight * ratioFrom
    }

    /**
     * @return Absolute end boundary (Y) of the [SelectionArea] relative to scrollable view.
     */
    fun getToY(scrollableViewHeight: Int): Double {
        return scrollableViewHeight * ratioTo
    }

    fun getAreaHeight(viewHeight: Int): Double {
        val fromY = getFromY(viewHeight)
        val toY = getToY(viewHeight)
        return toY - fromY
    }

    /**
     * @return `true` if this [SelectionArea] covers the whole scrollable view upon which it operates.
     */
    fun coversWholeView(): Boolean {
        return ratioFrom == 0.0 && ratioTo == 1.0
    }

    fun remapForView(ratio: Double?) = if (ratio != null) { remapForView(ratio) } else null

    /**
     * [selectionAreaRatio] (`[0.0; 1.0]`) is representation of the selection relative to [SelectionArea].
     *
     * This method remaps the [selectionAreaRatio] value from the space of [SelectionArea]
     * to its enclosing (scrollable) view.
     *
     * After remapping, one can easily obtain 'Y' coordinate of the selection relative to
     * (scrollable) view by:
     * ```
     * y = view.height * remapForView(selectionAreaRatio)
     * ```
     */
    fun remapForView(selectionAreaRatio: Double) =
        if (coversWholeView()) {
            selectionAreaRatio
        } else {
            ratioFrom + ((ratioTo - ratioFrom) * selectionAreaRatio)
        }

    override fun toString(): String {
        return "SelectionArea(ratioFrom=$ratioFrom, ratioTo=$ratioTo)"
    }

}