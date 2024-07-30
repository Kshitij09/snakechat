package cc.snakechat.ui.home

import androidx.paging.compose.LazyPagingItems
import cc.snakechat.domain.feed.Post
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize

@Parcelize
data object HomeScreen : Screen

sealed interface HomeState : CircuitUiState

data object Loading : HomeState

class Data(val feed: LazyPagingItems<Post>) : HomeState
