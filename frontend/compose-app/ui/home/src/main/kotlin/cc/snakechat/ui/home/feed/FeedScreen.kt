package cc.snakechat.ui.home.feed

import androidx.activity.compose.ReportDrawnWhen
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import cc.snakechat.domain.feed.Post

@Composable
fun FeedScreen(trendingFeed: LazyPagingItems<Post>, modifier: Modifier = Modifier) {
    var isFirstPostLoaded by remember { mutableStateOf(false) }
    ReportDrawnWhen { isFirstPostLoaded }
    LazyColumn(modifier = modifier) {
        items(
            count = trendingFeed.itemCount,
            key = trendingFeed.itemKey { it.id },
        ) { index ->
            val post = trendingFeed[index]
            if (post != null) {
                PostCard(
                    post = post,
                    onLoadComplete = { isFirstPostLoaded = true },
                )
            }
        }
    }
}
