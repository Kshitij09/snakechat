package cc.snakechat.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import cc.snakechat.domain.common.DomainError
import cc.snakechat.domain.common.NoInternet
import cc.snakechat.domain.profile.GetUserProfile
import cc.snakechat.domain.profile.Profile
import cc.snakechat.domain.profile.UserNotFound
import cc.snakechat.resources.strings
import com.github.michaelbull.result.Result
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize
import me.tatarka.inject.annotations.Assisted

@Parcelize
class ProfileScreen(val userId: String) : Screen

internal class ProfilePresenter(
    @Assisted private val screen: ProfileScreen,
    @Assisted private val navigator: Navigator,
    private val getUserProfile: GetUserProfile,
) : Presenter<ProfileState> {
    private val onBack: () -> Unit = { navigator.pop() }

    @Composable
    override fun present(): ProfileState {
        val profileResult by produceState<Result<Profile, DomainError>?>(null) {
            value = getUserProfile.execute(screen.userId)
        }
        if (profileResult == null) return Loading(screen.userId, onBack = onBack)
        val result = profileResult as Result<Profile, DomainError>
        return when {
            result.isOk -> {
                Data(
                    screen.userId,
                    onBack = onBack,
                    profile = result.value,
                    eventSink = {},
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
