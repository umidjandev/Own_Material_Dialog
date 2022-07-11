
package com.najdimu.own_material_dialog.utils

internal fun IntArray.appendAll(values: Collection<Int>): IntArray {
  return toMutableList()
      .apply { addAll(values) }
      .toIntArray()
}

internal fun IntArray.removeAll(values: Collection<Int>): IntArray {
  return toMutableList().apply { removeAll { values.contains(it) } }
      .toIntArray()
}
