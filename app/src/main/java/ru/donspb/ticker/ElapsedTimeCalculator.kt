package ru.donspb.ticker

import ru.donspb.ticker.model.TickerState

class ElapsedTimeCalculator(private val timestampProvider: TimestampProvider) {
    fun calculate(state: TickerState.Running): Long {
        val currentTimestamp = timestampProvider.getMilliseconds()
        val timePassedSinceStart = if (currentTimestamp > state.startTime) {
            currentTimestamp - state.elapsedTime
        } else {
            0
        }
        return timePassedSinceStart + state.elapsedTime
    }
}