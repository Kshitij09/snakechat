package cc.snakechat.likers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.paging.LoadState
import cc.snakechat.domain.common.ObservePagingData
import cc.snakechat.domain.model.common.ContentId
import cc.snakechat.domain.post.like.Liker
import cc.snakechat.ui.common.collectLazyRetainedCachedPagingFlow
import cc.snakechat.ui.common.screen.LikersScreen
import cc.snakechat.ui.common.screen.ProfileScreen
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import me.tatarka.inject.annotations.Assisted

internal class LikersPresenter(
    @Assisted private val screen: LikersScreen,
    @Assisted private val navigator: Navigator,
    private val observePagingData: () -> ObservePagingData<ContentId, Liker>,
) : Presenter<LikersState> {
    private val onBack: () -> Unit = { navigator.pop() }

    @Composable
    override fun present(): LikersState {
        val pagingItems = collectLazyRetainedCachedPagingFlow {
            observePagingData().observe(screen.contentId)
        }
        val state by remember(pagingItems) {
            derivedStateOf {
                if (pagingItems.loadState.refresh == LoadState.Loading) {
                    Loading(onBack = onBack)
                } else {
                    Data(
                        pagingItems = pagingItems,
                        onBack = onBack,
                        onRowClick = { navigator.goTo(ProfileScreen(it.id)) },
                    )
                }
            }
        }
        return state
    }
}
