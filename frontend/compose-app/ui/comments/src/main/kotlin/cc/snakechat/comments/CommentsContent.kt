package cc.snakechat.comments

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.InsertComment
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import cc.snakechat.design.SnakeAsyncImage
import cc.snakechat.design.SnakeBackNavigationIcon
import cc.snakechat.design.SnakeChatTheme
import cc.snakechat.design.SnakeText
import cc.snakechat.domain.post.comment.Comment
import cc.snakechat.domain.post.comment.Commenter
import cc.snakechat.ui.common.countString
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CommentsContent(state: CommentsState, modifier: Modifier = Modifier) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { SnakeText("Comments") },
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
                            val comment = pagingItems[index]
                            if (comment != null) {
                                CommentCard(
                                    comment = comment,
                                    onClick = { state.onRowClick(comment) },
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
private fun CommentsContentPreview() {
    val pagingItems = flowOf(
        PagingData.from(
            List(10) { fakeCommentOf(id = "comment_$it") },
        ),
    ).collectAsLazyPagingItems()
    val state = Data(pagingItems)
    SnakeChatTheme {
        CommentsContent(state = state)
    }
}

private val CommentProfileSize = 48.dp

@Composable
private fun CommentCard(
    comment: Comment,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    onFollow: (() -> Unit)? = null,
) {
    val commenter = comment.commenter
    Row(
        modifier = modifier
            .clickable(onClick = { onClick?.invoke() })
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SnakeAsyncImage(
            url = commenter.profileUrl,
            modifier = Modifier
                .size(CommentProfileSize)
                .clip(CircleShape)
                .align(Alignment.Top),
            loadingView = { ProfileLoadingView(modifier = Modifier.size(CommentProfileSize)) },
            fallbackView = { ProfileLoadingView(modifier = Modifier.size(CommentProfileSize)) },
            isDebug = true,
        )
        Spacer(Modifier.width(12.dp))
        Column {
            SnakeText(text = commentText(name = commenter.name, userId = commenter.id))
            SnakeText(text = comment.text)
            Row {
                CommentInteraction(
                    icon = Icons.Outlined.FavoriteBorder,
                    text = countString(comment.likes),
                )
                CommentInteraction(icon = Icons.AutoMirrored.Outlined.InsertComment)
            }
        }
        Spacer(
            Modifier
                .padding(start = 4.dp)
                .weight(1f),
        )
    }
}

@Composable
private fun commentText(name: String, userId: String): AnnotatedString {
    val nameColor = MaterialTheme.colorScheme.onSurface
    val nameStyle = MaterialTheme.typography.bodyMedium
        .toSpanStyle()
        .copy(color = nameColor)
    val userIdColor = MaterialTheme.colorScheme.secondary
    val userIdStyle = MaterialTheme.typography.labelMedium
        .toSpanStyle()
        .copy(color = userIdColor)
    return buildAnnotatedString {
        withStyle(nameStyle) { append(name) }
        append(" · ")
        withStyle(userIdStyle) { append(userId) }
    }
}

@Preview
@Composable
private fun LikerCardPreview() {
    SnakeChatTheme {
        Surface {
            CommentCard(comment = fakeCommentOf("1"))
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

@Composable
fun CommentInteraction(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    text: String? = null,
    contentDescription: String? = null,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable(
                onClick = { },
                role = Role.Button,
            )
            .minimumInteractiveComponentSize()
            .semantics {
                if (contentDescription != null) {
                    this.contentDescription = contentDescription
                }
            },
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Add Comment CTA",
        )
        Spacer(modifier = Modifier.width(4.dp))
        if (text != null) {
            SnakeText(
                text = text,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.secondary,
            )
        }
    }
}

private fun fakeCommentOf(id: String? = null) = Comment(
    commenter = Commenter(
        id = "1234",
        name = "Bert Gould",
    ),
    likes = 3611,
    id = id ?: "1234",
    text = "Sometimes I'll start a sentence and I don't even know where it's going. I just I find it along the way",
    updatedAt = LocalDateTime.now(),
)
