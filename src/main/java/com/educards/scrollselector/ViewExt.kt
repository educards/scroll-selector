package com.educards.scrollselector

import android.view.View
import androidx.core.view.marginBottom
import androidx.core.view.marginTop

/**
 * [measuredHeight] + vertical margins (if supported by underlying layout).
 */
inline val View.measuredLayoutHeight: Int
    get() = (marginTop + measuredHeight + marginBottom)

/**
 * [height] + vertical margins (if supported by underlying layout).
 */
inline val View.layoutHeight: Int
    get() = (marginTop + height + marginBottom)
