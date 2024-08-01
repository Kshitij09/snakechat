package cc.snakechat.comments

import androidx.paging.compose.LazyPagingItems
import cc.snakechat.domain.post.comment.Comment
import com.slack.circuit.runtime.CircuitUiState

sealed interface CommentsState : CircuitUiState {
    val onBack: () -> Unit
}

class Loading(
    override val onBack: () -> Unit,
) : CommentsState

class Data(
    val pagingItems: LazyPagingItems<Comment>,
    val onRowClick: (Comment) -> Unit = {},
    override val onBack: () -> Unit = {},
) : CommentsState
