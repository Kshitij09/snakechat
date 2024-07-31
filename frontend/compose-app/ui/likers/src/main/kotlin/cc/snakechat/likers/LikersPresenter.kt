package cc.snakechat.likers

import androidx.compose.runtime.Composable
import cc.snakechat.domain.common.ObservePagingData
import cc.snakechat.domain.post.Liker
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

internal class LikesPresenter(
    @Assisted private val screen: LikersScreen,
    private val observePagingData: ObservePagingData<String, Liker>,
): Presenter<LikersState> {

    @Composable
    override fun present(): LikersState {
        return LikersState(screen.postId)
    }
}

@Inject
class LikesPresenterFactory(
    private val observePagingData: ObservePagingData<String, Liker>,
) : Presenter.Factory {
    override fun create(
        screen: Screen,
        navigator: Navigator,
        context: CircuitContext,
    ): Presenter<*>? {
        return if (screen is LikersScreen) {
            LikesPresenter(
                screen = screen,
                observePagingData = observePagingData,
            )
        } else {
            null
        }
    }
}
