package com.mhabzda.userlist

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.mhabzda.userlist.theme.UserListTheme
import com.mhabzda.userlist.utils.SnackbarFlow
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var snackbarFlow: SnackbarFlow

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            UserListTheme {
                val snackbarHostState = remember { SnackbarHostState() }

                LaunchedEffect(snackbarFlow) {
                    snackbarFlow.flow.collectLatest { snackbarHostState.showSnackbar(it) }
                }

                Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) {
                    UserListNavHost()
                }
            }
        }
    }
}
