
package com.najdimu.own_material_dialog.internal.list

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.RestrictTo
import androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.najdimu.own_material_dialog.MaterialDialogOwn
import com.najdimu.own_material_dialog.utils.MDUtil.waitForWidth
import com.najdimu.own_material_dialog.utils.invalidateDividers

typealias InvalidateDividersDelegate = (scrolledDown: Boolean, atBottom: Boolean) -> Unit


@RestrictTo(LIBRARY_GROUP)
class DialogRecyclerView(
  context: Context,
  attrs: AttributeSet? = null
) : RecyclerView(context, attrs) {

  private var invalidateDividersDelegate: InvalidateDividersDelegate? = null

  fun attach(dialog: MaterialDialogOwn) {
    this.invalidateDividersDelegate = dialog::invalidateDividers
  }

  fun invalidateDividers() {
    if (childCount == 0 || measuredHeight == 0) return
    invalidateDividersDelegate?.invoke(!isAtTop(), !isAtBottom())
  }

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    waitForWidth {
      invalidateDividers()
      invalidateOverScroll()
    }
    addOnScrollListener(scrollListeners)
  }

  override fun onDetachedFromWindow() {
    removeOnScrollListener(scrollListeners)
    super.onDetachedFromWindow()
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

  private fun isAtTop(): Boolean {
    return when (val lm = layoutManager) {
      is LinearLayoutManager -> lm.findFirstCompletelyVisibleItemPosition() == 0
      is GridLayoutManager -> lm.findFirstCompletelyVisibleItemPosition() == 0
      else -> false
    }
  }

  private fun isAtBottom(): Boolean {
    val lastIndex = adapter!!.itemCount - 1
    return when (val lm = layoutManager) {
      is LinearLayoutManager -> lm.findLastCompletelyVisibleItemPosition() == lastIndex
      is GridLayoutManager -> lm.findLastCompletelyVisibleItemPosition() == lastIndex
      else -> false
    }
  }

  private fun isScrollable() = isAtBottom() && isAtTop()

  private val scrollListeners = object : RecyclerView.OnScrollListener() {
    override fun onScrolled(
      recyclerView: RecyclerView,
      dx: Int,
      dy: Int
    ) {
      super.onScrolled(recyclerView, dx, dy)
      invalidateDividers()
    }
  }

  private fun invalidateOverScroll() {
    overScrollMode = when {
      childCount == 0 || measuredHeight == 0 -> OVER_SCROLL_NEVER
      isScrollable() -> OVER_SCROLL_NEVER
      else -> OVER_SCROLL_IF_CONTENT_SCROLLS
    }
  }
}
