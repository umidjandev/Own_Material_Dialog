@file:Suppress("unused")

package com.najdimu.own_material_dialog.list

import android.content.res.ColorStateList.valueOf
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.LOLLIPOP
import android.util.Log
import androidx.annotation.ArrayRes
import androidx.annotation.CheckResult
import androidx.annotation.RestrictTo
import androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.najdimu.own_material_dialog.MaterialDialogOwn
import com.najdimu.own_material_dialog.internal.list.PlainListDialogAdapter
import com.najdimu.own_material_dialog.R
import com.najdimu.own_material_dialog.utils.MDUtil.assertOneSet
import com.najdimu.own_material_dialog.utils.MDUtil.getStringArray
import com.najdimu.own_material_dialog.utils.MDUtil.ifNotZero
import com.najdimu.own_material_dialog.utils.MDUtil.resolveDrawable
import com.najdimu.own_material_dialog.utils.resolveColor


@CheckResult fun MaterialDialogOwn.getRecyclerView(): RecyclerView {
  return this.view.contentLayout.recyclerView ?: throw IllegalStateException(
      "This dialog is not a list dialog."
  )
}

/** A shortcut to [RecyclerView.getAdapter] on [getRecyclerView]. */
@CheckResult fun MaterialDialogOwn.getListAdapter(): RecyclerView.Adapter<*>? {
  return this.view.contentLayout.recyclerView?.adapter
}

/**
 * Sets a custom list adapter to render custom list content.
 *
 * Cannot be used in combination with message, input, and some other types of dialogs.
 */
fun MaterialDialogOwn.customListAdapter(
  adapter: RecyclerView.Adapter<*>,
  layoutManager: LayoutManager? = null
): MaterialDialogOwn {
  this.view.contentLayout.addRecyclerView(
      dialog = this,
      adapter = adapter,
      layoutManager = layoutManager
  )
  return this
}


@CheckResult fun MaterialDialogOwn.listItems(
  @ArrayRes res: Int? = null,
  items: List<CharSequence>? = null,
  disabledIndices: IntArray? = null,
  waitForPositiveButton: Boolean = true,
  selection: ItemListener = null
): MaterialDialogOwn {
  assertOneSet("listItems", items, res)
  val array = items ?: windowContext.getStringArray(res).toList()

  if (getListAdapter() != null) {
    Log.w("MaterialDialogs", "Prefer calling updateListItems(...) over listItems(...) again.")
    return updateListItems(
        res = res,
        items = items,
        disabledIndices = disabledIndices,
        selection = selection
    )
  }

  return customListAdapter(
      PlainListDialogAdapter(
          dialog = this,
          items = array,
          disabledItems = disabledIndices,
          waitForPositiveButton = waitForPositiveButton,
          selection = selection
      )
  )
}


fun MaterialDialogOwn.updateListItems(
  @ArrayRes res: Int? = null,
  items: List<CharSequence>? = null,
  disabledIndices: IntArray? = null,
  selection: ItemListener = null
): MaterialDialogOwn {
  assertOneSet("updateListItems", items, res)
  val array = items ?: windowContext.getStringArray(res).toList()
  val adapter = getListAdapter()
  check(adapter is PlainListDialogAdapter) {
    "updateListItems(...) can't be used before you've created a plain list dialog."
  }
  adapter.replaceItems(array, selection)
  disabledIndices?.let(adapter::disableItems)
  return this
}


@RestrictTo(LIBRARY_GROUP)
fun MaterialDialogOwn.getItemSelector(): Drawable? {
  val drawable = resolveDrawable(context = context, attr = R.attr.md_item_selector)
  if (SDK_INT >= LOLLIPOP && drawable is RippleDrawable) {
    resolveColor(attr = R.attr.md_ripple_color).ifNotZero {
      drawable.setColor(valueOf(it))
    }
  }
  return drawable
}
