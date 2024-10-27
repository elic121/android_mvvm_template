package com.example.template.view.activity

import android.os.Bundle
import com.example.template.databinding.ActivitySub2Binding
import com.example.template.util.goToActivity
import com.example.template.view.base.BaseActivity

class Sub2Activity : BaseActivity<ActivitySub2Binding>(ActivitySub2Binding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btnRegister.setOnClickListener {
            goToActivity(LoginActivity::class.java)
        }
    }
}
