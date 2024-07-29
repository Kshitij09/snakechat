package cc.snakechat.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.compose.collectAsLazyPagingItems
import cc.snakechat.domain.feed.TrendingFeedPagingSource
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import me.tatarka.inject.annotations.Inject

@Inject
class HomePresenter(
    private val feedPagingSource: TrendingFeedPagingSource,
) : Presenter<HomeState> {

    @Composable
    override fun present(): HomeState {
        val pager = rememberRetained(feedPagingSource) {
            Pager(
                config = PagingConfig(
                    pageSize = 10,
                    prefetchDistance = 2,
                    initialLoadSize = 10,
                ),
                pagingSourceFactory = { feedPagingSource },
            )
        }
        val feed = pager.flow.collectAsLazyPagingItems()
        val state by rememberRetained(feed) {
            derivedStateOf {
                if (feed.loadState.refresh is LoadState.Loading) {
                    Loading
                } else {
                    Data(feed)
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
    ): Presenter<*>? {
        return if (screen is HomeScreen) presenterFactory(screen) else null
    }
}
