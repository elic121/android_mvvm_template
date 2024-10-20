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

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(binding.fragment1.id, BlankFragment())
                .replace(binding.fragment2.id, BlankFragment())
                .commit()
        }
        setUpListeners()
    }

    private fun setUpListeners() {
        binding.btnMove.setOnClickListener {
            toast("move to main")
            goToActivity(MainActivity::class.java, clearStack = true)
        }
    }
}