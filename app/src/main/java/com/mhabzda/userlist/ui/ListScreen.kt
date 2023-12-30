package com.mhabzda.userlist.ui

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.mhabzda.userlist.R
import com.mhabzda.userlist.domain.model.UserEntity
import com.mhabzda.userlist.theme.UserListTheme
import com.mhabzda.userlist.theme.marginDefault
import com.mhabzda.userlist.ui.ListContract.ListEffect
import com.mhabzda.userlist.ui.ListContract.ListViewState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@Composable
fun ListScreen(
    viewModel: ListViewModel = hiltViewModel(),
    showSnackbar: suspend (String) -> Unit,
) {
    ListScreen(
        viewState = viewModel.state,
        effects = viewModel.effects,
        showSnackbar = showSnackbar,
        onSearchTextChange = viewModel::onSearchTextChange,
    )
}

@Composable
private fun ListScreen(
    viewState: StateFlow<ListViewState>,
    effects: SharedFlow<ListEffect>,
    showSnackbar: suspend (String) -> Unit,
    onSearchTextChange: (String) -> Unit,
) {
    val state = viewState.collectAsState()

    LaunchedEffect(effects) {
        effects.collect {
            when (it) {
                is ListEffect.DisplayError -> launch { showSnackbar(it.errorMessage) }
            }
        }
    }

    Scaffold(
        topBar = { SearchAppBar(onSearchTextChange) },
        containerColor = MaterialTheme.colorScheme.background,
        content = {
            Box(
                modifier = Modifier.padding(top = it.calculateTopPadding()),
            ) {
                val users = state.value.users
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(count = users.size) { index ->
                        val item = users[index]
                        ItemView(item = item)
                        Divider()
                    }
                }

                if (state.value.isRefreshing) {
                    ListProgressIndicator(
                        modifier = Modifier.align(Alignment.TopCenter),
                    )
                }
            }
        },
    )
}

@Composable
private fun SearchAppBar(
    onSearchTextChange: (String) -> Unit,
) {
    val localFocusManager = LocalFocusManager.current
    var searchText by remember { mutableStateOf("") }
    var hasFocus by remember { mutableStateOf(false) }

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { hasFocus = it.hasFocus },
        value = searchText,
        onValueChange = {
            searchText = it
            onSearchTextChange(it)
        },
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
        trailingIcon = {
            if (hasFocus) {
                IconButton(
                    onClick = {
                        searchText = ""
                        onSearchTextChange("")
                        localFocusManager.clearFocus()
                    },
                    content = { Icon(Icons.Filled.Clear, contentDescription = null) },
                )
            }
        },
        label = { Text(stringResource(id = R.string.list_search_hint)) },
    )
}

@Composable
private fun ItemView(item: UserEntity) {
    Row(
        modifier = Modifier
            .height(100.dp)
            .fillMaxWidth(),
    ) {
        AsyncImage(
            modifier = Modifier.size(100.dp),
            model = item.avatarUrl,
            contentDescription = null,
            placeholder = rememberVectorPainter(image = Icons.Filled.Person),
            error = rememberVectorPainter(image = Icons.Filled.Person),
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

@Composable
private fun ListProgressIndicator(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .padding(marginDefault)
            .size(48.dp)
            .clip(CircleShape)
            .background(Color.LightGray),
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.Center),
            strokeWidth = 3.dp,
        )
    }
}

@Preview(uiMode = UI_MODE_NIGHT_NO)
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun ListScreenPreview() {
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
            onSearchTextChange = {},
        )
    }
}
