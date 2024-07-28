package cc.snakechat.ui.home

import cc.snakechat.inject.ActivityScope
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides

interface HomeComponent {
    @IntoSet
    @Provides
    @ActivityScope
    fun bindHomePresenterFactory(factory: HomePresenterFactory): Presenter.Factory = factory

    @IntoSet
    @Provides
    @ActivityScope
    fun bindHomeUiFactory(factory: HomeUiFactory): Ui.Factory = factory
}