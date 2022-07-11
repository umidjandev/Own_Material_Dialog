
package com.najdimu.own_material_dialog.list

import com.najdimu.own_material_dialog.MaterialDialogOwn

typealias ItemListener =
    ((dialog: MaterialDialogOwn, index: Int, text: CharSequence) -> Unit)?

typealias SingleChoiceListener =
    ((dialog: MaterialDialogOwn, index: Int, text: CharSequence) -> Unit)?

typealias MultiChoiceListener =
    ((dialog: MaterialDialogOwn, indices: IntArray, items: List<CharSequence>) -> Unit)?
