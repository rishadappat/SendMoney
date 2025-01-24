package com.appat.sendmoney.ui.customviews

import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.LocaleListCompat
import com.appat.sendmoney.R
import com.appat.sendmoney.data.models.RequiredField
import com.appat.sendmoney.data.models.Type
import com.appat.sendmoney.ui.theme.tertiaryText
import com.appat.sendmoney.utils.changeLanguage
import com.appat.sendmoney.utils.validate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SMTopAppBar(@StringRes title: Int) {
    val context = LocalContext.current
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onSecondary
        ),
        title = {
            Text(stringResource(title))
        },
        actions = {
            Icon(painter = painterResource(R.drawable.switch_language),
                modifier = Modifier.padding(horizontal = 10.dp).clickable(onClick = {
                    changeLanguage(context)
                }), contentDescription = "")
        }
    )
}

@Composable
fun SMTextField(modifier: Modifier = Modifier,
                text: String,
                isSecure: Boolean = false,
                charLimit: Int = 0,
                placeHolder: String = "",
                field: RequiredField? = null,
                validate: Boolean = false,
                onValueChange: (String)->Unit) {

    var isError by rememberSaveable { mutableStateOf(false) }

    val keyboardType by remember { derivedStateOf {
        when(field?.type) {
            Type.Msisdn -> { KeyboardType.Phone }
            Type.Number -> { KeyboardType.Number }
            Type.Option -> { KeyboardType.Unspecified }
            Type.Text -> { KeyboardType.Text }
            null -> { KeyboardType.Text }
        }
    } }

    val supportingText: @Composable (() -> Unit)? = if (isError && validate && !field?.validationErrorMessage?.get().isNullOrEmpty()) {
        {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = field?.validationErrorMessage?.get() ?: "",
                color = MaterialTheme.colorScheme.error
            )
        }
    } else null

    Column(modifier = modifier) {
        Text(text = field?.label?.get() ?: "", fontSize = 16.sp,
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(5.dp))
        TextField(
            modifier = modifier.fillMaxWidth(),
            value = text,
            onValueChange = {
                if (charLimit == 0 || it.length <= charLimit) {
                    onValueChange(it)
                    isError = !it.validate(field?.validation ?: "")
                }
            },
            placeholder = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = field?.placeholder?.get() ?: placeHolder,
                    color = tertiaryText
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            visualTransformation = if (isSecure)
                PasswordVisualTransformation()
            else
                VisualTransformation.None,
            singleLine = true,
            isError = isError && validate,
            supportingText = supportingText,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.tertiary,
                focusedContainerColor = MaterialTheme.colorScheme.tertiary,
                unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
                focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorContainerColor = MaterialTheme.colorScheme.tertiary,
                errorTextColor = MaterialTheme.colorScheme.onPrimary,
            )
        )
    }
}

@Composable
fun SMButton(modifier: Modifier = Modifier,
             @StringRes title: Int,
             onClick: ()->Unit) {
    ElevatedButton(
        modifier = modifier
            .height(50.dp),
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onSecondary
        ),
        onClick = onClick) {
        Text(stringResource(title))
    }
}

@Composable
fun DefaultErrorDialog(message: String,
                       onDismissRequest: ()->Unit) {
    AlertDialog(
        containerColor = MaterialTheme.colorScheme.background,
        titleContentColor = MaterialTheme.colorScheme.onPrimary,
        textContentColor = MaterialTheme.colorScheme.onPrimary,
        onDismissRequest = onDismissRequest,
        title = { Text(text = stringResource(R.string.error)) },
        text = { Text(text = message) },
        confirmButton = {

        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(R.string.dismiss))
            }
        }
    )
}

@Composable
fun <T>LookupField(modifier: Modifier = Modifier,
                   title: String,
                   field: RequiredField? = null,
                   items: List<T> = listOf(),
                   itemText: (T) -> String,
                   subText: (T) -> String = {""},
                   value: String = "",
                   enabled: Boolean = true,
                   validate: Boolean = true,
                   didSelectItem: (T)->Unit) {
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    Column(modifier = modifier) {
        Text(text = title, fontSize = 16.sp,
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(5.dp))
        Row(modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(color = Color.White, shape = RoundedCornerShape(5.dp))
            .border(width = 1.dp, color = MaterialTheme.colorScheme.onTertiary,
                shape = RoundedCornerShape(5.dp))
            .clickable(enabled = enabled, onClick = {
                openBottomSheet = true
            })
            .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {
            Text(value.ifEmpty { title },
                style = MaterialTheme.typography.bodyLarge,
                color = if(value.isEmpty())
                    MaterialTheme.colorScheme.onTertiary
                else
                    MaterialTheme.colorScheme.onPrimary)
            Icon(Icons.Default.ArrowDropDown, title, tint = MaterialTheme.colorScheme.primary)
        }
        if (validate && !field?.validationErrorMessage?.get().isNullOrEmpty()) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = field?.validationErrorMessage?.get() ?: "",
                color = MaterialTheme.colorScheme.error
            )
        }
    }

    SingleSelectionBottomSheet(openBottomSheet = openBottomSheet,
        items = items,
        title = title,
        didSelectItem = didSelectItem,
        itemText = itemText,
        subText = subText) {
        openBottomSheet = false
    }
}
