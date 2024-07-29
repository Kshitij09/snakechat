package cc.snakechat.ui.home.feed

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cc.snakechat.domain.feed.TrendingFeed

@Composable
fun FeedScreen(trendingFeed: TrendingFeed, modifier: Modifier = Modifier) {
   LazyColumn(modifier = modifier) {
       items(trendingFeed.posts) { post ->
           PostCard(post = post)
       }
   }
}