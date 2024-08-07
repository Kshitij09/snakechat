package cc.snakechat.likers

import cc.snakechat.comments.CommentsContent
import cc.snakechat.comments.CommentsPresenter
import cc.snakechat.comments.CommentsState
import cc.snakechat.domain.common.ObservePagingData
import cc.snakechat.domain.post.comment.Comment
import cc.snakechat.inject.ActivityScope
import cc.snakechat.ui.common.screen.CommentsScreen
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import me.tatarka.inject.annotations.Inject
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides

interface CommentsComponent {
    @Provides
    @IntoSet
    @ActivityScope
    fun provideCommentsUiFactory(impl: CommentsUiFactory): Ui.Factory = impl

    @Provides
    @IntoSet
    @ActivityScope
    fun provideCommentsPresenterFactory(impl: CommentsPresenterFactory): Presenter.Factory = impl
}

@Inject
class CommentsPresenterFactory(
    private val observePagingData: () -> ObservePagingData<String, Comment>,
) : Presenter.Factory {
    override fun create(
        screen: Screen,
        navigator: Navigator,
        context: CircuitContext,
    ): Presenter<*>? = if (screen is CommentsScreen) {
        CommentsPresenter(
            screen = screen,
            navigator = navigator,
            observePagingData = observePagingData,
        )
    } else {
        null
    }
}

@Inject
class CommentsUiFactory : Ui.Factory {
    override fun create(screen: Screen, context: CircuitContext): Ui<*>? = if (screen is CommentsScreen) {
        ui<CommentsState> { state, modifier -> CommentsContent(state, modifier) }
    } else {
        null
    }
}
