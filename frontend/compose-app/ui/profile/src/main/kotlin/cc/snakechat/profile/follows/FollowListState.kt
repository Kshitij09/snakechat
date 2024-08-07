package cc.snakechat.profile.follows

import androidx.paging.compose.LazyPagingItems
import cc.snakechat.domain.model.common.FollowListType
import cc.snakechat.domain.profile.Follow
import com.slack.circuit.runtime.CircuitUiState

sealed interface FollowListState : CircuitUiState {
    val listType: FollowListType
    val onBack: () -> Unit
}

class Loading(
    override val listType: FollowListType,
    override val onBack: () -> Unit,
) : FollowListState

class Data(
    override val listType: FollowListType,
    val pagingItems: LazyPagingItems<Follow>,
    val onRowClick: (Follow) -> Unit = {},
    override val onBack: () -> Unit = {},
) : FollowListState
