package com.example.template.view

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.template.R
import com.example.template.viewmodel.DataStoreViewModel
import com.example.template.viewmodel.ExampleViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val exampleViewModel: ExampleViewModel by viewModels()
    private val dataStoreViewModel: DataStoreViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        exampleViewModel.getExampleData()
        exampleViewModel.example.observe(this) { example ->
            val displayText = buildString {
                append("X-Cloud-Trace-Context: ${example?.xCloudTraceContext ?: "null"}\n")
                append("Accept: ${example?.accept ?: "null"}\n")
                append("Upgrade-Insecure-Requests: ${example?.upgradeInsecureRequests ?: "null"}\n")
                append("Traceparent: ${example?.traceparent ?: "null"}\n")
                append("User-Agent: ${example?.userAgent ?: "null"}\n")
                append("Host: ${example?.host ?: "null"}\n")
                append("Accept-Language: ${example?.acceptLanguage ?: "null"}\n")
            }
            findViewById<TextView>(R.id.main_text).text = displayText
        }

        lifecycleScope.launch {
            val initialCount = dataStoreViewModel.getExampleData().first()
            Timber.d("Initial count: $initialCount")
            findViewById<TextView>(R.id.text_setting).text = initialCount.toString()
        }

        val button = findViewById<TextView>(R.id.btn)
        button.setOnClickListener {
            lifecycleScope.launch {
                val currentCount = dataStoreViewModel.getExampleData().first()
                Timber.d("getExampleData (before increment): $currentCount")

                dataStoreViewModel.setExampleData(currentCount + 1)
                Timber.d("Setting new value: ${currentCount + 1}")

                val updatedCount = dataStoreViewModel.getExampleData().first()
                Timber.d("getExampleData (after increment): $updatedCount")
                findViewById<TextView>(R.id.text_setting).text = updatedCount.toString()
            }
        }
    }
}
