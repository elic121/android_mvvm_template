package com.example.template.view

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.template.R
import com.example.template.databinding.ActivityMainBinding
import com.example.template.util.goToActivity
import com.example.template.util.toast
import com.example.template.view.base.BaseActivity
import com.example.template.viewmodel.DataStoreViewModel
import com.example.template.viewmodel.ExampleViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    private val TAG = MainActivity::class.java.simpleName
    private val exampleViewModel: ExampleViewModel by viewModels()
    private val dataStoreViewModel: DataStoreViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        exampleViewModel.getExampleData()
        exampleViewModel.example.observe(this) { example ->
            val displayText = buildString {
                append("X-Cloud-Trace-Context: ${example?.xCloudTraceContext ?: "null"}\n")
                append("Traceparent: ${example?.traceparent ?: "null"}\n")
                append("User-Agent: ${example?.userAgent ?: "null"}\n")
                append("Host: ${example?.host ?: "null"}\n")
            }
            binding.mainText.text = displayText
        }

        lifecycleScope.launch {
            val initialCount = dataStoreViewModel.getExampleData().first()
            Timber.d("Initial count: $initialCount")
            binding.textSetting.text = initialCount.toString()
        }

        binding.btn.setOnClickListener {
            lifecycleScope.launch {
                val currentCount = dataStoreViewModel.getExampleData().first()
                Timber.d("getExampleData (before increment): $currentCount")

                dataStoreViewModel.setExampleData(currentCount + 1)
                Timber.d("Setting new value: ${currentCount + 1}")

                binding.textSetting.text = (currentCount + 1).toString()
            }
        }

        binding.btnMove.setOnClickListener {
            toast("move to sub")
            goToActivity(SubActivity::class.java, clearStack = true)
        }
    }
}
