
package com.najdimu.own_material_dialog.internal.rtl

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.najdimu.own_material_dialog.utils.setGravityStartCompat


class RtlTextView(
  context: Context,
  attrs: AttributeSet?
) : AppCompatTextView(context, attrs) {
  init {
    setGravityStartCompat()
  }
}
