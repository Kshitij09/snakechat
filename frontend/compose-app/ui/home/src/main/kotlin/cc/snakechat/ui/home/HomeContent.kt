package cc.snakechat.ui.home

import androidx.activity.compose.ReportDrawn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.SmartDisplay
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import cc.snakechat.design.SnakeChatTheme
import cc.snakechat.design.SnakeElevation
import cc.snakechat.design.SnakeText
import cc.snakechat.resources.strings
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import me.tatarka.inject.annotations.Inject

@Inject
class HomeUiFactory : Ui.Factory {
    override fun create(screen: Screen, context: CircuitContext): Ui<*>? {
        return if (screen is HomeScreen) ui<HomeScreen.State> { state, modifier ->
            HomeContent(state = state, modifier = modifier) {

            }
        } else null
    }
}

@Composable
fun HomeContent(
    state: HomeScreen.State,
    modifier: Modifier = Modifier,
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        topBar = {
            SnakeTopBar(
                leadingIcon = { LanguageIcon(onClick = { /*TODO*/ }) },
                centerContent = {
                    SnakeSearch(
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search Icon",
                            )
                        },
                        placeholder = {
                            SnakeText(text = "Search")
                        },
                        trailingIcon = {
                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(
                                    imageVector = Icons.Default.Mic,
                                    contentDescription = "Voice Search CTA",
                                )
                            }
                        },
                    )
                },
                trailingActions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications CTA",
                        )
                    }
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile CTA",
                        )
                    }
                },
            )
        },
        bottomBar = {
            BottomAppBar(
                tonalElevation = SnakeElevation.Level2,
            ) {
                NavigationBarItem(
                    label = {
                        SnakeText(text = strings.home)
                    },
                    selected = true,
                    onClick = { /*TODO*/ },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "BottomBar Home CTA",
                        )
                    },
                )
                NavigationBarItem(
                    label = {
                        SnakeText(text = strings.explore)
                    },
                    selected = false,
                    onClick = { /*TODO*/ },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Explore,
                            contentDescription = "BottomBar Explore CTA",
                        )
                    },
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { /*TODO*/ },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.AddCircle,
                            contentDescription = "BottomBar Create CTA",
                        )
                    },
                )
                NavigationBarItem(
                    label = {
                        SnakeText(text = strings.live)
                    },
                    selected = false,
                    onClick = { /*TODO*/ },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Sensors,
                            contentDescription = "BottomBar Live CTA",
                        )
                    },
                )
                NavigationBarItem(
                    label = {
                        SnakeText(text = strings.videos)
                    },
                    selected = false,
                    onClick = { /*TODO*/ },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.SmartDisplay,
                            contentDescription = "BottomBar Videos CTA",
                        )
                    },
                )
            }
        },
        content = {
            content(it)
            ReportDrawn()
        },
        modifier = modifier,
    )
}

@Preview
@Composable
private fun SnakeScaffoldPreview() {
    SnakeChatTheme {
        HomeContent(state = HomeScreen.State) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                SnakeText(text = "Feed")
            }
        }
    }
}
