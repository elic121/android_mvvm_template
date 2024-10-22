package com.example.template.view.activity

import android.Manifest
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.template.databinding.ActivityMainBinding
import com.example.template.util.goToActivity
import com.example.template.util.toast
import com.example.template.view.base.BaseActivity
import com.example.template.viewmodel.DataStoreViewModel
import com.example.template.viewmodel.ExampleViewModel
import com.gun0912.tedpermission.coroutine.TedPermission
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    private val TAG = MainActivity::class.java.simpleName
    private val exampleViewModel: ExampleViewModel by viewModels()
    private val dataStoreViewModel: DataStoreViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setUpExampleData()
        setUpCount()
        increaseCount()
        moveToSub()
        moveToSub2()
    }

    private fun setUpExampleData() {
        exampleViewModel.getExampleData()
        exampleViewModel.exampleEntity.observe(this) { example ->
            val displayText = buildString {
                append("X-Cloud-Trace-Context: ${example?.xCloudTraceContext ?: "null"}\n")
                append("Traceparent: ${example?.traceparent ?: "null"}\n")
                append("User-Agent: ${example?.userAgent ?: "null"}\n")
                append("Host: ${example?.host ?: "null"}\n")
            }
            binding.mainText.text = displayText
        }
    }

    private fun setUpCount() {
        lifecycleScope.launch {
            val initialCount = dataStoreViewModel.getExampleData().first()
            binding.textSetting.text = initialCount.toString()
        }
    }

    private fun increaseCount() {
        binding.btn.setOnClickListener {
            lifecycleScope.launch {
                var currentCount = dataStoreViewModel.getExampleData().first()
                ++currentCount
                dataStoreViewModel.setExampleData(currentCount)
                binding.textSetting.text = currentCount.toString()
            }
        }
    }

    private fun moveToSub() {
        binding.btnMove.setOnClickListener {
            toast("move to sub")
            goToActivity(SubActivity::class.java, clearStack = true)
        }
    }

    private fun moveToSub2() {
        binding.btnMoveQr.setOnClickListener {
            lifecycleScope.launch {
                getPermission()
            }
        }
    }

    private suspend fun getPermission() {
        try {
            val permissionResult = TedPermission.create()
                .setPermissions(Manifest.permission.CAMERA)
                .setDeniedMessage("권한을 허용하지 않으면 QR 스캔을 할 수 없습니다.\n\n[설정] > [권한]")
                .check()

            if (permissionResult.isGranted) {
                goToActivity(Sub2Activity::class.java)
            } else {
                toast("Permission Denied")
            }
        } catch (e: Exception) {
            toast("error: $e")
        }
    }
}
