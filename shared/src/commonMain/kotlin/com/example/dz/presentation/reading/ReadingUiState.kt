package com.example.dz.presentation.reading

data class ReadingUiState(
    val bookId: String = "",
    val bookTitle: String = "Mexican Gothic",
    val chapterLabel: String = "Chapter Three",
    val chapterTitle: String = "The Arrival",
    val paragraphs: List<String> = defaultParagraphs,
    val currentPage: Int = 198,
    val totalPages: Int = 320,
    val bookmarked: Boolean = false
) {
    val progress: Float
        get() = if (totalPages <= 0) 0f else (currentPage.toFloat() / totalPages).coerceIn(0f, 1f)

    val progressPercent: Int
        get() = if (totalPages <= 0) 0 else currentPage * 100 / totalPages
}

val defaultParagraphs: List<String> = listOf(
    "The house appeared out of the mist like something half-remembered from a dream — tall, severe, and utterly silent. Noemí pressed her face to the carriage window and watched the iron gates draw closer.",
    "She had not wanted to come. The city, with its parties and its noise, was where she belonged. Yet the letter had been impossible to ignore.",
    "“We are nearly there,” the driver said, though his voice carried no comfort at all.",
)
