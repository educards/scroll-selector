package com.educards.scrollselector

import android.view.View
import androidx.core.view.marginBottom
import androidx.core.view.marginTop
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.WeakHashMap
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
     * Measures the maximal scroll potential of [RecyclerView] in a given [direction]
     * with a max limit of [perceptionRangePx].
     *
     * The [perceptionRangePx] limit is required because this implementation might internally
     * create [View] instances to measure the dimension of not yet bound [RecyclerView] items.
     * Therefore a too large value of [perceptionRangePx] may result in poor performance.
     *
     * @return Scroll potential of a [RecyclerView] in a provided [direction] or
     * `null` if the scroll potential is greater then provided [perceptionRangePx],
     * which is particularly the case for large lists and small value of [perceptionRangePx].
     */
    override fun measure(perceptionRangePx: Int, direction: DistanceMeasure.Edge): Int? {

        var positionToEvaluate = if (direction == DistanceMeasure.Edge.BOTTOM) {
            layoutManager.findLastVisibleItemPosition()
        } else {
            layoutManager.findFirstVisibleItemPosition()
        }

        if (positionToEvaluate == RecyclerView.NO_POSITION) {
            return null

        } else {

            val firstChild = layoutManager.findViewByPosition(positionToEvaluate)
                ?: throw RuntimeException("Requested child view has not been laid out")

            var exploredDistancePx: Int = if (direction == DistanceMeasure.Edge.BOTTOM) {
                positionToEvaluate++
                (
                    + firstChild.y.toInt()
                    + firstChild.translationY.toInt()
                    + firstChild.height
                    + firstChild.marginBottom
                    - recyclerView.height
                )
            } else {
                positionToEvaluate--
                (
                    + firstChild.y.toInt()
                    + firstChild.translationY.toInt()
                    - firstChild.marginTop
                )
            }

            // Evaluate views until the perceptionRangePx value
            // or until there are more children to evaluate.
            var offscreenViewHolder: VH
            var childView: View
            while (exploredDistancePx.absoluteValue < perceptionRangePx
                && 0 <= positionToEvaluate && positionToEvaluate < adapter.itemCount) {

                offscreenViewHolder = getOrCreateOffscreenViewHolder(positionToEvaluate)

                // Previously we evaluated the very first or the very last child view (depending on the scroll direction).
                // The next view to examine will therefore lie beyond the drawable boundary.
                // To detect the height of the next/previous child we need to measure it offscreen.
                childView = onBindAndMeasureChild(offscreenViewHolder, positionToEvaluate)

                if (direction == DistanceMeasure.Edge.BOTTOM) {
                    positionToEvaluate++
                    exploredDistancePx += childView.measuredLayoutHeight
                } else {
                    positionToEvaluate--
                    exploredDistancePx -= childView.measuredLayoutHeight
                }
            }

            return if (exploredDistancePx.absoluteValue >= perceptionRangePx) {
                null
            } else {
                exploredDistancePx.absoluteValue
            }
        }
    }

    fun createPhantomViewHolder(position: Int): VH {
        val viewType = adapter.getItemViewType(position)
        var phantomViewHolder = phantomViewHoldersMap[viewType]
        if (phantomViewHolder == null) {
            phantomViewHolder = adapter.onCreateViewHolder(recyclerView, viewType)
            phantomViewHoldersMap[viewType] = phantomViewHolder
        }
        return phantomViewHolder!!
    }

    fun onBindAndMeasureChild(
        phantomViewHolder: VH,
        position: Int): View
    {
        adapter.onBindViewHolder(phantomViewHolder, position)
        layoutManager.measureChild(phantomViewHolder.itemView, 0, 0)
        return phantomViewHolder.itemView
    }

}