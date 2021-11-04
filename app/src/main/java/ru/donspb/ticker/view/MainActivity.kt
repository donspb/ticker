package ru.donspb.ticker.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import ru.donspb.ticker.*
import ru.donspb.ticker.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private val model = ViewModelProvider.NewInstanceFactory().create(MainViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val textView = findViewById<TextView>(R.id.text_time)

        model.liveData.observe(this, {
            textView.text = it
        })


//        CoroutineScope(Dispatchers.Main + SupervisorJob())
//            .launch {
//                tickerListOrchestrator.ticker.collect {
//                    textView.text = it
//                }
//            }

        findViewById<Button>(R.id.button_start).setOnClickListener {
            model.start()
        }
        findViewById<Button>(R.id.button_pause).setOnClickListener {
            model.pause()
        }
        findViewById<Button>(R.id.button_stop).setOnClickListener {
            model.stop()
        }
    }
}