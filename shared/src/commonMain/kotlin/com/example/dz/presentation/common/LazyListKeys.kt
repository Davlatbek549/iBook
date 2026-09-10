package com.example.dz.presentation.common

/**
 * One key per item for a lazy list: the item's own id, with a repeated id told apart by how many
 * times it has already appeared.
 *
 * A lazy list throws on a duplicate key, and the ids here come from the network — the Gutendex
 * mapper even falls back to an empty id — so a repeat must not be able to crash a screen that the
 * eager Column it replaced would simply have drawn twice. In the normal case every key is exactly
 * the id, which is what keeps a row's identity stable as the list around it changes.
 */
fun <T> List<T>.uniqueLazyKeys(id: (T) -> String): List<String> {
    val seen = HashMap<String, Int>()
    return map { item ->
        val base = id(item)
        val repeat = seen[base] ?: 0
        seen[base] = repeat + 1
        if (repeat == 0) base else "$base#$repeat"
    }
}
