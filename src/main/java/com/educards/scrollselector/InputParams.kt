package com.educards.scrollselector

/**
 * Input parameters for [SelectionRatioSolver].
 */
// TODO Refactor to SelectionParams or SelectionInputParams
data class InputParams(

    /**
     * Positive integer (number of pixels) which defines how far the [DistanceMeasure]
     * is allowed to look for finding the top edge of the intrinsic content wrapped in a scrollable view (such as [RecyclerView]).
     *
     * * **Impact on transition curve**: The bigger the [topPerceptionRange] the smoother is the transition from
     * [selectionYMid] to content edge.
     * * **Impact on performance**: Too big values of [topPerceptionRange] may result in poor performance. Consult
     * the [DistanceMeasure] implementation for details.
     * * **Recommendation**: For optimal results set the value somewhere within the interval
     * `(scrollableView.height, 2 * scrollableView.height)`.
     */
    var topPerceptionRange: Int = 2500,

    /**
     * Positive integer (number of pixels) which is the equivalent of [topPerceptionRange] for detecting the content bottom edge.
     */
    var bottomPerceptionRange: Int = 2500,

    /**
     * * 0 - Viewport top
     * * 1 - Viewport bottom
     */
    var selectionYMid: Double = .5,

    /**
     * * 0 - Maximal curvature (probably over-fitted for the needs of smooth scroll selection)
     * * 1 - No curvature, straight line
     */
    var stiffness: Double = 0.6,

    /**
     * By default the computed selection ratio is from the interval `(0, 1)`.
     * This attribute can be used to scale and shift the computed ratio
     * to match any arbitrary selection area.
     */
    val selectionArea: SelectionArea = SelectionArea(),

    /**
     * This ratio relatively defines the maximum allowed Y position of the selected entry inside `SelectionArea`.
     * If the relative Y position of selected entry within `SelectionArea` is greater then this value,
     * then the whole view is scrolled to match this criteria. As a consequence
     * * the selected entry is never positioned below/over this threshold
     * * the content is by default not scrolled after the selection is changed,
     *   however it is if the selection this threshold
     */
    var maxSelectionRatio: Double = 0.7

    ) {

    override fun toString(): String {
        return "InputParams(" +
                "topPerceptionRange=$topPerceptionRange, " +
                "bottomPerceptionRange=$bottomPerceptionRange, " +
                "selectionYMid=$selectionYMid, " +
                "stiffness=$stiffness, " +
                "selectionArea=$selectionArea, " +
                "maxSelectionRatio=$maxSelectionRatio)"
    }

}
