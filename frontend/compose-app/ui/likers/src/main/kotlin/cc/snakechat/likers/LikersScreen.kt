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
import com.slack.circuit.runtime.CircuitUiState

class LikersState(val postId: String) : CircuitUiState

@Composable
internal fun LikersContent(state: LikersState, modifier: Modifier = Modifier) {
    Surface(modifier = modifier) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            SnakeText(text = "Likes Screen here: ${state.postId}")
        }
    }
}

@Preview
@Composable
private fun LikersContentPreview() {
    SnakeChatTheme {
        LikersContent(state = LikersState("123"))
    }
}