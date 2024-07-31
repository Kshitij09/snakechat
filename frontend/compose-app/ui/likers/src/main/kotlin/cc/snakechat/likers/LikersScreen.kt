package cc.snakechat.likers

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
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import kotlinx.parcelize.Parcelize
import me.tatarka.inject.annotations.Inject
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides

@Composable
fun LikersContent(state: LikersState, modifier: Modifier = Modifier) {
    Surface(modifier = modifier) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            SnakeText(text = "Likes Screen here: ${state.postId}")
        }
    }
}

@Inject
class LikersUiFactory : Ui.Factory {
    override fun create(screen: Screen, context: CircuitContext): Ui<*>? {
        return if (screen is LikersScreen) {
            ui<LikersState> { state, modifier -> LikersContent(state, modifier) }
        } else {
            null
        }
    }
}


@Parcelize
class LikersScreen(val postId: String) : Screen

class LikersState(val postId: String) : CircuitUiState


interface LikersComponent {
    @Provides
    @IntoSet
    @ActivityScope
    fun provideLikersUiFactory(impl: LikersUiFactory): Ui.Factory = impl

    @Provides
    @IntoSet
    @ActivityScope
    fun provideLikesPresenterFactory(impl: LikesPresenterFactory): Presenter.Factory = impl
}

@Preview
@Composable
private fun LikersContentPreview() {
    SnakeChatTheme {
        LikersContent(state = LikersState("123"))
    }
}