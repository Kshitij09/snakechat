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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cc.snakechat.design.SnakeAsyncImage
import cc.snakechat.design.SnakeBackNavigationIcon
import cc.snakechat.design.SnakeChatTheme
import cc.snakechat.design.SnakeText
import cc.snakechat.domain.profile.PostThumbnail

@Composable
fun ProfileContent(state: ProfileState, modifier: Modifier = Modifier) {
    Scaffold(
        topBar = { TopBar(state.id, state.onBack) },
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
            is Error -> {
               Scaffold(
                   topBar = { TopBar(state.id, state.onBack) },
               ) {
                   Box(
                       modifier = Modifier
                           .padding(it)
                           .fillMaxSize(),
                       contentAlignment = Alignment.Center,
                   ) {
                       SnakeText(text = state.message, modifier = Modifier.padding(it))
                   }
               }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(userid: String, onBack: () -> Unit, modifier: Modifier = Modifier) {
    TopAppBar(
        title = { SnakeText(text = userid, style = MaterialTheme.typography.headlineSmall) },
        navigationIcon = {
            SnakeBackNavigationIcon(onBack = { onBack() })
        },
        modifier = modifier
    )
}

@Composable
fun ProfilePosts(
    thumbnails: List<PostThumbnail>,
    onPostClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val config = LocalConfiguration.current
    val itemSize = remember { config.screenWidthDp.dp / 3 }
    LazyVerticalGrid(columns = GridCells.Fixed(3), modifier = modifier) {
        items(thumbnails) {
            SnakeAsyncImage(
                url = it.mediaUrl,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clickable { onPostClick(it.id) }
                    .height(itemSize),
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
private fun ContentPreview() {
    SnakeChatTheme {
        ProfileContent(state = fakeDataState)
    }
}

@Preview
@Composable
private fun ErrorPreview() {
    SnakeChatTheme {
        ProfileContent(state = Error(fakeProfile.id,{},"Something went wrong"))
    }
}
