package cc.snakechat

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cc.snakechat.design.SnakeChatTheme
import com.slack.circuit.backstack.SaveableBackStack
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.runtime.Navigator
import me.tatarka.inject.annotations.Inject

interface SnakeContent {
    @Composable
    fun Content(
        backstack: SaveableBackStack,
        navigator: Navigator,
        modifier: Modifier,
    )
}

@Inject
class DefaultSnakeContent(
    private val circuit: Circuit,
) : SnakeContent {
    @Composable
    override fun Content(
        backstack: SaveableBackStack,
        navigator: Navigator,
        modifier: Modifier,
    ) {
        CircuitCompositionLocals(circuit) {
            SnakeChatTheme {
                NavigableCircuitContent(navigator, backstack)
            }
        }
    }
}
