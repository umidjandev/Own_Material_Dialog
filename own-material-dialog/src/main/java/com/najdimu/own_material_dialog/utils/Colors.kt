
package com.najdimu.own_material_dialog.utils

import android.graphics.Color
import androidx.annotation.AttrRes
import androidx.annotation.CheckResult
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import com.najdimu.own_material_dialog.MaterialDialogOwn
import com.najdimu.own_material_dialog.utils.MDUtil.resolveColor
import com.najdimu.own_material_dialog.utils.MDUtil.resolveColors

@ColorInt @CheckResult
internal fun MaterialDialogOwn.resolveColor(
  @ColorRes res: Int? = null,
  @AttrRes attr: Int? = null,
  fallback: (() -> Int)? = null
): Int = resolveColor(windowContext, res, attr, fallback)

@CheckResult
internal fun MaterialDialogOwn.resolveColors(
  attrs: IntArray,
  fallback: ((forAttr: Int) -> Int)? = null
) = resolveColors(windowContext, attrs, fallback)

@ColorInt @CheckResult
internal fun Int.adjustAlpha(alpha: Float): Int {
  return Color.argb((255 * alpha).toInt(), Color.red(this), Color.green(this), Color.blue(this))
}
