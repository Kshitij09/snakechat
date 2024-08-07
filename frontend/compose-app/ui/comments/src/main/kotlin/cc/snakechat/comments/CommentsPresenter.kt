package cc.snakechat.comments

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.paging.LoadState
import cc.snakechat.domain.common.ObservePagingData
import cc.snakechat.domain.model.common.CommentId
import cc.snakechat.domain.post.comment.Comment
import cc.snakechat.ui.common.collectLazyRetainedCachedPagingFlow
import cc.snakechat.ui.common.screen.CommentsScreen
import cc.snakechat.ui.common.screen.LikersScreen
import cc.snakechat.ui.common.screen.ProfileScreen
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import me.tatarka.inject.annotations.Assisted

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
                        eventSink = { event ->
                            when (event) {
                                is OnLikeCountClick -> {
                                    navigator.goTo(
                                        LikersScreen(
                                            CommentId(event.comment.id),
                                        ),
                                    )
                                }
                                is OnProfileClick -> {
                                    navigator.goTo(ProfileScreen(event.comment.commenter.id))
                                }
                            }
                        },
                    )
                }
            }
        }
        return state
    }
}
