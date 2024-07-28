package cc.snakechat.ui.home

import androidx.compose.runtime.Composable
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class HomePresenter(
    @Assisted private val screen: HomeScreen,
) : Presenter<HomeScreen.State> {

    @Composable
    override fun present(): HomeScreen.State {
        return HomeScreen.State
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
