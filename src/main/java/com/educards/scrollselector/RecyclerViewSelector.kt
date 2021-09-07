package com.educards.scrollselector

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * [RecyclerView] based implementation of the [Selector].
 *
 * Selector has to be explicitly initialized ([init()][init]) after instance is created.
 */
abstract class RecyclerViewSelector(
    private val recyclerView: RecyclerView,
    private val adapter: RecyclerView.Adapter<*>,
    private val linearLayoutManager: LinearLayoutManager,
    private val inputParams: InputParams
): Selector, RecyclerView.OnScrollListener() {

    val selectionRatioSolver = SelectionRatioSolver()

    var enabled = true

    private val distanceMeasure = RecyclerViewDistanceMeasure(
        recyclerView,
        adapter,
        linearLayoutManager
    )

    init {
        recyclerView.addOnScrollListener(this)
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

        if (enabled
            && dy != 0 // interested only in vertical changes (y)
        ) {

            val distTop = distanceMeasure.measure(inputParams, DistanceMeasure.Edge.TOP)
            val distBottom = distanceMeasure.measure(inputParams, DistanceMeasure.Edge.BOTTOM)
            val ratio = selectionRatioSolver.computeSelectionRatio(inputParams, distTop, distBottom)

            onUpdateSelection(ratio, dy, distTop, distBottom)
        }
    }

    abstract override fun onUpdateSelection(selectionRatio: Double?, scrollDeltaY: Int, topDistance: Int?, bottomDistance: Int?)

}