package com.appat.sendmoney.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.appat.sendmoney.R
import com.appat.sendmoney.data.entities.Transactions
import com.appat.sendmoney.ui.customviews.SMTopAppBar
import com.appat.sendmoney.ui.screens.LoginScreen
import com.appat.sendmoney.ui.screens.SendMoneyScreen
import com.appat.sendmoney.ui.screens.TransactionDetailsScreen
import com.appat.sendmoney.ui.screens.TransactionsScreen
import com.appat.sendmoney.ui.theme.primaryDark

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SMNavigation(modifier: Modifier) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    var title by rememberSaveable {
        mutableIntStateOf(R.string.sign_in)
    }
    Scaffold(modifier.fillMaxSize(),
        topBar = {
            SMTopAppBar(title)
        },
        floatingActionButton = {
            if(currentRoute == Screen.SendMoney.route) {
                FloatingActionButton(
                    containerColor = primaryDark,
                    onClick = {
                    navController.navigate(Screen.Transactions.route)
                }) {
                    Icon(imageVector = Icons.Default.Menu, "Transactions")
                }
            }
        }
    ) {
        NavHost(
            modifier = modifier.padding(it),
            navController = navController,
            startDestination = Screen.Login.route
        ) {
            slideComposable(route = Screen.Login.route) {
                title = Screen.Login.title
                LoginScreen {
                    navController.navigate(Screen.SendMoney.route) {
                        popUpTo(Screen.Login.route) {
                            inclusive = true
                        }
                    }
                }
            }
            slideComposable(route = Screen.SendMoney.route) {
                title = Screen.SendMoney.title
                SendMoneyScreen()
            }
            slideComposable(route = Screen.Transactions.route) {
                title = Screen.Transactions.title
                TransactionsScreen {
                    navController.navigate(it)
                }
            }
            composable<Transactions> { backStackEntry ->
                val transactions: Transactions = backStackEntry.toRoute()
                TransactionDetailsScreen(modifier = Modifier, transactions)
            }
        }
    }
}