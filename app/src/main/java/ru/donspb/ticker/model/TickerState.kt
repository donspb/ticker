package ru.donspb.ticker.model

sealed class TickerState {
    data class Paused(
        val elapsedTime: Long
    ) : TickerState()

    data class Running(
        val startTime: Long,
        val elapsedTime: Long
    ) : TickerState()
}
