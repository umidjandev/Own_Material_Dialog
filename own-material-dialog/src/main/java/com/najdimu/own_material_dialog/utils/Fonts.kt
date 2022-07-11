
package com.najdimu.own_material_dialog.utils

import android.content.Context
import android.graphics.Typeface
import androidx.annotation.AttrRes
import androidx.annotation.CheckResult
import androidx.annotation.FontRes
import androidx.core.content.res.ResourcesCompat
import com.najdimu.own_material_dialog.MaterialDialogOwn
import com.najdimu.own_material_dialog.utils.MDUtil.assertOneSet

@CheckResult internal fun MaterialDialogOwn.font(
  @FontRes res: Int? = null,
  @AttrRes attr: Int? = null
): Typeface? {
  assertOneSet("font", attr, res)
  if (res != null) {
    return safeGetFont(windowContext, res)
  }
  requireNotNull(attr)
  val a = windowContext.theme.obtainStyledAttributes(intArrayOf(attr))
  try {
    val resId = a.getResourceId(0, 0)
    if (resId == 0) return null
    return safeGetFont(windowContext, resId)
  } finally {
    a.recycle()
  }
}

private fun safeGetFont(context: Context, @FontRes res: Int): Typeface? {
  return try {
    ResourcesCompat.getFont(context, res)
  } catch (e: Throwable) {
    e.printStackTrace()
    null
  }
}
