package com.mhabzda.userlist.ui

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.mhabzda.userlist.R
import com.mhabzda.userlist.domain.model.UserEntity
import com.mhabzda.userlist.theme.UserListTheme
import com.mhabzda.userlist.ui.ListContract.ListEffect
import com.mhabzda.userlist.ui.ListContract.ListViewState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@Composable
fun ListScreen(viewModel: ListViewModel = hiltViewModel()) {
    ListScreen(
        viewState = viewModel.state,
        effects = viewModel.effects,
        showSnackbar = viewModel::showSnackbar,
        onRefresh = viewModel::onRefresh,
    )
}

@Composable
fun ListScreen(
    viewState: StateFlow<ListViewState>,
    effects: SharedFlow<ListEffect>,
    showSnackbar: suspend (String) -> Unit,
    onRefresh: () -> Unit,
) {
    val state = viewState.collectAsState()

    LaunchedEffect(effects) {
        effects.collect {
            when (it) {
                ListEffect.ClearSearch -> Unit // TODO("implement)
                is ListEffect.DisplayError -> launch { showSnackbar(it.errorMessage) }
            }
        }
    }

    Scaffold(
        topBar = { ListTopAppBar() },
        containerColor = MaterialTheme.colorScheme.background,
        content = {
            Box(
                modifier = Modifier
                    .padding(top = it.calculateTopPadding()),
            ) {
                val users = state.value.users
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(count = users.size) { index ->
                        val item = users[index]
                        ItemView(item = item)
                        Divider()
                    }
                }
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ListTopAppBar() {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.app_name),
                color = MaterialTheme.colorScheme.onPrimary,
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun ItemView(item: UserEntity) {
    Row(
        modifier = Modifier
            .height(100.dp)
            .fillMaxWidth(),
    ) {
        GlideImage(
            model = item.avatarUrl,
            contentScale = ContentScale.FillHeight,
            contentDescription = null,
        )

        Column {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .weight(1f),
                text = item.name,
                style = MaterialTheme.typography.titleMedium,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .weight(1f),
                text = item.repositories.joinToString(),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview(uiMode = UI_MODE_NIGHT_NO)
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun ListScreenPreview() {
    UserListTheme {
        ListScreen(
            viewState = MutableStateFlow(
                ListViewState(
                    users = listOf(
                        UserEntity("john", "url", listOf("repo1", "jsonSerialize")),
                        UserEntity("micheal", "url", listOf("repo2")),
                        UserEntity("thomas", "url", listOf("repo3", "repoawesome")),
                        UserEntity("arthur", "url", listOf("repo4")),
                        UserEntity("alan13", "url", listOf("repo5", "repo321")),
                        UserEntity("dave", "url", listOf("repo7")),
                    ),
                ),
            ),
            effects = MutableSharedFlow(),
            showSnackbar = {},
            onRefresh = {},
        )
    }
}
