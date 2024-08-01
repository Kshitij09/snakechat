package cc.snakechat.likers

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.paging.LoadState
import cc.snakechat.domain.common.ObservePagingData
import cc.snakechat.domain.post.like.Liker
import cc.snakechat.ui.common.collectLazyRetainedCachedPagingFlow
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize
import me.tatarka.inject.annotations.Assisted

@Parcelize
class LikersScreen(val postId: String) : Screen

internal class LikesPresenter(
    @Assisted private val screen: LikersScreen,
    @Assisted private val navigator: Navigator,
    private val observePagingData: () -> ObservePagingData<String, Liker>,
) : Presenter<LikersState> {
    private val onBack: () -> Unit = { navigator.pop() }

    @Composable
    override fun present(): LikersState {
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
                        onRowClick = { Log.e("likers", "clicked") },
                    )
                }
            }
        }
        return state
    }
}
