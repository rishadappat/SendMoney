package com.appat.sendmoney.navigation

import androidx.annotation.StringRes
import com.appat.sendmoney.R

sealed class Screen(val route: String, @StringRes val title: Int) {
    data object Login: Screen("login_screen", R.string.sign_in)
    data object SendMoney: Screen("send_money_screen", R.string.send_money)
    data object Transactions: Screen("transactions", R.string.transactions)
    data object TransactionDetails: Screen("transaction_details", R.string.transactions)
}