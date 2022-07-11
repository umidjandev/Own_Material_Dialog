package com.najdimu.own_material_dialog.internal.list

import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.core.widget.CompoundButtonCompat
import androidx.recyclerview.widget.RecyclerView
import com.najdimu.own_material_dialog.MaterialDialogOwn
import com.najdimu.own_material_dialog.R
import com.najdimu.own_material_dialog.WhichButton.POSITIVE
import com.najdimu.own_material_dialog.actions.hasActionButtons
import com.najdimu.own_material_dialog.actions.setActionButtonEnabled
import com.najdimu.own_material_dialog.list.SingleChoiceListener
import com.najdimu.own_material_dialog.list.getItemSelector
import com.najdimu.own_material_dialog.utils.MDUtil.createColorSelector
import com.najdimu.own_material_dialog.utils.MDUtil.inflate
import com.najdimu.own_material_dialog.utils.MDUtil.maybeSetTextColor
import com.najdimu.own_material_dialog.utils.resolveColors

internal class SingleChoiceViewHolder(
  itemView: View,
  private val adapter: SingleChoiceDialogAdapter
) : RecyclerView.ViewHolder(itemView), OnClickListener {

  init {
    itemView.setOnClickListener(this)
  }

  val controlView: AppCompatRadioButton = itemView.findViewById(R.id.md_control)
  val titleView: TextView = itemView.findViewById(R.id.md_title)

  var isEnabled: Boolean
    get() = itemView.isEnabled
    set(value) {
      itemView.isEnabled = value
      controlView.isEnabled = value
      titleView.isEnabled = value
    }

  override fun onClick(view: View) {
    if (adapterPosition < 0) return
    adapter.itemClicked(adapterPosition)
  }
}


internal class SingleChoiceDialogAdapter(
  private var dialog: MaterialDialogOwn,
  internal var items: List<CharSequence>,
  disabledItems: IntArray?,
  initialSelection: Int,
  private val waitForActionButton: Boolean,
  internal var selection: SingleChoiceListener,
  @ColorInt private val checkedColor: Int,
  @ColorInt private val uncheckedColor: Int
) : RecyclerView.Adapter<SingleChoiceViewHolder>(),
  DialogAdapter<CharSequence, SingleChoiceListener> {

  private var currentSelection: Int = initialSelection
    set(value) {
      if (value == field) return
      val previousSelection = field
      field = value
      notifyItemChanged(previousSelection, UncheckPayload)
      notifyItemChanged(value, CheckPayload)
    }
  private var disabledIndices: IntArray = disabledItems ?: IntArray(0)

  internal fun itemClicked(index: Int) {
    this.currentSelection = index
    if (waitForActionButton && dialog.hasActionButtons()) {
      // Wait for action button, don't call listener
      // so that positive action button press can do so later.
      dialog.setActionButtonEnabled(POSITIVE, true)
    } else {
      // Don't wait for action button, call listener and dismiss if auto dismiss is applicable
      this.selection?.invoke(dialog, index, this.items[index])
      if (dialog.autoDismissEnabled && !dialog.hasActionButtons()) {
        dialog.dismiss()
      }
    }
  }

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): SingleChoiceViewHolder {
    val listItemView: View = parent.inflate(dialog.windowContext, R.layout.md_listitem_singlechoice)
    val viewHolder = SingleChoiceViewHolder(
        itemView = listItemView,
        adapter = this
    )
    viewHolder.titleView.maybeSetTextColor(dialog.windowContext, R.attr.md_color_content)

    val widgetAttrs = intArrayOf(R.attr.md_color_widget, R.attr.md_color_widget_unchecked)
    dialog.resolveColors(attrs = widgetAttrs)
        .let {
          CompoundButtonCompat.setButtonTintList(
              viewHolder.controlView,
              createColorSelector(dialog.windowContext,
                  checked = if (checkedColor == -1) it[0] else checkedColor,
                  unchecked = if (uncheckedColor == -1) it[1] else uncheckedColor
              )
          )
        }

    return viewHolder
  }

  override fun getItemCount() = items.size

  override fun onBindViewHolder(
    holder: SingleChoiceViewHolder,
    position: Int
  ) {
    holder.isEnabled = !disabledIndices.contains(position)
    holder.controlView.isChecked = currentSelection == position

    holder.titleView.text = items[position]
    holder.itemView.background = dialog.getItemSelector()

    if (dialog.bodyFont != null) {
      holder.titleView.typeface = dialog.bodyFont
    }
  }

  override fun onBindViewHolder(
    holder: SingleChoiceViewHolder,
    position: Int,
    payloads: MutableList<Any>
  ) {
    when (payloads.firstOrNull()) {
      CheckPayload -> {
        holder.controlView.isChecked = true
        return
      }
      UncheckPayload -> {
        holder.controlView.isChecked = false
        return
      }
    }
    super.onBindViewHolder(holder, position, payloads)
  }

  override fun positiveButtonClicked() {
    if (currentSelection > -1) {
      selection?.invoke(dialog, currentSelection, items[currentSelection])
    }
  }

  override fun replaceItems(
    items: List<CharSequence>,
    listener: SingleChoiceListener
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

  override fun checkItems(indices: IntArray) {
    val targetIndex = if (indices.isNotEmpty()) indices[0] else -1
    check(targetIndex >= 0 && targetIndex < items.size) {
      "Index $targetIndex is out of range for this adapter of ${items.size} items."
    }
    if (this.disabledIndices.contains(targetIndex)) return
    this.currentSelection = targetIndex
  }

  override fun uncheckItems(indices: IntArray) {
    val targetIndex = if (indices.isNotEmpty()) indices[0] else -1
    check(targetIndex >= 0 && targetIndex < items.size) {
      "Index $targetIndex is out of range for this adapter of ${items.size} items."
    }
    if (this.disabledIndices.contains(targetIndex)) return
    this.currentSelection = -1
  }

  override fun toggleItems(indices: IntArray) {
    val targetIndex = if (indices.isNotEmpty()) indices[0] else -1
    if (this.disabledIndices.contains(targetIndex)) return
    if (indices.isEmpty() || this.currentSelection == targetIndex) {
      this.currentSelection = -1
    } else {
      this.currentSelection = targetIndex
    }
  }

  override fun checkAllItems() = Unit

  override fun uncheckAllItems() = Unit

  override fun toggleAllChecked() = Unit

  override fun isItemChecked(index: Int) = this.currentSelection == index
}
