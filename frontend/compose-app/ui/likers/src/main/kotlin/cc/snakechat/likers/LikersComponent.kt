package cc.snakechat.likers

import cc.snakechat.domain.common.ObservePagingData
import cc.snakechat.domain.model.liker.ContentId
import cc.snakechat.domain.post.like.Liker
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

interface LikersComponent {
    @Provides
    @IntoSet
    @ActivityScope
    fun provideLikersUiFactory(impl: LikersUiFactory): Ui.Factory = impl

    @Provides
    @IntoSet
    @ActivityScope
    fun provideLikesPresenterFactory(impl: LikesPresenterFactory): Presenter.Factory = impl
}

@Inject
class LikesPresenterFactory(
    private val observePagingData: () -> ObservePagingData<ContentId, Liker>,
) : Presenter.Factory {
    override fun create(
        screen: Screen,
        navigator: Navigator,
        context: CircuitContext,
    ): Presenter<*>? = if (screen is LikersScreen) {
        LikesPresenter(
            screen = screen,
            navigator = navigator,
            observePagingData = observePagingData,
        )
    } else {
        null
    }
}

@Inject
class LikersUiFactory : Ui.Factory {
    override fun create(screen: Screen, context: CircuitContext): Ui<*>? = if (screen is LikersScreen) {
        ui<LikersState> { state, modifier -> LikersContent(state, modifier) }
    } else {
        null
    }
}
