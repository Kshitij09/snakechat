package cc.snakechat.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import cc.snakechat.design.SnakeChatTheme
import cc.snakechat.design.SnakeElevation
import cc.snakechat.design.SnakeText
import cc.snakechat.resources.strings
import cc.snakechat.ui.home.feed.FeedScreen
import cc.snakechat.ui.home.feed.mockPost
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import kotlinx.coroutines.flow.flowOf
import me.tatarka.inject.annotations.Inject

@Inject
class HomeUiFactory : Ui.Factory {
    override fun create(screen: Screen, context: CircuitContext): Ui<*>? = if (screen is HomeScreen) {
        ui<HomeState> { state, modifier ->
            HomeContent(state = state, modifier = modifier)
        }
    } else {
        null
    }
}

@Composable
fun HomeContent(
    state: HomeState,
    modifier: Modifier = Modifier,
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
            val paddingModifier = Modifier.padding(it)
            when (state) {
                is Data -> {
                    FeedScreen(
                        state = state,
                        modifier = paddingModifier,
                    )
                }
                Loading -> LoadingScreen(modifier = paddingModifier)
            }
        },
        modifier = modifier,
    )
}

@Preview
@Composable
private fun SnakeScaffoldPreview() {
    val fakePagingData = flowOf(
        PagingData.from(listOf(mockPost)),
    )
    val state = Data(fakePagingData.collectAsLazyPagingItems())
    SnakeChatTheme {
        HomeContent(state = state)
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        SnakeText(text = "Loading")
    }
}
