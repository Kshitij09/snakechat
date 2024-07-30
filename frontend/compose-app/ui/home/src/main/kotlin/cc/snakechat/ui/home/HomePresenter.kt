package cc.snakechat.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.compose.collectAsLazyPagingItems
import cc.snakechat.domain.feed.ObservePagingData
import cc.snakechat.domain.feed.Post
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class HomePresenter(
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
                    Data(pagingItems)
                }
            }
        }
        return state
    }
}

@Inject
class HomePresenterFactory(
    private val presenterFactory: (HomeScreen) -> HomePresenter,
) : Presenter.Factory {
    override fun create(
        screen: Screen,
        navigator: Navigator,
        context: CircuitContext,
    ): Presenter<*>? = if (screen is HomeScreen) presenterFactory(screen) else null
}

@Composable
inline fun <T : Any> Flow<PagingData<T>>.rememberRetainedCachedPagingFlow(
    scope: CoroutineScope = rememberRetainedCoroutineScope(),
): Flow<PagingData<T>> = rememberRetained(this, scope) { cachedIn(scope) }
