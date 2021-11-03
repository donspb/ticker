package ru.donspb.ticker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val textView = findViewById<TextView>(R.id.text_time)
        CoroutineScope(Dispatchers.Main + SupervisorJob())
            .launch {
                tickerListOrchestrator.ticker.collect {
                    textView.text = it
                }
            }

        findViewById<Button>(R.id.button_start).setOnClickListener {
            tickerListOrchestrator.start()
        }


        ViewModelProvider(this).get(MainViewModel::class.java).liveData.observe(
            this,
            { dataFromDataBase -> textView.text = dataFromDataBase.data }
        )
    }


    companion object {
        private const val TAG = "TICKER_TAG"
    }

    internal data class Data(val data: String)

    internal object DataBase {
        fun fetchData() = Random.nextInt()
    }

    internal class DataSource(
        private val dataBase: DataBase = DataBase,
        private val refreshIntervalMs: Long = 1000
    ) {
        val data: Flow<String> = flow {
            while (true) {
                val dataFromDataBase = dataBase.fetchData()
                emit(dataFromDataBase.toString())
                delay(refreshIntervalMs)
            }
        }
            .flowOn(Dispatchers.Default)
            .catch {
                e -> println(e.message)
            }
    }

    internal class Repository(dataSource: DataSource = DataSource()) {

        val userData: Flow<Data> = dataSource.data.map { data -> Data(data) }
    }

    internal class MainViewModel(repository: Repository = Repository()) : ViewModel() {
        val liveData: LiveData<Data> = repository.userData.asLiveData()
    }
}