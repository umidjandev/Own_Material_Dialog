
@file:Suppress("unused")

package com.najdimu.own_material_dialog.internal.message

import android.text.style.URLSpan
import android.view.View


class CustomUrlSpan(
  url: String,
  private val onLinkClick: (String) -> Unit
) : URLSpan(url) {
  override fun onClick(widget: View) = onLinkClick(url)
}
