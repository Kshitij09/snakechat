package cc.snakechat.comments

import androidx.paging.compose.LazyPagingItems
import cc.snakechat.domain.post.comment.Comment
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

sealed interface CommentsState : CircuitUiState {
    val onBack: () -> Unit
}

class Loading(
    override val onBack: () -> Unit,
) : CommentsState

class Data(
    val pagingItems: LazyPagingItems<Comment>,
    override val onBack: () -> Unit = {},
    val eventSink: (CommentEvent) -> Unit = {},
) : CommentsState

sealed interface CommentEvent : CircuitUiEvent

class OnProfileClick(val comment: Comment) : CommentEvent
class OnLikeCountClick(val comment: Comment) : CommentEvent