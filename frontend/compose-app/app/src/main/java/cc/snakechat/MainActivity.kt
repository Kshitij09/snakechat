package cc.snakechat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.ReportDrawn
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import cc.snakechat.design.SnakeChatTheme
import cc.snakechat.design.SnakeText
import cc.snakechat.ui.home.HomeContent
import cc.snakechat.ui.home.HomePresenter
import cc.snakechat.ui.home.HomeScreen
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator

class MainActivity : ComponentActivity() {
    private val circuit = Circuit.Builder()
        .addPresenterFactory(HomePresenter.Factory())
        .addUi<HomeScreen, HomeScreen.State> { state, modifier ->
            HomeContent(state, modifier) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    SnakeText(text = "Feed")
                }
            }
        }
        .build()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SnakeChatTheme {
                val backstack = rememberSaveableBackStack(root = HomeScreen)
                val navigator = rememberCircuitNavigator(backStack = backstack)
                CircuitCompositionLocals(circuit) {
                    NavigableCircuitContent(navigator, backstack)
                }
            }
        }
    }
}

@Composable
fun Greeting(
    name: String,
    modifier: Modifier = Modifier,
) {
    ReportDrawn()
    SnakeText(
        text = "Hello $name!",
        modifier = modifier.testTag("txt_greeting"),
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SnakeChatTheme {
        Greeting("Android")
    }
}
