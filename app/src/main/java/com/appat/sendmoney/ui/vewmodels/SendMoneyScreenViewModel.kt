package com.appat.sendmoney.ui.vewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appat.sendmoney.data.repository.SendMoneyRepository
import com.appat.sendmoney.data.models.FormData
import com.appat.sendmoney.data.models.Provider
import com.appat.sendmoney.data.models.Service
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SendMoneyScreenViewModel @Inject constructor(private val repository: SendMoneyRepository): ViewModel() {
    private val _uiState = MutableStateFlow(SendMoneyUIState())
    val uiState = _uiState.asStateFlow()

    fun getFormData(context: Context) = viewModelScope.launch {
        repository.getFormData(context).collect {
            setFormData(it)
        }
    }

    private fun setFormData(newFormData: FormData?) {
        _uiState.update { currentState ->
            currentState.copy(
                formData = newFormData
            )
        }
    }

    fun setSelectedService(newService: Service?) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedService = newService
            )
        }
        setSelectedProvider(null)
        resetSavedValues()
    }

    fun setSelectedProvider(newProvider: Provider?) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedProvider = newProvider
            )
        }
        resetSavedValues()
    }

    fun setSelectedValue(value: Map<String, Any>) {
        _uiState.update { currentState ->
            currentState.copy(
                savedValues = currentState.savedValues + value
            )
        }
        stopValidate()
    }

    private fun resetSavedValues() {
        _uiState.update { currentState ->
            currentState.copy(
                savedValues = mapOf(),
                allFieldsValidationMap = mapOf()
            )
        }
        stopValidate()
    }

    fun validate() {
        _uiState.update { currentState ->
            currentState.copy(
                performValidate = true
            )
        }
    }

    private fun stopValidate() {
        _uiState.update { currentState ->
            currentState.copy(
                performValidate = false
            )
        }
    }

    fun setFieldValidation(value: Map<String, Boolean>) {
        _uiState.update { currentState ->
            currentState.copy(
                allFieldsValidationMap = currentState.allFieldsValidationMap + value
            )
        }
    }

    fun isAllValuesAreValid(): Boolean {
        return _uiState.value.allFieldsValidationMap.isNotEmpty() &&
                _uiState.value.allFieldsValidationMap.filter {
                    it.value
                }.size == _uiState.value.allFieldsValidationMap.size
    }
}

data class SendMoneyUIState(
    val formData: FormData? = null,
    val selectedService: Service? = null,
    val selectedProvider: Provider? = null,
    val savedValues: Map<String, Any> = mapOf(),
    val performValidate: Boolean = false,
    val allFieldsValidationMap: Map<String, Boolean> = mapOf()
)