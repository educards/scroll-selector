package com.educards.scrollselector

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * [RecyclerView] based implementation of the [Selector].
 * @see [onUpdateSelection]
 */
abstract class RecyclerViewSelector<VH : RecyclerView.ViewHolder>(
    private val recyclerView: RecyclerView,
    private val adapter: RecyclerView.Adapter<VH>,
    private val linearLayoutManager: LinearLayoutManager,
    val inputParams: InputParams
): Selector, RecyclerView.OnScrollListener() {

    val selectionRatioSolver = SelectionRatioSolver()

    var enabled = true

    var updateRequested = true

    val distanceMeasure = RecyclerViewDistanceMeasure(
        recyclerView,
        adapter,
        linearLayoutManager
    )

    init {
        recyclerView.addOnScrollListener(this)
    }

    /**
     * Request to recalculate the selection based on bound [InputParams]
     * and call [onUpdateSelection].
     *
     * By default this is done implicitly when scrolling the view.
     */
    fun requestUpdateSelection() {
        updateRequested = true
        onScrolled(recyclerView, 0, 0)
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

        if (enabled && (updateRequested || dy != 0) // Interested only in changes of 'y'.
        ) {
            updateRequested = false

            val distTop = distanceMeasure.measure(inputParams.topPerceptionRange, DistanceMeasure.Edge.TOP)
            val distBottom = distanceMeasure.measure(inputParams.bottomPerceptionRange, DistanceMeasure.Edge.BOTTOM)
            val ratio = selectionRatioSolver.computeSelectionRatio(inputParams, distTop, distBottom)

            onUpdateSelection(ratio, dy, distTop, distBottom)
        }
    }

    abstract override fun onUpdateSelection(selectionRatio: Double?, scrollDeltaY: Int, topDistance: Int?, bottomDistance: Int?)

}