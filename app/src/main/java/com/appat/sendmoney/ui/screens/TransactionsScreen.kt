package com.appat.sendmoney.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.appat.sendmoney.data.entities.Transactions
import com.appat.sendmoney.ui.theme.dividerColor
import com.appat.sendmoney.ui.theme.price
import com.appat.sendmoney.ui.theme.primaryDark
import com.appat.sendmoney.ui.vewmodels.SendMoneyScreenViewModel
import com.appat.sendmoney.ui.vewmodels.TransactionsViewModel
import com.appat.sendmoney.utils.epochToIso8601

@Composable
fun TransactionsScreen(modifier: Modifier = Modifier, onItemClick: (Transactions) -> Unit) {

    val context = LocalContext.current
    val transactionsViewModel: TransactionsViewModel = hiltViewModel()
    val sendMoneyScreenViewModel: SendMoneyScreenViewModel = hiltViewModel()

    val transactionsUIState = transactionsViewModel.uiState.collectAsStateWithLifecycle()
    val sendMoneyUIState = sendMoneyScreenViewModel.uiState.collectAsStateWithLifecycle()

    val formData = sendMoneyUIState.value.formData

    LaunchedEffect(Unit) {
        transactionsViewModel.getAllTransactions()
        sendMoneyScreenViewModel.getFormData(context)
    }

    Column(modifier.fillMaxSize()) {
        LazyColumn {
            items(transactionsUIState.value.transactions) { transaction ->
                val fields = transactionsViewModel.getFieldsForTransaction(transactionId = transaction.id ?: 0).collectAsStateWithLifecycle(
                    listOf()
                )
                val service = formData?.services?.firstOrNull { it.name == transaction.service }
                val provider = service?.providers?.firstOrNull { it.id == transaction.provider }
                ListItem(
                    modifier = Modifier.clickable(onClick = {
                        onItemClick(transaction)
                    }),
                    headlineContent = {
                    Text(service?.label?.get() ?: "")
                }, overlineContent = {
                    Text(epochToIso8601(transaction.timeStamp))
                }, supportingContent = {
                        Text(provider?.name ?: "")
                }, colors = ListItemDefaults.colors(
                    headlineColor = MaterialTheme.colorScheme.onTertiary,
                    overlineColor = primaryDark,
                    supportingColor = MaterialTheme.colorScheme.onPrimary),
                    trailingContent = {
                        Column(modifier = Modifier.height(IntrinsicSize.Max)) {
                            Text(fields.value.firstOrNull {
                                it.fieldName == "amount"
                            }?.value ?: "", fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = price)
                        }
                    }, shadowElevation = 10.dp)
                HorizontalDivider(color = dividerColor,
                    modifier = Modifier.padding(horizontal = 10.dp))
            }
        }
    }
}