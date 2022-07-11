
package com.najdimu.own_material_dialog

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager.LayoutParams
import androidx.annotation.ColorInt
import androidx.annotation.Px
import androidx.annotation.StyleRes
import com.najdimu.own_material_dialog.WhichButton.NEGATIVE
import com.najdimu.own_material_dialog.WhichButton.POSITIVE
import com.najdimu.own_material_dialog.actions.getActionButton
import com.najdimu.own_material_dialog.internal.main.DialogLayout
import com.najdimu.own_material_dialog.utils.MDUtil.getWidthAndHeight
import com.najdimu.own_material_dialog.utils.isVisible
import kotlin.math.min

interface DialogBehavior {
  @StyleRes fun getThemeRes(isDark: Boolean): Int

  fun createView(
    creatingContext: Context,
    dialogWindow: Window,
    layoutInflater: LayoutInflater,
    dialog: MaterialDialogOwn
  ): ViewGroup

  fun getDialogLayout(root: ViewGroup): DialogLayout

  fun setWindowConstraints(
    context: Context,
    window: Window,
    view: DialogLayout,
    @Px maxWidth: Int?
  )

  fun setBackgroundColor(
    view: DialogLayout,
    @ColorInt color: Int,
    cornerRadius: Float
  )

  /** Called when the dialog is about to be shown. */
  fun onPreShow(dialog: MaterialDialogOwn)

  /** Called when the dialog is has been shown. */
  fun onPostShow(dialog: MaterialDialogOwn)


  fun onDismiss(): Boolean
}

object ModalDialog : DialogBehavior {
  override fun getThemeRes(isDark: Boolean): Int {
    return if (isDark) {
      R.style.MD_Dark
    } else {
      R.style.MD_Light
    }
  }

  @SuppressLint("InflateParams")
  override fun createView(
    creatingContext: Context,
    dialogWindow: Window,
    layoutInflater: LayoutInflater,
    dialog: MaterialDialogOwn
  ): ViewGroup {
    return layoutInflater.inflate(
        R.layout.md_dialog_base,
        null,
        false
    ) as ViewGroup
  }

  override fun getDialogLayout(root: ViewGroup): DialogLayout {
    return root as DialogLayout
  }

  override fun setWindowConstraints(
    context: Context,
    window: Window,
    view: DialogLayout,
    maxWidth: Int?
  ) {
    if (maxWidth == 0) {
      // Postpone
      return
    }

    window.setSoftInputMode(LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    val wm = window.windowManager ?: return
    val res = context.resources
    val (windowWidth, windowHeight) = wm.getWidthAndHeight()

    val windowVerticalPadding =
      res.getDimensionPixelSize(R.dimen.md_dialog_vertical_margin)
    view.maxHeight = windowHeight - windowVerticalPadding * 2

    val lp = LayoutParams().apply {
      copyFrom(window.attributes)

      val windowHorizontalPadding =
        res.getDimensionPixelSize(R.dimen.md_dialog_horizontal_margin)
      val calculatedWidth = windowWidth - windowHorizontalPadding * 2
      val actualMaxWidth =
        maxWidth ?: res.getDimensionPixelSize(R.dimen.md_dialog_max_width)
      width = min(actualMaxWidth, calculatedWidth)
    }
    window.attributes = lp
  }

  override fun setBackgroundColor(
    view: DialogLayout,
    @ColorInt color: Int,
    cornerRadius: Float
  ) {
    view.cornerRadii = floatArrayOf(
        cornerRadius, cornerRadius, // top left
        cornerRadius, cornerRadius, // top right
        0f, 0f, // bottom left
        0f, 0f // bottom right
    )
    view.background = GradientDrawable().apply {
      this.cornerRadius = cornerRadius
      setColor(color)
    }
  }

  override fun onPreShow(dialog: MaterialDialogOwn) = Unit

  override fun onPostShow(dialog: MaterialDialogOwn) {
    val negativeBtn = dialog.getActionButton(NEGATIVE)
    if (negativeBtn.isVisible()) {
      negativeBtn.post { negativeBtn.requestFocus() }
      return
    }
    val positiveBtn = dialog.getActionButton(POSITIVE)
    if (positiveBtn.isVisible()) {
      positiveBtn.post { positiveBtn.requestFocus() }
    }
  }

  override fun onDismiss(): Boolean = false
}
