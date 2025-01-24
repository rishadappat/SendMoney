package com.appat.sendmoney.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.appat.sendmoney.data.entities.FieldValues
import com.appat.sendmoney.data.entities.Transactions
import com.appat.sendmoney.ui.vewmodels.SendMoneyScreenViewModel
import com.appat.sendmoney.ui.vewmodels.TransactionsViewModel
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
fun TransactionDetailsScreen(modifier: Modifier = Modifier, transaction: Transactions) {

    val context = LocalContext.current
    val transactionsViewModel: TransactionsViewModel = hiltViewModel()
    val sendMoneyScreenViewModel: SendMoneyScreenViewModel = hiltViewModel()

    val fields = transactionsViewModel.getFieldsForTransaction(transactionId = transaction.id ?: 0).collectAsStateWithLifecycle(
        listOf()
    )

    var displayJson by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        transactionsViewModel.getAllTransactions()
        sendMoneyScreenViewModel.getFormData(context)
    }

    Column(modifier = modifier
        .verticalScroll(rememberScrollState())) {
        val transactionDetails = TransactionDetailsModel(transaction, fields.value)
        displayJson = Json.encodeToString(transactionDetails)
        Text(displayJson, color = MaterialTheme.colorScheme.onPrimary)
    }
}

@Serializable
data class TransactionDetailsModel(
    val transaction: Transactions? = null,
    val fields: List<FieldValues> = listOf()
)