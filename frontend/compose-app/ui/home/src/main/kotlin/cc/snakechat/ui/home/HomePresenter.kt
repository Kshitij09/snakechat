package cc.snakechat.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import cc.snakechat.domain.feed.GetTrendingFeed
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import me.tatarka.inject.annotations.Inject

@Inject
class HomePresenter(
    private val getTrendingFeed: GetTrendingFeed,
) : Presenter<HomeState> {

    @Composable
    override fun present(): HomeState {
        val state by produceState<HomeState>(initialValue = Loading) {
            val feed = getTrendingFeed.execute()
            value = Data(feed)
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
