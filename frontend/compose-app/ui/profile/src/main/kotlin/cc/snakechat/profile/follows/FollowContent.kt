package cc.snakechat.profile.follows

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import cc.snakechat.design.SnakeAsyncImage
import cc.snakechat.design.SnakeBackNavigationIcon
import cc.snakechat.design.SnakeChatTheme
import cc.snakechat.design.SnakeText
import cc.snakechat.domain.model.common.FollowListType
import cc.snakechat.domain.profile.Follow
import cc.snakechat.resources.strings
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FollowContent(state: FollowListState, modifier: Modifier = Modifier) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    SnakeText(if (state.listType == FollowListType.Following) strings.following else strings.followers)
                },
                navigationIcon = {
                    SnakeBackNavigationIcon(onBack = { state.onBack() })
                },
            )
        },
    ) {
        Surface(modifier = modifier.padding(it)) {
            when (state) {
                is Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        SnakeText(text = "Loading")
                    }
                }
                is Data -> {
                    val pagingItems = state.pagingItems
                    LazyColumn(modifier = modifier.fillMaxSize()) {
                        items(
                            count = pagingItems.itemCount,
                            key = pagingItems.itemKey { it.id },
                        ) { index ->
                            val liker = pagingItems[index]
                            if (liker != null) {
                                FollowListCard(
                                    follow = liker,
                                    onClick = { state.onRowClick(liker) },
                                    onFollow = { },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun LikersContentPreview() {
    val pagingItems = flowOf(
        PagingData.from(
            List(10) { fakerFollowOf(id = "follow_list_$it") },
        ),
    ).collectAsLazyPagingItems()
    val state = Data(
        listType = FollowListType.Following,
        pagingItems = pagingItems,
    )
    SnakeChatTheme {
        FollowContent(state = state)
    }
}

private val ProfileSize = 48.dp

@Composable
private fun FollowListCard(
    follow: Follow,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    onFollow: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .clickable(onClick = { onClick?.invoke() })
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SnakeAsyncImage(
            url = follow.profileUrl,
            modifier = Modifier
                .size(ProfileSize)
                .clip(CircleShape),
            loadingView = { ProfileLoadingView(modifier = Modifier.size(ProfileSize)) },
            fallbackView = { ProfileLoadingView(modifier = Modifier.size(ProfileSize)) },
        )
        Spacer(Modifier.width(12.dp))
        Column {
            SnakeText(
                text = follow.name,
                style = MaterialTheme.typography.titleMedium,
            )
            SnakeText(
                text = follow.id,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        Spacer(
            Modifier
                .padding(start = 4.dp)
                .weight(1f),
        )
        val backgroundColor = MaterialTheme.colorScheme.primary
        Button(
            onClick = { onFollow?.invoke() },
            shape = MaterialTheme.shapes.large,
            colors = ButtonDefaults.buttonColors(
                containerColor = backgroundColor,
                contentColor = contentColorFor(backgroundColor = backgroundColor),
            ),
        ) {
            SnakeText(text = strings.follow)
        }
    }
}

@Preview
@Composable
private fun FollowListCardPreview() {
    SnakeChatTheme {
        Surface {
            FollowListCard(follow = fakeLiker)
        }
    }
}

@Composable
private fun ProfileLoadingView(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.background(
            color = MaterialTheme.colorScheme.surfaceDim,
            shape = CircleShape,
        ),
    )
}

private val fakeLiker = Follow(
    id = "john_doe",
    name = "John Doe",
    profileUrl = null,
    updatedAt = LocalDateTime.now(),
)

private fun fakerFollowOf(id: String? = null) = Follow(
    id = id ?: "voluptatibus",
    name = "Bert Gould",
    profileUrl = null,
    updatedAt = LocalDateTime.now(),
)
