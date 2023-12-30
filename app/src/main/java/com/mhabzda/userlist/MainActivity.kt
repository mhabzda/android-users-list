package com.mhabzda.userlist

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import com.mhabzda.userlist.theme.UserListTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            UserListTheme {
                val snackbarHostState = remember { SnackbarHostState() }

                Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) {
                    UserListNavHost(
                        showSnackbar = { snackbarHostState.showSnackbar(it) },
                    )
                }
            }
        }
    }
}
