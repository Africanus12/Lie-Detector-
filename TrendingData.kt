package com.liedetector.app.domain

data class TrendingItem(
    val message: String,
    val truthScore: Int
)

object TrendingData {
    val trendingMessages = listOf(
        TrendingItem("I'll pay you back tomorrow", 12),
        TrendingItem("I'm just busy these days", 41),
        TrendingItem("I still love you", 54),
        TrendingItem("I was sleeping", 21),
        TrendingItem("My phone died", 25),
        TrendingItem("I'm just busy right now", 33),
        TrendingItem("I'll send it tomorrow", 17),
        TrendingItem("I'm on my way", 18),
        TrendingItem("Sorry I didn't see your message", 28),
        TrendingItem("I was in a meeting", 38),
        TrendingItem("It's not what it looks like", 9),
        TrendingItem("I'll call you later", 31),
        TrendingItem("Let's talk tomorrow", 44),
        TrendingItem("I forgot", 35),
        TrendingItem("We're just friends", 22)
    )

    val topLiesThisWeek: List<TrendingItem>
        get() = trendingMessages.sortedBy { it.truthScore }.take(10)

    fun getRandomTrending(count: Int = 5): List<TrendingItem> =
        trendingMessages.shuffled().take(count)
}
