package cc.snakechat.inject

import cc.snakechat.DefaultSnakeContent
import cc.snakechat.SnakeContent
import cc.snakechat.likers.CommentsComponent
import cc.snakechat.likers.LikersComponent
import cc.snakechat.profile.ProfileComponent
import cc.snakechat.ui.home.HomeComponent
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import me.tatarka.inject.annotations.Provides

interface UiComponent :
    HomeComponent,
    RootUiComponent,
    LikersComponent,
    CommentsComponent,
    ProfileComponent {

    val snakeContent: SnakeContent

    @Provides
    @ActivityScope
    fun provideCircuit(
        uiFactories: Set<Ui.Factory>,
        presenterFactories: Set<Presenter.Factory>,
    ): Circuit = Circuit.Builder()
        .addUiFactories(uiFactories)
        .addPresenterFactories(presenterFactories)
        .build()
}

interface RootUiComponent {
    @Provides
    @ActivityScope
    fun bindSnakeContent(impl: DefaultSnakeContent): SnakeContent = impl
}
