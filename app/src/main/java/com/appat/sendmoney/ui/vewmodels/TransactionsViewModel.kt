package com.appat.sendmoney.ui.vewmodels

import android.graphics.Path.Op
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appat.sendmoney.data.entities.FieldValues
import com.appat.sendmoney.data.entities.Transactions
import com.appat.sendmoney.data.models.Option
import com.appat.sendmoney.data.repository.FieldValuesRepository
import com.appat.sendmoney.data.repository.TransactionsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val transactionsRepository: TransactionsRepository,
    private val fieldValuesRepository: FieldValuesRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(TransactionsUIState())
    val uiState = _uiState.asStateFlow()

    fun insertTransaction(transaction: Transactions, savedValues: Map<String, Any>) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            val transactionId = transactionsRepository.insertTransaction(transaction)
            val fields = arrayListOf<FieldValues>()
            savedValues.forEach {
                when (it.value) {
                    is String -> {
                        fields.add(FieldValues(transactionId = transactionId, fieldName = it.key, value = it.value as String))
                    }
                    is Option -> {
                        fields.add(FieldValues(transactionId = transactionId, fieldName = it.key, value = (it.value as Option).name ?: ""))
                    }
                    else -> {
                        null
                    }
                }
            }
            fieldValuesRepository.insertAll(fields)
        }
    }

    fun getAllTransactions() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            transactionsRepository.getAllTransaction().collect {
                setTransactions(it)
            }
        }
    }

    private fun setTransactions(transactions: List<Transactions>) {
        _uiState.update { currentState->
            currentState.copy(
                transactions = transactions.sortedByDescending { it.timeStamp }
            )
        }
    }

    fun getFieldsForTransaction(transactionId: Long): Flow<List<FieldValues>>  {
        return fieldValuesRepository.getFieldValues(transactionId)
    }
}

data class TransactionsUIState(
    val transactions: List<Transactions> = listOf()
)