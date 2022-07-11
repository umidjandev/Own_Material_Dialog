
package com.najdimu.own_material_dialog.internal.main

import android.content.Context
import android.graphics.Paint
import android.graphics.Paint.Style.STROKE
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.annotation.RestrictTo
import androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP
import com.najdimu.own_material_dialog.MaterialDialogOwn
import com.najdimu.own_material_dialog.R
import com.najdimu.own_material_dialog.utils.MDUtil.dimenPx
import com.najdimu.own_material_dialog.utils.MDUtil.resolveColor

@RestrictTo(LIBRARY_GROUP)
abstract class BaseSubLayout internal constructor(
  context: Context,
  attrs: AttributeSet? = null
) : ViewGroup(context, attrs) {

  private val dividerPaint = Paint()
  protected val dividerHeight = dimenPx(R.dimen.md_divider_height)
  lateinit var dialog: MaterialDialogOwn

  var drawDivider: Boolean = false
    set(value) {
      field = value
      invalidate()
    }

  init {
    @Suppress("LeakingThis")
    setWillNotDraw(false)
    dividerPaint.style = STROKE
    dividerPaint.strokeWidth = context.resources.getDimension(R.dimen.md_divider_height)
    dividerPaint.isAntiAlias = true
  }

  protected fun dividerPaint(): Paint {
    dividerPaint.color = getDividerColor()
    return dividerPaint
  }

  private fun getDividerColor(): Int {
    return resolveColor(dialog.context, attr = R.attr.md_divider_color)
  }
}
