package cc.snakechat.profile

import cc.snakechat.domain.profile.PostThumbnail
import cc.snakechat.domain.profile.Profile
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

sealed interface ProfileState : CircuitUiState {
    val id: String
    val onBack: () -> Unit
}

class Loading(
    override val id: String,
    override val onBack: () -> Unit,
) : ProfileState

class Error(
    override val id: String,
    override val onBack: () -> Unit,
    val message: String
) : ProfileState

class Data(
    override val id: String,
    override val onBack: () -> Unit,
    val profile: Profile,
    val eventSink: (ProfileEvent) -> Unit,
) : ProfileState

sealed interface ProfileEvent : CircuitUiEvent

class OnPostClick(val postId: String) : ProfileEvent
data object OnFollowersClick : ProfileEvent
data object OnFollowingClick : ProfileEvent
data object OnFollowClick : ProfileEvent


internal val fakeThumbnail = PostThumbnail("123", "https://picsum.photos/200/300")

internal val fakeProfile = Profile(
    followersCount = 4218,
    followingCount = 1560,
    id = "pope",
    postThumbnails = List(10) { fakeThumbnail },
    postsCount = 7283,
    profileUrl = null,
    status = """
            |very very very very very
            |very very very very very 
            |very very very long status""".trimMargin(),
    username = "Madeline Pope"
)

internal val fakeDataState = Data(
    id = fakeProfile.id,
    profile = fakeProfile,
    onBack = {},
    eventSink = {}
)
