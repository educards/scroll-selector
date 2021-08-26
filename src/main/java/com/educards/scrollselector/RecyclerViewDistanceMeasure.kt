package com.educards.scrollselector

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.absoluteValue

/**
 * [DistanceMeasure] implementation for [RecyclerView] utilizing [LinearLayoutManager].
 */
class RecyclerViewDistanceMeasure<VH : RecyclerView.ViewHolder>(
    private val recyclerView: RecyclerView,
    private val adapter: RecyclerView.Adapter<VH>,
    private val layoutManager: LinearLayoutManager
): DistanceMeasure {

    private val phantomViewHoldersMap = mutableMapOf<Int, VH>()

    /**
     * Measures the distance from the current scroll position to the desired [edge].
     *
     * This implementation internally creates [View] instance to measure the dimension of hidden elements.
     * Therefore large values of [InputParams.topPerceptionRange] or [InputParams.bottomPerceptionRange] may result in poor performance.
     *
     * @return The distance (number of pixels) from the current scroll position to the desired [edge].
     * `null` if the [edge] is too far to be measured. This is particularly the case for large lists.
     */
    override fun measure(inputParams: InputParams, edge: DistanceMeasure.Edge): Int? {

        var positionToEvaluate: Int
        var perceptionRange: Int
        if (edge == DistanceMeasure.Edge.BOTTOM) {
            positionToEvaluate = layoutManager.findLastVisibleItemPosition()
            perceptionRange = inputParams.bottomPerceptionRange
        } else {
            positionToEvaluate = layoutManager.findFirstVisibleItemPosition()
            perceptionRange = inputParams.topPerceptionRange
        }

        if (positionToEvaluate == RecyclerView.NO_POSITION) {
            return null

        } else {

            val firstChild = layoutManager.findViewByPosition(positionToEvaluate)
                ?: throw RuntimeException("Requested child view has not been laid out")

            var exploredDistance: Int = if (edge == DistanceMeasure.Edge.BOTTOM) {
                positionToEvaluate++
                firstChild.y.toInt() + firstChild.height - recyclerView.height
            } else {
                positionToEvaluate--
                firstChild.y.toInt()
            }

            var phantomViewHolder = getPhantomViewHolder(positionToEvaluate)

            // Evaluate views until the watchAheadDistance is met
            // and there are children to evaluate.
            while (exploredDistance.absoluteValue < perceptionRange
                && 0 <= positionToEvaluate && positionToEvaluate < adapter.itemCount) {

                // Previously we evaluated the very first or the very last child view (depending on the scroll direction).
                // The next view to examine will therefore lie beyond the drawable boundary.
                // To detect the height of the next/previous child we need to measure it offscreen.
                var childView = onBindAndMeasureChild(adapter, phantomViewHolder, positionToEvaluate)

                if (edge == DistanceMeasure.Edge.BOTTOM) {
                    positionToEvaluate++
                    exploredDistance += childView.measuredHeight
                } else {
                    positionToEvaluate--
                    exploredDistance -= childView.measuredHeight
                }
            }

            return if (exploredDistance.absoluteValue >= perceptionRange) {
                null
            } else {
                exploredDistance.absoluteValue
            }
        }
    }

    private fun getPhantomViewHolder(positionToEvaluate: Int): VH {
        val viewType = adapter.getItemViewType(positionToEvaluate)
        var phantomViewHolder = phantomViewHoldersMap[viewType]
        if (phantomViewHolder == null) {
            phantomViewHolder = adapter.onCreateViewHolder(recyclerView, viewType)
            phantomViewHoldersMap[viewType] = phantomViewHolder
        }
        return phantomViewHolder!!
    }

    private fun onBindAndMeasureChild(
        adapter: RecyclerView.Adapter<VH>,
        phantomViewHolder: VH,
        position: Int): View
    {
        adapter.onBindViewHolder(phantomViewHolder, position)
        layoutManager.measureChild(phantomViewHolder.itemView, 0, 0)
        return phantomViewHolder.itemView
    }

}