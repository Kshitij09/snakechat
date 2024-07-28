package cc.snakechat.ui.home

import androidx.compose.runtime.Composable
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen

class HomePresenter(
    private val screen: HomeScreen,
) : Presenter<HomeScreen.State> {

    @Composable
    override fun present(): HomeScreen.State {
        return HomeScreen.State
    }

    class Factory : Presenter.Factory {
        override fun create(
            screen: Screen,
            navigator: Navigator,
            context: CircuitContext,
        ): Presenter<*>? {
            return if (screen is HomeScreen) HomePresenter(screen) else null
        }

    }
}