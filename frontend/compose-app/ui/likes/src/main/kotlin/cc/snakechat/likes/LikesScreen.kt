package cc.snakechat.likes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import cc.snakechat.design.SnakeChatTheme
import cc.snakechat.design.SnakeText
import cc.snakechat.inject.ActivityScope
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.presenter.presenterOf
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import kotlinx.parcelize.Parcelize
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides

@Composable
fun LikesContent(state: LikesState, modifier: Modifier = Modifier) {
    Surface(modifier = modifier) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            SnakeText(text = "Likes Screen here: ${state.postId}")
        }
    }
}

@Inject
class LikesUiFactory : Ui.Factory {
    override fun create(screen: Screen, context: CircuitContext): Ui<*>? {
        return if (screen is LikesScreen) {
            ui<LikesState> { state, modifier -> LikesContent(state, modifier) }
        } else {
            null
        }
    }
}

@Composable
fun LikesPresenter(@Assisted screen: LikesScreen): LikesState {
    return LikesState(screen.postId)
}

@Inject
class LikesPresenterFactory : Presenter.Factory {
    override fun create(
        screen: Screen,
        navigator: Navigator,
        context: CircuitContext,
    ): Presenter<*>? {
        return if (screen is LikesScreen) presenterOf { LikesPresenter(screen) } else null
    }
}

@Parcelize
class LikesScreen(val postId: String) : Screen

class LikesState(val postId: String) : CircuitUiState


interface LikesComponent {
    @Provides
    @IntoSet
    @ActivityScope
    fun provideLikesUiFactory(impl: LikesUiFactory): Ui.Factory = impl

    @Provides
    @IntoSet
    @ActivityScope
    fun provideLikesPresenterFactory(impl: LikesPresenterFactory): Presenter.Factory = impl
}

@Preview
@Composable
private fun LikesContentPreview() {
    SnakeChatTheme {
        LikesContent(state = LikesState("123"))
    }
}