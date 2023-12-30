package com.mhabzda.userlist

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mhabzda.userlist.ui.ListScreen

@Composable
fun UserListNavHost(
    navController: NavHostController = rememberNavController(),
    showSnackbar: suspend (String) -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = "list",
    ) {
        composable(route = "list") {
            ListScreen(showSnackbar = showSnackbar)
        }
    }
}
