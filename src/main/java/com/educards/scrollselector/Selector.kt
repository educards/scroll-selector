package com.educards.scrollselector

/**
 * [Selector] is a contract to perform the selection on a scrollable view (such as [RecyclerView])
 * based on `selectionRatio` which is a real value representing the vertical position of the selection.
 * * 0.0 - the top edge of the scrollable view
 * * 0.5 - the middle part of the scrollable view
 * * 1.0 - the bottom edge of the scrollable view
 *
 * In the context of the `ScrollSelector` module, the selection is a second step in
 * the scroll selection procedure.
 *
 * The first step is actually to compute the value of `selectionRatio` (see [SelectionRatioSolver]).
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