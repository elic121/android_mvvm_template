package com.example.template.view

import android.os.Bundle
import com.example.template.databinding.ActivitySubBinding
import com.example.template.util.goToActivity
import com.example.template.util.toast
import com.example.template.view.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubActivity : BaseActivity<ActivitySubBinding>(ActivitySubBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpListeners()
    }

    private fun setUpListeners() {
        binding.btnMove.setOnClickListener {
            toast("move to main")
            goToActivity(MainActivity::class.java, clearStack = true)
        }
    }
}