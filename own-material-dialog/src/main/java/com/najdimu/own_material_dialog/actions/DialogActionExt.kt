
package com.najdimu.own_material_dialog.actions

import com.najdimu.own_material_dialog.MaterialDialogOwn
import com.najdimu.own_material_dialog.WhichButton
import com.najdimu.own_material_dialog.internal.button.DialogActionButton
import com.najdimu.own_material_dialog.utils.isVisible

fun MaterialDialogOwn.hasActionButtons(): Boolean {
  return view.buttonsLayout?.visibleButtons?.isNotEmpty() ?: false
}

fun MaterialDialogOwn.hasActionButton(which: WhichButton) = getActionButton(which).isVisible()

fun MaterialDialogOwn.getActionButton(which: WhichButton): DialogActionButton {
  return view.buttonsLayout?.actionButtons?.get(which.index) ?: throw IllegalStateException(
      "The dialog does not have an attached buttons layout."
  )
}

fun MaterialDialogOwn.setActionButtonEnabled(
  which: WhichButton,
  enabled: Boolean
) {
  getActionButton(which).isEnabled = enabled
}
