package cc.snakechat.comments

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.paging.LoadState
import cc.snakechat.domain.common.ObservePagingData
import cc.snakechat.domain.post.comment.Comment
import cc.snakechat.ui.common.collectLazyRetainedCachedPagingFlow
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize
import me.tatarka.inject.annotations.Assisted

@Parcelize
class CommentsScreen(val postId: String) : Screen

internal class CommentsPresenter(
    @Assisted private val screen: CommentsScreen,
    @Assisted private val navigator: Navigator,
    private val observePagingData: () -> ObservePagingData<String, Comment>,
) : Presenter<CommentsState> {
    private val onBack: () -> Unit = { navigator.pop() }

    @Composable
    override fun present(): CommentsState {
        val pagingItems = collectLazyRetainedCachedPagingFlow {
            observePagingData().observe(screen.postId)
        }
        val state by remember(pagingItems) {
            derivedStateOf {
                if (pagingItems.loadState.refresh == LoadState.Loading) {
                    Loading(onBack = onBack)
                } else {
                    Data(
                        pagingItems = pagingItems,
                        onBack = onBack,
                    )
                }
            }
        }
        return state
    }
}
