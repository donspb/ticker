package ru.donspb.ticker

import ru.donspb.ticker.model.TickerState

class TickerStateHolder(
    private val tickerStateCalculator: TickerStateCalculator,
    private val elapsedTimeCalculator: ElapsedTimeCalculator,
    private val timestampMsFormatter: TimestampMsFormatter
) {
    var currentState: TickerState = TickerState.Paused(0)
    private set

    fun start() {
        currentState = tickerStateCalculator.calculateRunningState(currentState)
    }

    fun pause() {
        currentState = tickerStateCalculator.calculatePausedState(currentState)
    }

    fun stop() {
        currentState = TickerState.Paused(0)
    }

    fun getStringTimeRepresentation(): String {
        val elapsedTime = when (val currentState = currentState) {
            is TickerState.Paused -> currentState.elapsedTime
            is TickerState.Running -> elapsedTimeCalculator.calculate(currentState)
        }
        return timestampMsFormatter.format(elapsedTime)
    }
}