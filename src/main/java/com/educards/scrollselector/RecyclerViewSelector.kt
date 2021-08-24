package com.educards.scrollselector

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * [RecyclerView] based implementation of the [Selector].
 */
abstract class RecyclerViewSelector<VH : RecyclerView.ViewHolder>(
    private val recyclerView: RecyclerView,
    private val adapter: RecyclerView.Adapter<VH>,
    private val linearLayoutManager: LinearLayoutManager,
    private val inputParams: InputParams
): Selector, RecyclerView.OnScrollListener() {

    private val solver = SelectionRatioSolver()

    private val distanceMeasure = RecyclerViewDistanceMeasure(
        recyclerView,
        adapter,
        linearLayoutManager
    )

    init {
        recyclerView.addOnScrollListener(this)
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

        if (dy != 0) { // interested only in vertical changes (y)

            val distTop = distanceMeasure.measure(inputParams, DistanceMeasure.Edge.TOP)
            val distBottom = distanceMeasure.measure(inputParams, DistanceMeasure.Edge.BOTTOM)
            val ratio = solver.computeSelectionRatio(inputParams, distTop, distBottom)

            onUpdateSelection(ratio, distTop, distBottom)
        }
    }

    abstract override fun onUpdateSelection(selectionRatio: Double?, topDistance: Int?, bottomDistance: Int?)

}