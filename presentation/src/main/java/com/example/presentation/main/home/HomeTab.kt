package com.example.presentation.main.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.domain.models.Image
import com.example.domain.models.ImageOrderType
import com.example.presentation.R
import com.example.presentation.common.theme.LightGray
import com.example.presentation.main.home.components.HomeError
import com.example.presentation.main.home.components.HomeLoading
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTab(
    openImagePreview: (Image) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val state = viewModel.state.collectAsState()
    val gridState = rememberLazyGridState()

    val isAtBottom = !gridState.canScrollForward

    BackHandler(state.value.isSearchMode) {
        viewModel.handleEvent(HomeEvent.DisableSearchMode)
    }

    LaunchedEffect(isAtBottom) {
        if (isAtBottom) viewModel.handleEvent(HomeEvent.LoadNextPage)
    }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val imageSize = screenWidth / 2

    val tabs =
        listOf(stringResource(id = R.string.tab_new), stringResource(id = R.string.tab_popular))
    val pullState = rememberPullToRefreshState()


    LaunchedEffect(key1 = "home_tab") {
        viewModel.handleEvent(HomeEvent.LoadNextPage)
        viewModel.effect.collectLatest {
            when (it) {
                is HomeEffect.OpenImagePreview -> openImagePreview(it.image)
            }
        }
    }

    Column(
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(LightGray)
                .zIndex(1F)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.padding(start = 16.dp)
            )
            TextField(
                value = state.value.searchQuery,
                onValueChange = {
                    viewModel.handleEvent(HomeEvent.SearchQueryChanged(it))
                },
                placeholder = {
                    Text(text = stringResource(id = R.string.hint_search))
                },
                maxLines = 1,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions {
                    viewModel.handleEvent(HomeEvent.Search)
                }
            )
        }
        if (state.value.isSearchMode.not()) {
            TabRow(
                selectedTabIndex = state.value.orderType.ordinal,
                containerColor = Color.White,
                modifier = Modifier.zIndex(1F)
            ) {
                tabs.forEachIndexed { index, name ->
                    Tab(
                        selected = index == state.value.orderType.ordinal,
                        selectedContentColor = Color.Black,
                        unselectedContentColor = Color.LightGray,
                        onClick = { viewModel.handleEvent(HomeEvent.ChangeOrderType(ImageOrderType.entries[index])) }
                    ) {
                        Text(
                            text = name,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }
            }
        }
        when {
            state.value.isLoading && state.value.images.isEmpty() -> {
                HomeLoading()
            }

            state.value.isError && state.value.images.isEmpty() -> {
                HomeError()
            }

            else -> {
                Box(
                    Modifier.nestedScroll(pullState.nestedScrollConnection),
                ) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        state = gridState
                    ) {
                        items(state.value.images, key = { it.id }) {
                            AsyncImage(
                                model = it.thumbnailUrl,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(imageSize.dp)
                                    .padding(8.dp)
                                    .clip(RoundedCornerShape(24.dp))
                                    .clickable { viewModel.handleEvent(HomeEvent.ImageClicked(it)) }
                            )
                        }
                    }
                    if (pullState.isRefreshing) {
                        LaunchedEffect(true) {
                            viewModel.handleEvent(HomeEvent.Refresh)
                        }
                    }
                    LaunchedEffect(state.value.isRefreshing) {
                        if (state.value.isRefreshing) {
                            pullState.startRefresh()
                        } else {
                            pullState.endRefresh()
                        }
                    }
                    if (state.value.isSearchMode.not()) {
                        PullToRefreshContainer(
                            state = pullState,
                            modifier = Modifier.align(Alignment.TopCenter)
                        )
                    }
                }
            }
        }
    }
}
