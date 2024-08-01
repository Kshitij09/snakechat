package cc.snakechat.likers

import androidx.paging.compose.LazyPagingItems
import cc.snakechat.domain.post.Liker
import com.slack.circuit.runtime.CircuitUiState

sealed interface LikersState : CircuitUiState {
    val onBack: () -> Unit
}

class Loading(
    override val onBack: () -> Unit,
) : LikersState

class Data(
    val pagingItems: LazyPagingItems<Liker>,
    val onRowClick: (Liker) -> Unit = {},
    override val onBack: () -> Unit = {},
) : LikersState
