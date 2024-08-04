package cc.snakechat.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.compose.collectAsLazyPagingItems
import cc.snakechat.comments.CommentsScreen
import cc.snakechat.domain.common.ObservePagingData
import cc.snakechat.domain.feed.Post
import cc.snakechat.likers.LikersScreen
import cc.snakechat.profile.ProfileScreen
import cc.snakechat.ui.common.rememberRetainedCoroutineScope
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class HomePresenter(
    @Assisted private val navigator: Navigator,
    private val observePagingData: Lazy<ObservePagingData<Unit, Post>>,
) : Presenter<HomeState> {

    @Composable
    override fun present(): HomeState {
        val scope = rememberRetainedCoroutineScope()
        val feedFlow = rememberRetained { observePagingData.value.observe(Unit).cachedIn(scope) }
        val pagingItems = feedFlow.collectAsLazyPagingItems()
        val state by rememberRetained(pagingItems) {
            derivedStateOf {
                if (pagingItems.loadState.refresh is LoadState.Loading) {
                    Loading
                } else {
                    Data(pagingItems) { event ->
                        when (event) {
                            is OnLikeClicked -> navigator.goTo(LikersScreen(event.post.id))
                            is OnCommentClicked -> navigator.goTo(CommentsScreen(event.post.id))
                            is OnProfileClicked -> navigator.goTo(ProfileScreen(event.post.user.id))
                        }
                    }
                }
            }
        }
        return state
    }
}

@Inject
class HomePresenterFactory(
    private val presenterFactory: (Navigator) -> HomePresenter,
) : Presenter.Factory {
    override fun create(
        screen: Screen,
        navigator: Navigator,
        context: CircuitContext,
    ): Presenter<*>? = if (screen is HomeScreen) presenterFactory(navigator) else null
}

@Composable
inline fun <T : Any> Flow<PagingData<T>>.rememberRetainedCachedPagingFlow(
    scope: CoroutineScope = rememberRetainedCoroutineScope(),
): Flow<PagingData<T>> = rememberRetained(this, scope) { cachedIn(scope) }
