package com.educards.scrollselector

interface DistanceMeasure {

    enum class Edge {

        /**
         * Represents the very top edge of the content
         * wrapped by the scrollable view.
         */
        TOP,

        /**
         * Represents the very bottom edge of the content
         * wrapped by the scrollable view.
         */
        BOTTOM
    }

    /**
     * Measures the distance from the current scroll position to the desired [edge].
     * @return The distance (number of pixels) from the current scroll position to the desired [edge].
     * `null` if the edge is too far to be measured. This is particularly the case for large lists.
     */
    fun measure(inputParams: InputParams, edge: Edge): Int?

}