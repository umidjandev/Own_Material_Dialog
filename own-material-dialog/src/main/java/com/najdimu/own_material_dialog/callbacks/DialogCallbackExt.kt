
package com.najdimu.own_material_dialog.callbacks

import com.najdimu.own_material_dialog.DialogCallback
import com.najdimu.own_material_dialog.MaterialDialogOwn


fun MaterialDialogOwn.onPreShow(callback: DialogCallback): MaterialDialogOwn {
  this.preShowListeners.add(callback)
  return this
}


fun MaterialDialogOwn.onShow(callback: DialogCallback): MaterialDialogOwn {
  this.showListeners.add(callback)
  if (this.isShowing) {
    // Already showing, invoke now
    this.showListeners.invokeAll(this)
  }
  setOnShowListener { this.showListeners.invokeAll(this) }
  return this
}


fun MaterialDialogOwn.onDismiss(callback: DialogCallback): MaterialDialogOwn {
  this.dismissListeners.add(callback)
  setOnDismissListener { dismissListeners.invokeAll(this) }
  return this
}

fun MaterialDialogOwn.onCancel(callback: DialogCallback): MaterialDialogOwn {
  this.cancelListeners.add(callback)
  setOnCancelListener { cancelListeners.invokeAll(this) }
  return this
}

internal fun MutableList<DialogCallback>.invokeAll(dialog: MaterialDialogOwn) {
  for (callback in this) {
    callback.invoke(dialog)
  }
}
