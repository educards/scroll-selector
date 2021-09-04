package com.educards.scrollselector

/**
 * Input parameters for [SelectionRatioSolver].
 */
data class InputParams(

    /**
     * Positive integer (number of pixels) which defines how far the [DistanceMeasure]
     * is allowed to look for finding the top edge of the content wrapped in a scrollable [View] (such as [RecyclerView]).
     *
     * * **Impact on transition curve**: The bigger the [topPerceptionRange] the smoother is the transition from
     * [selectionYMid] to content edge.
     * * **Impact on performance**: Too big values of [topPerceptionRange] may result in poor performance. Consult
     * the [DistanceMeasure] implementation for details.
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
     * * 0 - TODO define
     * * 1 - Straight line
     */
    var stiffness: Double = 0.5,

    /**
     * By default the computed selection ratio is from the interval `(0, 1)`.
     * This attribute can be used to specify a custom interval.
     */
    var remappedRatio: Pair<Double, Double>? = null,

)
