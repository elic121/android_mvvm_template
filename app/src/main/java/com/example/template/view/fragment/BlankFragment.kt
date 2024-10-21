package com.example.template.view.fragment

import android.os.Bundle
import android.view.View
import com.example.template.R
import com.example.template.databinding.FragmentBlankBinding
import com.example.template.view.base.BaseFragment

class BlankFragment : BaseFragment<FragmentBlankBinding>() {
    private val TAG = BlankFragment::class.java.simpleName

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imgGoogle.setImageResource(R.drawable.ic_launcher_background)
    }
}
