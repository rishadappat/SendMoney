package com.appat.sendmoney.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.appat.sendmoney.R
import com.appat.sendmoney.data.entities.Transactions
import com.appat.sendmoney.data.models.Option
import com.appat.sendmoney.data.models.Type
import com.appat.sendmoney.ui.customviews.LookupField
import com.appat.sendmoney.ui.customviews.SMButton
import com.appat.sendmoney.ui.customviews.SMTextField
import com.appat.sendmoney.ui.vewmodels.SendMoneyScreenViewModel
import com.appat.sendmoney.ui.vewmodels.TransactionsViewModel
import com.appat.sendmoney.utils.getTimeStamp
import com.appat.sendmoney.utils.validate

@Composable
fun SendMoneyScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val viewmodel: SendMoneyScreenViewModel = hiltViewModel()
    val transactionsViewModel: TransactionsViewModel = hiltViewModel()
    val uiState = viewmodel.uiState.collectAsStateWithLifecycle()

    val providers by remember {
        derivedStateOf {
            uiState.value.selectedService?.providers ?: listOf()
        }
    }

    val fields by remember {
        derivedStateOf {
            uiState.value.selectedProvider?.requiredFields ?: listOf()
        }
    }

    LaunchedEffect(Unit) {
        viewmodel.getFormData(context)
    }
    Column(modifier = modifier.fillMaxSize()
        .padding(20.dp)
        .background(MaterialTheme.colorScheme.background)) {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()) {
            item {
                LookupField(
                    modifier = Modifier.animateItem(),
                    title = stringResource(R.string.services),
                    items = uiState.value.formData?.services ?: listOf(),
                    itemText = {
                        return@LookupField it.label?.get() ?: ""
                    },
                    didSelectItem = {
                        viewmodel.setSelectedService(it)
                        viewmodel.setSelectedProvider(null)
                    },
                    value = uiState.value.selectedService?.label?.get() ?: ""
                )
            }
            item {
                if (uiState.value.selectedService != null) {
                    LookupField(
                        modifier = Modifier.animateItem(),
                        title = stringResource(R.string.provider),
                        items = providers,
                        itemText = {
                            return@LookupField it.name ?: ""
                        },
                        didSelectItem = {
                            viewmodel.setSelectedProvider(it)
                        },
                        value = uiState.value.selectedProvider?.name ?: ""
                    )
                }
            }
            items(fields) { field ->
                val value = uiState.value.savedValues[field.name ?: ""]
                val displayText = when (value) {
                    is Option -> {
                        value.label ?: ""
                    }

                    is String? -> {
                        value ?: ""
                    }

                    else -> {
                        ""
                    }
                }
                if (field.type == Type.Option) {
                    LookupField(
                        modifier = Modifier.animateItem(),
                        title = field.label?.get() ?: "",
                        items = field.options ?: listOf(),
                        itemText = {
                            return@LookupField it.label ?: ""
                        },
                        didSelectItem = {
                            viewmodel.setSelectedValue(mapOf(Pair(field.name ?: "", it)))
                        },
                        value = displayText,
                        validate = uiState.value.performValidate,
                        field = field
                    )
                } else {
                    SMTextField(modifier = Modifier.fillMaxWidth()
                        .animateItem(),
                        text = displayText,
                        validate = uiState.value.performValidate,
                        onValueChange = {
                            viewmodel.setSelectedValue(mapOf(Pair(field.name ?: "", it)))
                            val isValid = it.validate(field.validation ?: "")
                            viewmodel.setFieldValidation(mapOf(Pair(field.name ?: "", isValid)))
                        },
                        field = field)
                }
            }
            item {
                if (uiState.value.savedValues.values.size == uiState.value.selectedProvider?.requiredFields?.size) {
                    Spacer(modifier = Modifier.height(40.dp))
                    SMButton(Modifier.fillMaxWidth(0.6f),
                        title = R.string.send_money) {
                        viewmodel.validate()
                        if (viewmodel.isAllValuesAreValid()) {
                            viewmodel.setSelectedService(null)
                            val transaction = Transactions(timeStamp = getTimeStamp(),
                                service = uiState.value.selectedService?.name ?: "",
                                provider = uiState.value.selectedProvider?.id ?: "")
                            transactionsViewModel.insertTransaction(transaction, uiState.value.savedValues)
                        }
                    }
                }

            }
        }
    }
}