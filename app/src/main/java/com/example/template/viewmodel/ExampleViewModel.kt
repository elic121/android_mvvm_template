package com.example.template.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.template.model.entity.ExampleEntity
import com.example.template.model.entity.ResultState
import com.example.template.model.repository.ExampleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ExampleViewModel @Inject constructor(
    private val exampleRepository: ExampleRepository
): ViewModel() {
    private val _exampleEntity = MutableLiveData<ResultState<ExampleEntity>>()
    val exampleEntity: LiveData<ResultState<ExampleEntity>> = _exampleEntity

    fun getExampleData() {
        viewModelScope.launch {
            _exampleEntity.value = ResultState.Loading

            val result = exampleRepository.getExampleData()
            _exampleEntity.value = if (result.isSuccess) {
                ResultState.Success(result.getOrNull()!!)
            } else {
                ResultState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }
}