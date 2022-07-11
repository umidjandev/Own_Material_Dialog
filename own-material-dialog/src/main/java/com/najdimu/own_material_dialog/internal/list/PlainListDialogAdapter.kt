package com.najdimu.own_material_dialog.internal.list

import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.najdimu.own_material_dialog.MaterialDialogOwn
import com.najdimu.own_material_dialog.R
import com.najdimu.own_material_dialog.WhichButton.POSITIVE
import com.najdimu.own_material_dialog.actions.hasActionButton
import com.najdimu.own_material_dialog.actions.hasActionButtons
import com.najdimu.own_material_dialog.list.ItemListener
import com.najdimu.own_material_dialog.list.getItemSelector
import com.najdimu.own_material_dialog.utils.MDUtil.inflate
import com.najdimu.own_material_dialog.utils.MDUtil.maybeSetTextColor

private const val KEY_ACTIVATED_INDEX = "activated_index"

internal class PlainListViewHolder(
  itemView: View,
  private val adapter: PlainListDialogAdapter
) : RecyclerView.ViewHolder(itemView), OnClickListener {
  init {
    itemView.setOnClickListener(this)
  }

  val titleView = (itemView as ViewGroup).getChildAt(0) as TextView

  override fun onClick(view: View) = adapter.itemClicked(adapterPosition)
}


internal class PlainListDialogAdapter(
  private var dialog: MaterialDialogOwn,
  internal var items: List<CharSequence>,
  disabledItems: IntArray?,
  private var waitForPositiveButton: Boolean,
  internal var selection: ItemListener
) : RecyclerView.Adapter<PlainListViewHolder>(), DialogAdapter<CharSequence, ItemListener> {

  private var disabledIndices: IntArray = disabledItems ?: IntArray(0)

  fun itemClicked(index: Int) {
    if (waitForPositiveButton && dialog.hasActionButton(POSITIVE)) {
      // Wait for positive action button, mark clicked item as activated so that we can call the
      // selection listener when the button is pressed.
      val lastActivated = dialog.config[KEY_ACTIVATED_INDEX] as? Int
      dialog.config[KEY_ACTIVATED_INDEX] = index
      if (lastActivated != null) {
        notifyItemChanged(lastActivated)
      }
      notifyItemChanged(index)
    } else {
      // Don't wait for action buttons, call listener and dismiss if auto dismiss is applicable
      this.selection?.invoke(dialog, index, this.items[index])
      if (dialog.autoDismissEnabled && !dialog.hasActionButtons()) {
        dialog.dismiss()
      }
    }
  }

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): PlainListViewHolder {
    val listItemView: View = parent.inflate(dialog.windowContext, R.layout.md_listitem)
    val viewHolder = PlainListViewHolder(
        itemView = listItemView,
        adapter = this
    )
    viewHolder.titleView.maybeSetTextColor(dialog.windowContext, R.attr.md_color_content)
    return viewHolder
  }

  override fun getItemCount() = items.size

  override fun onBindViewHolder(
    holder: PlainListViewHolder,
    position: Int
  ) {
    holder.itemView.isEnabled = !disabledIndices.contains(position)

    val titleValue = items[position]
    holder.titleView.text = titleValue
    holder.itemView.background = dialog.getItemSelector()

    val activatedIndex = dialog.config[KEY_ACTIVATED_INDEX] as? Int
    holder.itemView.isActivated = activatedIndex != null && activatedIndex == position

    if (dialog.bodyFont != null) {
      holder.titleView.typeface = dialog.bodyFont
    }
  }

  override fun positiveButtonClicked() {
    val activatedIndex = dialog.config[KEY_ACTIVATED_INDEX] as? Int
    if (activatedIndex != null) {
      selection?.invoke(dialog, activatedIndex, items[activatedIndex])
      dialog.config.remove(KEY_ACTIVATED_INDEX)
    }
  }

  override fun replaceItems(
    items: List<CharSequence>,
    listener: ItemListener
  ) {
    this.items = items
    if (listener != null) {
      this.selection = listener
    }
    this.notifyDataSetChanged()
  }

  override fun disableItems(indices: IntArray) {
    this.disabledIndices = indices
    notifyDataSetChanged()
  }

  override fun checkItems(indices: IntArray) = Unit

  override fun uncheckItems(indices: IntArray) = Unit

  override fun toggleItems(indices: IntArray) = Unit

  override fun checkAllItems() = Unit

  override fun uncheckAllItems() = Unit

  override fun toggleAllChecked() = Unit

  override fun isItemChecked(index: Int) = false
}
