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
     */
    fun onUpdateSelection(
        selectionRatio: Double?,
        topDistance: Int?,
        bottomDistance: Int?
    )

}