
package com.najdimu.own_material_dialog.internal.button

import android.content.Context
import android.content.res.ColorStateList.valueOf
import android.graphics.drawable.RippleDrawable
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.LOLLIPOP
import android.util.AttributeSet
import android.view.Gravity.CENTER
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatButton
import com.najdimu.own_material_dialog.R
import com.najdimu.own_material_dialog.inferThemeIsLight
import com.najdimu.own_material_dialog.utils.MDUtil.ifNotZero
import com.najdimu.own_material_dialog.utils.MDUtil.resolveColor
import com.najdimu.own_material_dialog.utils.MDUtil.resolveDrawable
import com.najdimu.own_material_dialog.utils.MDUtil.resolveInt
import com.najdimu.own_material_dialog.utils.adjustAlpha
import com.najdimu.own_material_dialog.utils.setGravityEndCompat


class DialogActionButton(
  context: Context,
  attrs: AttributeSet? = null
) : AppCompatButton(context, attrs) {
  companion object {
    private const val CASING_UPPER = 1
  }

  private var enabledColor: Int = 0
  private var disabledColor: Int = 0
  private var enabledColorOverride: Int? = null

  init {
    isClickable = true
    isFocusable = true
  }

  internal fun update(
    baseContext: Context,
    appContext: Context,
    stacked: Boolean
  ) {
    // Casing
    val casing = resolveInt(
        context = appContext,
        attr = R.attr.md_button_casing,
        defaultValue = CASING_UPPER
    )
    setSupportAllCaps(casing == CASING_UPPER)

    // Text color
    val isLightTheme = inferThemeIsLight(appContext)
    enabledColor = resolveColor(appContext, attr = R.attr.md_color_button_text) {
      resolveColor(appContext, attr = com.google.android.material.R.attr.colorPrimary)
    }
    val disabledColorRes =
      if (isLightTheme) R.color.md_disabled_text_light_theme
      else R.color.md_disabled_text_dark_theme
    disabledColor = resolveColor(baseContext, res = disabledColorRes)
    setTextColor(enabledColorOverride ?: enabledColor)

    // Selector
    val bgDrawable = resolveDrawable(baseContext, attr = R.attr.md_button_selector)
    if (SDK_INT >= LOLLIPOP && bgDrawable is RippleDrawable) {
      resolveColor(context = baseContext, attr = R.attr.md_ripple_color) {
        resolveColor(appContext, attr = com.google.android.material.R.attr.colorPrimary).adjustAlpha(.12f)
      }.ifNotZero {
        bgDrawable.setColor(valueOf(it))
      }
    }
    background = bgDrawable

    // Text alignment
    if (stacked) setGravityEndCompat()
    else gravity = CENTER

    // Invalidate in case enabled state was changed before this method executed
    isEnabled = isEnabled
  }

  fun updateTextColor(@ColorInt color: Int) {
    enabledColor = color
    enabledColorOverride = color
    isEnabled = isEnabled
  }

  override fun setEnabled(enabled: Boolean) {
    super.setEnabled(enabled)
    setTextColor(if (enabled) enabledColorOverride ?: enabledColor else disabledColor)
  }
}
