package cc.snakechat.likers

import android.os.Parcel
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.paging.LoadState
import cc.snakechat.domain.common.ObservePagingData
import cc.snakechat.domain.model.liker.CommentId
import cc.snakechat.domain.model.liker.ContentId
import cc.snakechat.domain.model.liker.PostId
import cc.snakechat.domain.post.like.Liker
import cc.snakechat.ui.common.collectLazyRetainedCachedPagingFlow
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.TypeParceler
import me.tatarka.inject.annotations.Assisted

@Parcelize
@TypeParceler<ContentId, ContentIdParceler>
class LikersScreen(
    val contentId: ContentId
) : Screen

internal class LikesPresenter(
    @Assisted private val screen: LikersScreen,
    @Assisted private val navigator: Navigator,
    private val observePagingData: () -> ObservePagingData<ContentId, Liker>,
) : Presenter<LikersState> {
    private val onBack: () -> Unit = { navigator.pop() }

    @Composable
    override fun present(): LikersState {
        val pagingItems = collectLazyRetainedCachedPagingFlow {
            observePagingData().observe(screen.contentId)
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

class ContentIdParceler : Parceler<ContentId> {
    override fun ContentId.write(parcel: Parcel, flags: Int) {
        val identifier = when (this) {
            is CommentId -> TYPE_COMMENT
            is PostId -> TYPE_POST
        }
        parcel.writeInt(identifier)
        parcel.writeString(id)
    }

    override fun create(parcel: Parcel): ContentId {
        return when (parcel.readInt()) {
            TYPE_POST -> PostId(parcel.readString()!!)
            TYPE_COMMENT -> CommentId(parcel.readString()!!)
            else -> throw IllegalArgumentException("Unknown content id type")
        }
    }

    companion object {
        private const val TYPE_POST = 0
        private const val TYPE_COMMENT = 1
    }
}