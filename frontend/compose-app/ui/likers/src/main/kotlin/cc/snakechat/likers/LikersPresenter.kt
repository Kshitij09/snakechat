package cc.snakechat.likers

import androidx.compose.runtime.Composable
import cc.snakechat.domain.common.ObservePagingData
import cc.snakechat.domain.post.Liker
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize
import me.tatarka.inject.annotations.Assisted

@Parcelize
class LikersScreen(val postId: String) : Screen

internal class LikesPresenter(
    @Assisted private val screen: LikersScreen,
    private val observePagingData: ObservePagingData<String, Liker>,
): Presenter<LikersState> {

    @Composable
    override fun present(): LikersState {
        return LikersState(screen.postId)
    }
}

