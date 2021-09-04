package com.educards.scrollselector

/**
 * [Selector] is a contract to actually perform the selection on a scrollable view (such as [RecyclerView]).
 *
 * In the context of the `ScrollSelector` module, the selection is a second step in
 * the scroll selection procedure.
 *
 * The first step is actually to compute the value of `selectionRatio`.
 *
 * @see SelectionRatioSolver
 */
interface Selector {

    /**
     * Performs the selection on a scrollable view (such as [RecyclerView]).
     *
     * @param selectionRatio
     * Value of the selection ratio which is a result of the current state of scrollable view and [InputParams].
     * For possible values see [interval definition][InputParams.remappedRatio].
     *
     * @param scrollDeltaY Vertical distance scrolled by a scrollable view (such as [RecyclerView]).
     * @param topDistance Distance measured from the top edge of the scrollable content which was used to calculate [selectionRatio].
     * @param topDistance Distance measured from the bottom edge of the scrollable content which was used to calculate [selectionRatio].
     */
    fun onUpdateSelection(
        selectionRatio: Double?,
        scrollDeltaY: Int,
        topDistance: Int?,
        bottomDistance: Int?
    )

}