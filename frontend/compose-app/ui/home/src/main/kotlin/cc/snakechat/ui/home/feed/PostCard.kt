package cc.snakechat.ui.home.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.Whatsapp
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cc.snakechat.design.SnakeAsyncImage
import cc.snakechat.design.SnakeChatTheme
import cc.snakechat.design.SnakeText
import cc.snakechat.domain.feed.Post
import cc.snakechat.domain.feed.User
import cc.snakechat.ui.common.countString
import cc.snakechat.ui.common.formatDate
import cc.snakechat.ui.home.R
import java.time.LocalDateTime

@Composable
fun PostCard(
    post: Post,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    onLoadComplete: (() -> Unit)? = null,
    onLikeClick: (Post) -> Unit = {},
    onCommentClick: (Post) -> Unit = {},
    onProfileClick: (Post) -> Unit = {},
) {
    Surface(
        color = containerColor,
        modifier = Modifier.padding(3.dp),
        shape = MaterialTheme.shapes.medium,
    ) {
        Column(modifier = modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 12.dp),
            ) {
                SnakeAsyncImage(
                    url = post.user.profileUrl,
                    loadingView = {
                        ProfileLoading()
                    },
                    fallbackView = {
                        ProfileLoading()
                    },
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onProfileClick(post) },
                )
                SnakeText(
                    text = post.user.name,
                    fontWeight = FontWeight.Light,
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .clickable { onProfileClick(post) },
                )
            }

            val contentModifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 300.dp)
            SnakeAsyncImage(
                url = post.mediaUrl,
                contentDescription = "Post Content",
                contentScale = ContentScale.Crop,
                loadingView = { PostContentPlaceholder(contentModifier) },
                fallbackView = { PostContentPlaceholder(contentModifier) },
                onLoadComplete = { onLoadComplete?.invoke() },
                modifier = contentModifier.clip(MaterialTheme.shapes.small),
            )
            Box(modifier = Modifier.fillMaxWidth()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier,
                ) {
                    Interaction(
                        count = countString(post.likes),
                        icon = Icons.Outlined.FavoriteBorder,
                        onClick = { onLikeClick(post) },
                    )
                    Interaction(
                        count = countString(post.comments),
                        icon = Icons.AutoMirrored.Default.Message,
                        onClick = { onCommentClick(post) },
                    )
                    Interaction(
                        icon = Icons.Outlined.Download,
                    )
                }
                Interaction(
                    icon = Icons.Default.Whatsapp,
                    modifier = Modifier.align(Alignment.CenterEnd),
                )
            }
            Column(modifier = Modifier.padding(4.dp)) {
                post.caption?.let {
                    val text = buildAnnotatedString {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(post.user.name)
                        }
                        append(" ")
                        withStyle(SpanStyle(fontWeight = FontWeight.Normal)) { append(it) }
                    }
                    SnakeText(
                        text = text,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.clickable { onProfileClick(post) },
                    )
                }
                post.createdAt?.let {
                    SnakeText(
                        text = formatDate(it),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        modifier = Modifier.padding(top = 4.dp),
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileLoading(modifier: Modifier = Modifier) {
    Icon(
        painter = painterResource(id = R.drawable.ui_home_profile_placeholder),
        tint = MaterialTheme.colorScheme.secondary,
        contentDescription = "Profile Loading View",
        modifier = modifier.size(24.dp),
    )
}

@Composable
fun PostContentPlaceholder(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.small,
    color: Color = MaterialTheme.colorScheme.surfaceDim,
) {
    Box(
        modifier = modifier
            .padding(4.dp)
            .background(color, shape)
            .height(300.dp),
    )
}

@Composable
private fun Interaction(
    count: String? = null,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable(onClick = onClick)
            .minimumInteractiveComponentSize(),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
        )
        if (count != null) {
            SnakeText(
                text = count,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = 4.dp),
            )
        }
    }
}

@Preview
@Composable
private fun PostCardPreview() {
    SnakeChatTheme {
        Surface {
            PostCard(post = mockPost)
        }
    }
}

internal val mockUser = User(
    id = "user123",
    name = "John Doe",
)

internal val mockPost = Post(
    caption = "Caption",
    createdAt = LocalDateTime.now(),
    id = "1",
    mediaUrl = null,
    tagId = null,
    user = mockUser,
    comments = 4,
    likes = 1270,
)
