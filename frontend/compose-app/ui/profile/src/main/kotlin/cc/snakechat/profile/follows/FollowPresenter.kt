package cc.snakechat.profile.follows

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.paging.LoadState
import cc.snakechat.domain.common.ObservePagingData
import cc.snakechat.domain.profile.Follow
import cc.snakechat.domain.profile.FollowList
import cc.snakechat.domain.profile.ListType
import cc.snakechat.ui.common.collectLazyRetainedCachedPagingFlow
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize
import me.tatarka.inject.annotations.Assisted

@Parcelize
class FollowListScreen(
    val listType: ListType,
    val userId: String
) : Screen

internal class FollowPresenter(
    @Assisted private val screen: FollowListScreen,
    @Assisted private val navigator: Navigator,
    @FollowList(ListType.Followers) private val observeFollowersData: () -> ObservePagingData<String, Follow>,
    @FollowList(ListType.Following) private val observeFollowingData: () -> ObservePagingData<String, Follow>,
) : Presenter<FollowListState> {
    private val onBack: () -> Unit = { navigator.pop() }

    @Composable
    override fun present(): FollowListState {
        val pagingItems = collectLazyRetainedCachedPagingFlow {
            if (screen.listType == ListType.Following) {
                observeFollowingData().observe(screen.userId)
            } else {
                observeFollowersData().observe(screen.userId)
            }
        }
        val state by remember(pagingItems) {
            derivedStateOf {
                if (pagingItems.loadState.refresh == LoadState.Loading) {
                    Loading(listType = screen.listType, onBack = onBack)
                } else {
                    Data(
                        listType = screen.listType,
                        pagingItems = pagingItems,
                        onBack = onBack,
                        onRowClick = { }
                    )
                }
            }
        }
        return state
    }
}