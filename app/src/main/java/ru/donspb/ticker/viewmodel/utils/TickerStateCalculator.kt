package ru.donspb.ticker.viewmodel.utils

import ru.donspb.ticker.model.TickerState

class TickerStateCalculator (
    private val timestampProvider: TimestampProvider,
    private val elapsedTimeCalculator: ElapsedTimeCalculator
    ) {

    fun calculateRunningState(oldState: TickerState): TickerState.Running =
        when (oldState) {
            is TickerState.Running -> oldState
            is TickerState.Paused -> {
                TickerState.Running(
                    startTime = timestampProvider.getMilliseconds(),
                    elapsedTime = oldState.elapsedTime
                )
            }
        }

    fun calculatePausedState(oldState: TickerState): TickerState.Paused =
        when (oldState) {
            is TickerState.Running -> {
                val elapsedTime = elapsedTimeCalculator.calculate(oldState)
                TickerState.Paused(elapsedTime = elapsedTime)
            }
            is TickerState.Paused -> oldState
        }
}