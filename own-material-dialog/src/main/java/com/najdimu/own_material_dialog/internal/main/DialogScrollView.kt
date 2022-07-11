package com.najdimu.own_material_dialog.internal.main

import android.content.Context
import android.util.AttributeSet
import android.widget.ScrollView
import androidx.annotation.RestrictTo
import androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP
import com.najdimu.own_material_dialog.utils.MDUtil.waitForWidth


@RestrictTo(LIBRARY_GROUP)
class DialogScrollView(
  context: Context?,
  attrs: AttributeSet? = null
) : ScrollView(context, attrs) {

  var rootView: DialogLayout? = null

  private val isScrollable: Boolean
    get() = getChildAt(0).measuredHeight > height

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    waitForWidth {
      invalidateDividers()
      invalidateOverScroll()
    }
  }

  override fun onScrollChanged(
    left: Int,
    top: Int,
    oldl: Int,
    oldt: Int
  ) {
    super.onScrollChanged(left, top, oldl, oldt)
    invalidateDividers()
  }

  fun invalidateDividers() {
    if (childCount == 0 || measuredHeight == 0 || !isScrollable) {
      rootView?.invalidateDividers(showTop = false, showBottom = false)
      return
    }
    val view = getChildAt(childCount - 1)
    val diff = view.bottom - (measuredHeight + scrollY)
    rootView?.invalidateDividers(
        scrollY > 0,
        diff > 0
    )
  }

  private fun invalidateOverScroll() {
    overScrollMode = if (childCount == 0 || measuredHeight == 0 || !isScrollable) {
      OVER_SCROLL_NEVER
    } else {
      OVER_SCROLL_IF_CONTENT_SCROLLS
    }
  }
}
