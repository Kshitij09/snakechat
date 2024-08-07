package cc.snakechat.ui.common.screen

import cc.snakechat.domain.model.common.ContentId
import cc.snakechat.domain.model.common.FollowListType
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.TypeParceler

interface SnakeScreen : Screen

@Parcelize
data object HomeScreen : Screen

@Parcelize
class ProfileScreen(val userId: String) : SnakeScreen

@Parcelize
class CommentsScreen(val postId: String) : SnakeScreen

@Parcelize
@TypeParceler<ContentId, ContentIdParceler>
class LikersScreen(
    val contentId: ContentId
) : SnakeScreen

@Parcelize
class FollowListScreen(
    val listType: FollowListType,
    val userId: String
) : Screen
