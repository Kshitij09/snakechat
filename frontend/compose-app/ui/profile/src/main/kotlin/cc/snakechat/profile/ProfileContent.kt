package cc.snakechat.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cc.snakechat.design.SnakeAsyncImage
import cc.snakechat.design.SnakeBackNavigationIcon
import cc.snakechat.design.SnakeChatTheme
import cc.snakechat.design.SnakeText
import cc.snakechat.domain.profile.PostThumbnail

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContent(state: ProfileState, modifier: Modifier = Modifier) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { SnakeText(text = state.id, style = MaterialTheme.typography.headlineSmall) },
                navigationIcon = {
                    SnakeBackNavigationIcon(onBack = { state.onBack() })
                },
            )
        },
        modifier = modifier,
    ) { contentPadding ->
        when (state) {
            is Data -> {
                Column(modifier = Modifier.padding(contentPadding)) {
                    ProfileHeader(state = state)
                    Spacer(modifier = Modifier.height(24.dp))
                    ProfilePosts(
                        thumbnails = state.profile.postThumbnails,
                        onPostClick = { state.eventSink(OnPostClick(it)) },
                    )
                }
            }
            is Loading -> {
                Box(
                    modifier = Modifier
                        .padding(contentPadding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    SnakeText(text = "Loading")
                }
            }
        }
    }
}

@Composable
fun ProfilePosts(
    thumbnails: List<PostThumbnail>,
    onPostClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(columns = GridCells.Fixed(3), modifier = modifier) {
        items(thumbnails) {
            SnakeAsyncImage(
                url = it.mediaUrl,
                modifier = Modifier.clickable { onPostClick(it.id) }.height(100.dp),
                fallbackView = { ThumbnailPlaceholder() },
                loadingView = { ThumbnailPlaceholder() },
            )
        }
    }
}

@Composable
fun ThumbnailPlaceholder(modifier: Modifier = Modifier) {
    Box(modifier = modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.surfaceDim)
    )
}


@Preview
@Composable
private fun ProfileContentPreview() {
    SnakeChatTheme {
        ProfileContent(state = fakeDataState)
    }
}