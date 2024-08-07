package cc.snakechat.profile.follows

import cc.snakechat.domain.common.ObservePagingData
import cc.snakechat.domain.model.common.FollowListType
import cc.snakechat.domain.profile.Follow
import cc.snakechat.domain.profile.FollowList
import cc.snakechat.inject.ActivityScope
import cc.snakechat.ui.common.screen.FollowListScreen
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import me.tatarka.inject.annotations.Inject
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides

interface FollowsComponent {
    @Provides
    @IntoSet
    @ActivityScope
    fun provideFollowListUiFactory(impl: FollowListUiFactory): Ui.Factory = impl

    @Provides
    @IntoSet
    @ActivityScope
    fun provideFollowListPresenterFactory(impl: FollowListPresenterFactory): Presenter.Factory = impl
}

@Inject
class FollowListPresenterFactory(
    @FollowList(FollowListType.Followers) private val observeFollowersData: () -> ObservePagingData<String, Follow>,
    @FollowList(FollowListType.Following) private val observeFollowingData: () -> ObservePagingData<String, Follow>,
) : Presenter.Factory {
    override fun create(
        screen: Screen,
        navigator: Navigator,
        context: CircuitContext,
    ): Presenter<*>? = if (screen is FollowListScreen) {
        FollowPresenter(
            screen = screen,
            navigator = navigator,
            observeFollowingData = observeFollowersData,
            observeFollowersData = observeFollowingData,
        )
    } else {
        null
    }
}

@Inject
class FollowListUiFactory : Ui.Factory {
    override fun create(screen: Screen, context: CircuitContext): Ui<*>? = if (screen is FollowListScreen) {
        ui<FollowListState> { state, modifier -> FollowContent(state, modifier) }
    } else {
        null
    }
}
