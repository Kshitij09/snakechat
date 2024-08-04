package cc.snakechat.profile

import cc.snakechat.domain.profile.GetUserProfile
import cc.snakechat.inject.ActivityScope
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import me.tatarka.inject.annotations.Inject
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides

interface ProfileComponent {
    @Provides
    @ActivityScope
    @IntoSet
    fun provideProfilePresenterFactory(factory: ProfilePresenterFactory): Presenter.Factory = factory

    @Provides
    @ActivityScope
    @IntoSet
    fun provideProfileUiFactory(factory: ProfileUiFactory): Ui.Factory = factory
}

@Inject
class ProfilePresenterFactory(private val getUserProfile: GetUserProfile) : Presenter.Factory {
    override fun create(
        screen: Screen,
        navigator: Navigator,
        context: CircuitContext,
    ): Presenter<*>? {
        return if (screen is ProfileScreen) {
            ProfilePresenter(screen, navigator, getUserProfile)
        } else {
            null
        }
    }
}

@Inject
class ProfileUiFactory : Ui.Factory {
    override fun create(screen: Screen, context: CircuitContext): Ui<*>? {
        return if (screen is ProfileScreen) {
            ui<ProfileState> { state, modifier ->
                ProfileContent(state = state, modifier = modifier)
            }
        } else {
            null
        }
    }
}