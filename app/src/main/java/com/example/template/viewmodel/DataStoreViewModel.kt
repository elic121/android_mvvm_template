package com.example.template.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.template.model.repository.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DataStoreViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    fun setExampleData(data: Int) {
        viewModelScope.launch {
            dataStoreRepository.setExampleData(data)
        }
    }

    fun getExampleData(): Flow<Int> {
        return dataStoreRepository.getExampleData()
    }

    fun setAccessToken(token: String) {
        viewModelScope.launch {
            dataStoreRepository.setAccessToken(token)
        }
    }

    fun getAccessToken(): Flow<String> {
        return dataStoreRepository.getAccessToken()
    }
}