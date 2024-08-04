package cc.snakechat.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cc.snakechat.design.SnakeAsyncImage
import cc.snakechat.design.SnakeChatTheme
import cc.snakechat.design.SnakeText
import cc.snakechat.resources.strings

@Composable
fun ProfileHeader(state: Data, modifier: Modifier = Modifier) {
    val profile = state.profile
    Column(
        modifier = modifier
            .padding(horizontal = 12.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            SnakeAsyncImage(
                url = profile.profileUrl,
                loadingView = {
                    ProfileLoadingView()
                },
                fallbackView = {
                    ProfileLoadingView()
                },
            )
            Spacer(modifier = Modifier.width(20.dp))
            CountColumn(count = profile.postsCount, label = strings.posts, modifier = Modifier.weight(1f))
            CountColumn(
                count = profile.followersCount,
                label = strings.followers,
                onClick = { state.eventSink(OnFollowersClick) },
                modifier = Modifier.weight(1f),
            )
            CountColumn(
                count = profile.followingCount,
                label = strings.following,
                onClick = { state.eventSink(OnFollowingClick) },
                modifier = Modifier.weight(1f),
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        SnakeText(
            text = profile.username,
            style = MaterialTheme.typography.titleLarge,
        )
        val status = profile.status
        if (status != null) {
            SnakeText(
                text = status,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { state.eventSink(OnFollowClick) },
            shape = MaterialTheme.shapes.large,
            modifier = Modifier.fillMaxWidth(0.33f),
        ) {
            SnakeText(text = strings.follow)
        }
    }
}

@Composable
private fun ProfileLoadingView(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(96.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceDim,
                shape = CircleShape,
            ),
    )
}

@Composable
private fun CountColumn(
    count: Long,
    label: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier.clickable(enabled = onClick != null, onClick = { onClick?.invoke() }),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SnakeText(text = count.toString(), style = MaterialTheme.typography.titleMedium)
        SnakeText(text = label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondary)
    }
}

@Preview
@Composable
private fun ProfileHeaderPreview() {
    SnakeChatTheme {
        Surface {
            ProfileHeader(state = fakeDataState)
        }
    }
}
