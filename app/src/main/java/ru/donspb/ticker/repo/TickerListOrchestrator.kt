package ru.donspb.ticker.repo

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.donspb.ticker.viewmodel.utils.TickerStateHolder

class TickerListOrchestrator(
    private val tickerStateHolder: TickerStateHolder,
    private val scope: CoroutineScope
) {
    private var job: Job? = null
    private val mutableTicker = MutableStateFlow(DEFAULT_TIME_STRING)
    val ticker: StateFlow<String> = mutableTicker

    fun start() {
        if (job == null) startJob()
        tickerStateHolder.start()
    }

    private fun startJob() {
        scope.launch {
            while (isActive) {
                mutableTicker.value =
                    tickerStateHolder.getStringTimeRepresentation()
                delay(20)
            }
        }
    }

    fun pause() {
        tickerStateHolder.pause()
        stopJob()
    }

    fun stop() {
        tickerStateHolder.stop()
        stopJob()
        clearValue()
    }

    private fun stopJob() {
        scope.coroutineContext.cancelChildren()
        job = null
    }

    private fun clearValue() {
        mutableTicker.value = DEFAULT_TIME_STRING
    }

    companion object {
        private const val DEFAULT_TIME_STRING = "00:00:000"
    }
}