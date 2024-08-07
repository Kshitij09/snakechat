package cc.snakechat

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.ReportDrawn
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.tooling.preview.Preview
import cc.snakechat.design.SnakeChatTheme
import cc.snakechat.design.SnakeText
import cc.snakechat.inject.AndroidActivityComponent
import cc.snakechat.inject.AndroidApplicationComponent
import cc.snakechat.inject.create
import cc.snakechat.ui.common.screen.HomeScreen
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.rememberCircuitNavigator

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val applicationComponent = AndroidApplicationComponent.from(this)
        val component = AndroidActivityComponent::class.create(this, applicationComponent)
        enableEdgeToEdge()
        setContent {
            val backstack = rememberSaveableBackStack(root = HomeScreen)
            val navigator = rememberCircuitNavigator(backStack = backstack)
            component.snakeContent.Content(
                backstack = backstack,
                navigator = navigator,
                modifier = Modifier.semantics {
                    @OptIn(ExperimentalComposeUiApi::class)
                    testTagsAsResourceId = true
                },
            )
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

private fun AndroidApplicationComponent.Companion.from(context: Context): AndroidApplicationComponent = (context.applicationContext as SnakeApplication).component
