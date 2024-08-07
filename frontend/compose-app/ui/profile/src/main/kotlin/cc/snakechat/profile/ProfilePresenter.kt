package cc.snakechat.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cc.snakechat.domain.common.DomainError
import cc.snakechat.domain.common.NoInternet
import cc.snakechat.domain.model.common.FollowListType
import cc.snakechat.domain.profile.GetUserProfile
import cc.snakechat.domain.profile.Profile
import cc.snakechat.domain.profile.UserNotFound
import cc.snakechat.resources.strings
import cc.snakechat.ui.common.screen.FollowListScreen
import cc.snakechat.ui.common.screen.ProfileScreen
import com.github.michaelbull.result.Result
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import me.tatarka.inject.annotations.Assisted

internal class ProfilePresenter(
    @Assisted private val screen: ProfileScreen,
    @Assisted private val navigator: Navigator,
    private val getUserProfile: GetUserProfile,
) : Presenter<ProfileState> {
    private val onBack: () -> Unit = { navigator.pop() }

    @Composable
    override fun present(): ProfileState {
        var profileResult by rememberRetained { mutableStateOf<Result<Profile, DomainError>?>(null) }
        LaunchedEffect(Unit) {
            profileResult = getUserProfile.execute(screen.userId)
        }
        if (profileResult == null) return Loading(screen.userId, onBack = onBack)
        val result = profileResult as Result<Profile, DomainError>
        return when {
            result.isOk -> {
                Data(
                    screen.userId,
                    onBack = onBack,
                    profile = result.value,
                    eventSink = { event ->
                        when (event) {
                            OnFollowClick -> {}
                            OnFollowersClick -> navigator.goTo(
                                FollowListScreen(
                                    listType = FollowListType.Followers,
                                    userId = screen.userId,
                                ),
                            )
                            OnFollowingClick -> navigator.goTo(
                                FollowListScreen(
                                    listType = FollowListType.Following,
                                    userId = screen.userId,
                                ),
                            )
                            is OnPostClick -> {}
                        }
                    },
                )
            }
            result.isErr -> {
                val message = when (result.error) {
                    UserNotFound -> strings.userNotFound
                    NoInternet -> strings.noInternet
                    else -> strings.somethingWentWrong
                }
                Error(screen.userId, onBack = onBack, message = message)
            }
            else -> Loading(screen.userId, onBack = onBack)
        }
    }
}
