
package com.najdimu.own_material_dialog.message

import android.text.Html
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.annotation.StringRes
import com.najdimu.own_material_dialog.MaterialDialogOwn
import com.najdimu.own_material_dialog.internal.message.LinkTransformationMethod
import com.najdimu.own_material_dialog.R
import com.najdimu.own_material_dialog.utils.MDUtil.resolveFloat
import com.najdimu.own_material_dialog.utils.MDUtil.resolveString

class DialogMessageSettings internal constructor(
    private val dialog: MaterialDialogOwn,
    @Suppress("MemberVisibilityCanBePrivate")
  val messageTextView: TextView
) {
  private var isHtml: Boolean = false
  private var didSetLineSpacing: Boolean = false

  fun lineSpacing(multiplier: Float): DialogMessageSettings {
    didSetLineSpacing = true
    messageTextView.setLineSpacing(0f, multiplier)
    return this
  }

  fun html(onLinkClick: ((link: String) -> Unit)? = null): DialogMessageSettings {
    isHtml = true
    if (onLinkClick != null) {
      messageTextView.transformationMethod = LinkTransformationMethod(onLinkClick)
    }
    messageTextView.movementMethod = LinkMovementMethod.getInstance()
    return this
  }

  internal fun setText(
    @StringRes res: Int?,
    text: CharSequence?
  ) {
    if (!didSetLineSpacing) {
      lineSpacing(
          resolveFloat(
              context = dialog.windowContext,
              attr = R.attr.md_line_spacing_body,
              defaultValue = 1.1f
          )
      )
    }
    messageTextView.text = text.maybeWrapHtml(isHtml)
        ?: resolveString(dialog, res, html = isHtml)
  }

  private fun CharSequence?.maybeWrapHtml(isHtml: Boolean): CharSequence? {
    if (this == null) return null
    @Suppress("DEPRECATION")
    return if (isHtml) Html.fromHtml(this.toString()) else this
  }
}
