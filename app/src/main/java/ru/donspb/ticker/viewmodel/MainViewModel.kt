package ru.donspb.ticker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import ru.donspb.ticker.repo.TickerListOrchestrator
import ru.donspb.ticker.viewmodel.utils.*

class MainViewModel() : ViewModel() {

    private val timestampProvider = object : TimestampProvider {
        override fun getMilliseconds(): Long {
            return System.currentTimeMillis()
        }
    }

    private val tickerListOrchestrator = TickerListOrchestrator(
        TickerStateHolder(
            TickerStateCalculator(
                timestampProvider,
                ElapsedTimeCalculator(timestampProvider)
            ),
            ElapsedTimeCalculator(timestampProvider),
            TimestampMsFormatter()
        ),
        CoroutineScope(
            Dispatchers.Main + SupervisorJob()
        )
    )

    val liveData: LiveData<String> = tickerListOrchestrator.ticker.asLiveData()

    fun start() { tickerListOrchestrator.start() }

    fun pause() { tickerListOrchestrator.pause() }

    fun stop() { tickerListOrchestrator.stop() }

}