package com.example.dz

import com.example.dz.presentation.common.uniqueLazyKeys
import kotlin.test.Test
import kotlin.test.assertEquals

class LazyListKeysTest {

    @Test
    fun uniqueIdsAreUsedAsTheyAre() {
        assertEquals(listOf("a", "b", "c"), listOf("a", "b", "c").uniqueLazyKeys { it })
    }

    @Test
    fun aRepeatedIdStillGetsAKeyOfItsOwn() {
        val keys = listOf("a", "b", "a", "a").uniqueLazyKeys { it }

        assertEquals(listOf("a", "b", "a#1", "a#2"), keys)
        assertEquals(keys.size, keys.toSet().size, "a lazy list throws on a duplicate key")
    }

    @Test
    fun blankIdsDoNotCollide() {
        // The Gutendex mapper falls back to an empty id when the API omits one.
        val keys = listOf("", "", "gutenberg-1", "").uniqueLazyKeys { it }

        assertEquals(keys.size, keys.toSet().size)
    }
}
