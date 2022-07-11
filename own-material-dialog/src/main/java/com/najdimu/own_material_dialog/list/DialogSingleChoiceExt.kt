
@file:Suppress("unused")

package com.najdimu.own_material_dialog.list

import android.util.Log
import androidx.annotation.ArrayRes
import androidx.annotation.CheckResult
import androidx.annotation.ColorInt
import com.najdimu.own_material_dialog.MaterialDialogOwn
import com.najdimu.own_material_dialog.WhichButton.POSITIVE
import com.najdimu.own_material_dialog.actions.setActionButtonEnabled
import com.najdimu.own_material_dialog.internal.list.DialogAdapter
import com.najdimu.own_material_dialog.internal.list.SingleChoiceDialogAdapter
import com.najdimu.own_material_dialog.utils.MDUtil.assertOneSet
import com.najdimu.own_material_dialog.utils.MDUtil.getStringArray


@CheckResult fun MaterialDialogOwn.listItemsSingleChoice(
  @ArrayRes res: Int? = null,
  items: List<CharSequence>? = null,
  disabledIndices: IntArray? = null,
  initialSelection: Int = -1,
  waitForPositiveButton: Boolean = true,
  @ColorInt checkedColor: Int = -1,
  @ColorInt uncheckedColor: Int = -1,
  selection: SingleChoiceListener = null
): MaterialDialogOwn {
  assertOneSet("listItemsSingleChoice", items, res)
  val array = items ?: windowContext.getStringArray(res).toList()
  require(initialSelection >= -1 || initialSelection < array.size) {
    "Initial selection $initialSelection must be between -1 and " +
        "the size of your items array ${array.size}"
  }

  if (getListAdapter() != null) {
    Log.w(
        "MaterialDialogs",
        "Prefer calling updateListItemsSingleChoice(...) over listItemsSingleChoice(...) again."
    )
    return updateListItemsSingleChoice(
        res = res,
        items = items,
        disabledIndices = disabledIndices,
        selection = selection
    )
  }

  setActionButtonEnabled(POSITIVE, initialSelection > -1)
  return customListAdapter(
      SingleChoiceDialogAdapter(
          dialog = this,
          items = array,
          disabledItems = disabledIndices,
          initialSelection = initialSelection,
          waitForActionButton = waitForPositiveButton,
          selection = selection,
          checkedColor = checkedColor,
          uncheckedColor = uncheckedColor
      )
  )
}


fun MaterialDialogOwn.updateListItemsSingleChoice(
  @ArrayRes res: Int? = null,
  items: List<CharSequence>? = null,
  disabledIndices: IntArray? = null,
  selection: SingleChoiceListener = null
): MaterialDialogOwn {
  assertOneSet("updateListItemsSingleChoice", items, res)
  val array = items ?: windowContext.getStringArray(res).toList()
  val adapter = getListAdapter()
  check(adapter is SingleChoiceDialogAdapter) {
    "updateListItemsSingleChoice(...) can't be used before you've created a single choice list dialog."
  }
  adapter.replaceItems(array, selection)
  disabledIndices?.let(adapter::disableItems)
  return this
}

/** Checks a single or multiple choice list item. */
fun MaterialDialogOwn.checkItem(index: Int) {
  val adapter = getListAdapter()
  if (adapter is DialogAdapter<*, *>) {
    adapter.checkItems(intArrayOf(index))
    return
  }
  throw UnsupportedOperationException(
      "Can't check item on adapter: ${adapter?.javaClass?.name ?: "null"}"
  )
}

/** Unchecks a single or multiple choice list item. */
fun MaterialDialogOwn.uncheckItem(index: Int) {
  val adapter = getListAdapter()
  if (adapter is DialogAdapter<*, *>) {
    adapter.uncheckItems(intArrayOf(index))
    return
  }
  throw UnsupportedOperationException(
      "Can't uncheck item on adapter: ${adapter?.javaClass?.name ?: "null"}"
  )
}

/** Checks or unchecks a single or multiple choice list item. */
fun MaterialDialogOwn.toggleItemChecked(index: Int) {
  val adapter = getListAdapter()
  if (adapter is DialogAdapter<*, *>) {
    adapter.toggleItems(intArrayOf(index))
    return
  }
  throw UnsupportedOperationException(
      "Can't toggle checked item on adapter: ${adapter?.javaClass?.name ?: "null"}"
  )
}

/** Returns true if a single or multiple list item is checked. */
fun MaterialDialogOwn.isItemChecked(index: Int): Boolean {
  val adapter = getListAdapter()
  if (adapter is DialogAdapter<*, *>) {
    return adapter.isItemChecked(index)
  }
  throw UnsupportedOperationException(
      "Can't check if item is checked on adapter: ${adapter?.javaClass?.name ?: "null"}"
  )
}
