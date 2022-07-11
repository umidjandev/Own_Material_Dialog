
package com.najdimu.own_material_dialog

import android.R.attr
import android.content.Context
import androidx.annotation.CheckResult
import androidx.annotation.StyleRes
import com.najdimu.own_material_dialog.utils.MDUtil.isColorDark
import com.najdimu.own_material_dialog.utils.MDUtil.resolveColor

@StyleRes @CheckResult
internal fun inferTheme(
  context: Context,
  dialogBehavior: DialogBehavior
): Int {
  val isThemeDark = !inferThemeIsLight(context)
  return dialogBehavior.getThemeRes(isThemeDark)
}

@CheckResult internal fun inferThemeIsLight(context: Context): Boolean {
  return resolveColor(context = context, attr = attr.textColorPrimary).isColorDark()
}
