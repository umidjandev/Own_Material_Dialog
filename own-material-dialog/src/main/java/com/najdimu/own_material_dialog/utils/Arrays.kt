
package com.najdimu.own_material_dialog.utils

internal inline fun <reified T> List<T>.pullIndices(indices: IntArray): List<T> {
  return mutableListOf<T>().apply {
    for (index in indices) {
      add(this@pullIndices[index])
    }
  }
}
