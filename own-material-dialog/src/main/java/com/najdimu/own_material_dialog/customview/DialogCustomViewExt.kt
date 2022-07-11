
package com.najdimu.own_material_dialog.customview

import android.view.View
import androidx.annotation.CheckResult
import androidx.annotation.LayoutRes
import com.najdimu.own_material_dialog.MaterialDialogOwn
import com.najdimu.own_material_dialog.utils.MDUtil.assertOneSet
import com.najdimu.own_material_dialog.utils.MDUtil.waitForWidth

internal const val CUSTOM_VIEW_NO_VERTICAL_PADDING = "md.custom_view_no_vertical_padding"


@CheckResult fun MaterialDialogOwn.getCustomView(): View {
  return this.view.contentLayout.customView
      ?: error("You have not setup this dialog as a customView dialog.")
}

fun MaterialDialogOwn.customView(
  @LayoutRes viewRes: Int? = null,
  view: View? = null,
  scrollable: Boolean = false,
  noVerticalPadding: Boolean = false,
  horizontalPadding: Boolean = false,
  dialogWrapContent: Boolean = false
): MaterialDialogOwn {
  assertOneSet("customView", view, viewRes)
  config[CUSTOM_VIEW_NO_VERTICAL_PADDING] = noVerticalPadding

  if (dialogWrapContent) {
    // Postpone window measurement so custom view measures itself naturally.
    maxWidth(literal = 0)
  }

  this.view.contentLayout
      .addCustomView(
          res = viewRes,
          view = view,
          scrollable = scrollable,
          horizontalPadding = horizontalPadding,
          noVerticalPadding = noVerticalPadding
      )
      .also {
        if (dialogWrapContent) {
          it.waitForWidth {
            maxWidth(literal = measuredWidth)
          }
        }
      }

  return this
}
