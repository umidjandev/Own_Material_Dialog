
package com.najdimu.own_material_dialog.internal.list

interface DialogAdapter<in IT, in SL> {
  fun replaceItems(
    items: List<IT>,
    listener: SL? = null
  )

  fun disableItems(indices: IntArray)

  fun checkItems(indices: IntArray)

  fun uncheckItems(indices: IntArray)

  fun toggleItems(indices: IntArray)

  fun checkAllItems()

  fun uncheckAllItems()

  fun toggleAllChecked()

  fun isItemChecked(index: Int): Boolean

  fun getItemCount(): Int

  fun positiveButtonClicked()
}
