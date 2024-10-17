package com.example.template.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.template.model.entity.Example
import com.example.template.model.repository.ExampleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExampleViewModel @Inject constructor(
    private val exampleRepository: ExampleRepository
): ViewModel() {
    private val _example = MutableLiveData<Example>()
    val example: LiveData<Example> = _example

    fun getExampleData() {
        viewModelScope.launch {
            val example = exampleRepository.getExampleData()
            _example.value = example
        }
    }
}