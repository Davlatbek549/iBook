package com.example.dz.domain.usecase.book

/**
 * Splits book text into fixed-size pages.
 *
 * Pages are built by grouping whole paragraphs so a page never ends mid-paragraph, unless a single
 * paragraph is longer than [charsPerPage] — in that case it is hard-split on the nearest whitespace
 * before the limit. No page ever exceeds [charsPerPage] characters. Blank input yields no pages.
 */
class BookPaginator(
    private val charsPerPage: Int = DEFAULT_CHARS_PER_PAGE
) {
    init {
        require(charsPerPage > 0) { "charsPerPage must be positive, was $charsPerPage" }
    }

    fun paginate(text: String): List<String> {
        val normalized = text.trim()
        if (normalized.isEmpty()) return emptyList()

        val paragraphs = normalized
            .split(PARAGRAPH_BREAK)
            .map { it.trim() }
            .filter { it.isNotEmpty() }

        val pages = mutableListOf<String>()
        val page = StringBuilder()

        fun flush() {
            if (page.isNotEmpty()) {
                pages += page.toString()
                page.clear()
            }
        }

        for (paragraph in paragraphs) {
            val units =
                if (paragraph.length <= charsPerPage) listOf(paragraph)
                else splitOversizedParagraph(paragraph)

            for (unit in units) {
                when {
                    page.isEmpty() -> page.append(unit)
                    page.length + PARAGRAPH_GAP.length + unit.length <= charsPerPage ->
                        page.append(PARAGRAPH_GAP).append(unit)
                    else -> {
                        flush()
                        page.append(unit)
                    }
                }
            }
        }
        flush()
        return pages
    }

    /** Breaks a paragraph longer than [charsPerPage] into chunks, preferring a whitespace break. */
    private fun splitOversizedParagraph(paragraph: String): List<String> {
        val chunks = mutableListOf<String>()
        var start = 0
        while (start < paragraph.length) {
            val hardEnd = (start + charsPerPage).coerceAtMost(paragraph.length)
            val cut =
                if (hardEnd == paragraph.length) hardEnd
                else lastWhitespaceIndex(paragraph, start, hardEnd).takeIf { it > start } ?: hardEnd
            chunks += paragraph.substring(start, cut).trim()
            start = cut
            while (start < paragraph.length && paragraph[start].isWhitespace()) start++
        }
        return chunks.filter { it.isNotEmpty() }
    }

    private fun lastWhitespaceIndex(text: String, fromInclusive: Int, toExclusive: Int): Int {
        for (index in toExclusive - 1 downTo fromInclusive) {
            if (text[index].isWhitespace()) return index
        }
        return -1
    }

    companion object {
        const val DEFAULT_CHARS_PER_PAGE = 1500
        private const val PARAGRAPH_GAP = "\n\n"
        private val PARAGRAPH_BREAK = Regex("\\n\\s*\\n")
    }
}
