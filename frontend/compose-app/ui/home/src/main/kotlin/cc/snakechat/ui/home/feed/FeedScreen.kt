package cc.snakechat.ui.home.feed

import androidx.activity.compose.ReportDrawnWhen
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.paging.compose.itemKey
import cc.snakechat.ui.home.Data
import cc.snakechat.ui.home.OnCommentClicked
import cc.snakechat.ui.home.OnLikeClicked
import cc.snakechat.ui.home.OnProfileClicked

@Composable
fun FeedScreen(
    state: Data,
    modifier: Modifier = Modifier,
) {
    val trendingFeed = state.feed
    var isFirstPostLoaded by remember { mutableStateOf(false) }
    ReportDrawnWhen { isFirstPostLoaded }
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
    ) {
        LazyColumn {
            items(
                count = trendingFeed.itemCount,
                key = trendingFeed.itemKey { it.id },
            ) { index ->
                val post = trendingFeed[index]
                if (post != null) {
                    PostCard(
                        post = post,
                        onLoadComplete = { isFirstPostLoaded = true },
                        onLikeClick = { state.eventSink(OnLikeClicked(it)) },
                        onCommentClick = { state.eventSink(OnCommentClicked(it)) },
                        onProfileClick = { state.eventSink(OnProfileClicked(it)) },
                    )
                }
            }
        }
    }
}
