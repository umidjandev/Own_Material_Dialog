
@file:Suppress("unused")

package com.najdimu.own_material_dialog.checkbox

import android.view.View
import android.widget.CheckBox
import androidx.annotation.CheckResult
import androidx.annotation.StringRes
import androidx.core.widget.CompoundButtonCompat
import com.najdimu.own_material_dialog.MaterialDialogOwn
import com.najdimu.own_material_dialog.R
import com.najdimu.own_material_dialog.utils.MDUtil.assertOneSet
import com.najdimu.own_material_dialog.utils.MDUtil.createColorSelector
import com.najdimu.own_material_dialog.utils.MDUtil.maybeSetTextColor
import com.najdimu.own_material_dialog.utils.MDUtil.resolveString
import com.najdimu.own_material_dialog.utils.resolveColors

typealias BooleanCallback = ((Boolean) -> Unit)?

@CheckResult fun MaterialDialogOwn.getCheckBoxPrompt(): CheckBox {
  return view.buttonsLayout?.checkBoxPrompt ?: throw IllegalStateException(
      "The dialog does not have an attached buttons layout."
  )
}

@CheckResult fun MaterialDialogOwn.isCheckPromptChecked() = getCheckBoxPrompt().isChecked


fun MaterialDialogOwn.checkBoxPrompt(
  @StringRes res: Int = 0,
  text: String? = null,
  isCheckedDefault: Boolean = false,
  onToggle: BooleanCallback
): MaterialDialogOwn {
  assertOneSet("checkBoxPrompt", text, res)
  view.buttonsLayout?.checkBoxPrompt?.run {
    this.visibility = View.VISIBLE
    this.text = text ?: resolveString(this@checkBoxPrompt, res)
    this.isChecked = isCheckedDefault
    this.setOnCheckedChangeListener { _, checked ->
      onToggle?.invoke(checked)
    }
    maybeSetTextColor(windowContext, R.attr.md_color_content)
    bodyFont?.let(this::setTypeface)

    val widgetAttrs = intArrayOf(R.attr.md_color_widget, R.attr.md_color_widget_unchecked)
    resolveColors(attrs = widgetAttrs).let {
      CompoundButtonCompat.setButtonTintList(
          this,
          createColorSelector(windowContext, checked = it[0], unchecked = it[1])
      )
    }
  }
  return this
}
