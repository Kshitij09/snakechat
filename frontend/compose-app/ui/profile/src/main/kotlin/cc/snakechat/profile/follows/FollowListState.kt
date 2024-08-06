package cc.snakechat.profile.follows

import androidx.paging.compose.LazyPagingItems
import cc.snakechat.domain.profile.Follow
import cc.snakechat.domain.profile.ListType
import com.slack.circuit.runtime.CircuitUiState

sealed interface FollowListState : CircuitUiState {
    val listType: ListType
    val onBack: () -> Unit
}

class Loading(
    override val listType: ListType,
    override val onBack: () -> Unit,
) : FollowListState

class Data(
    override val listType: ListType,
    val pagingItems: LazyPagingItems<Follow>,
    val onRowClick: (Follow) -> Unit = {},
    override val onBack: () -> Unit = {},
) : FollowListState
