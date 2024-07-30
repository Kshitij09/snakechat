package cc.snakechat.ui.home.feed

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import cc.snakechat.domain.feed.Post

@Composable
fun FeedScreen(trendingFeed: LazyPagingItems<Post>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(
            count = trendingFeed.itemCount,
            key = trendingFeed.itemKey { it.id },
        ) { index ->
            val post = trendingFeed[index]
            if (post != null) {
                PostCard(post = post)
            }
        }
    }
}
