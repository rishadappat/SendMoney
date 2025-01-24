package com.appat.sendmoney.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.appat.sendmoney.R
import com.appat.sendmoney.ui.customviews.DefaultErrorDialog
import com.appat.sendmoney.ui.customviews.SMButton
import com.appat.sendmoney.ui.customviews.SMTextField
import com.appat.sendmoney.ui.vewmodels.LoginScreenViewModel

@Composable
fun LoginScreen(modifier: Modifier = Modifier,
                onLoginSuccess: ()->Unit) {
    val loginScreenViewmodel: LoginScreenViewModel = viewModel()
    val uiState = loginScreenViewmodel.uiState.collectAsStateWithLifecycle()
    Column(modifier = modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
        .padding(20.dp),
        verticalArrangement = Arrangement.SpaceBetween) {
        Spacer(modifier = Modifier.height(0.dp))
        Column(modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(20.dp)) {
            Text(modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.app_name_upper),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center)
            Text(modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.welcome),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onTertiary,
                textAlign = TextAlign.Center)
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            SMTextField(modifier = Modifier.fillMaxWidth(),
                text = uiState.value.username, onValueChange = {
                    loginScreenViewmodel.setUsername(it)
                }, placeHolder = stringResource(R.string.email))
            SMTextField(modifier = Modifier.fillMaxWidth(),
                text = uiState.value.password, onValueChange = {
                    loginScreenViewmodel.setPassword(it)
                }, placeHolder = stringResource(R.string.password),
                isSecure = true)
        }
        SMButton(Modifier.fillMaxWidth(), title = R.string.sign_in) {
            val loginSuccess = loginScreenViewmodel.performLogin()
            if (loginSuccess)
                onLoginSuccess()
        }
        Text(text = stringResource(R.string.agreement),
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 20.dp),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onTertiary,
            textAlign = TextAlign.Center)
        if (uiState.value.errorMessage != -1) {
            DefaultErrorDialog(stringResource(uiState.value.errorMessage)) {
                loginScreenViewmodel.setErrorMessage(-1)
            }
        }
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    LoginScreen(){}
}