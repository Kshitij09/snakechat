package cc.snakechat.domain.profile

import cc.snakechat.data.profile.ProfileApi
import cc.snakechat.domain.common.ObservePagingData
import cc.snakechat.domain.model.common.FollowListType
import me.tatarka.inject.annotations.Provides
import me.tatarka.inject.annotations.Qualifier

interface ProfileComponent {
    @Provides
    fun provideObserveFollowingListPageData(
        api: ProfileApi,
    ):
        @FollowList(FollowListType.Following)
        ObservePagingData<String, Follow> = ObserveFollowingListPageData(
        followListFetcher(api),
    )

    @Provides
    fun provideObserveFollowersListPageData(
        api: ProfileApi,
    ):
        @FollowList(FollowListType.Followers)
        ObservePagingData<String, Follow> = ObserveFollowerListPageData(
        followListFetcher(api),
    )

    private fun followListFetcher(api: ProfileApi) = FollowListFetcher(api)
}

@Qualifier
@Target(
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.TYPE,
)
annotation class FollowList(val type: FollowListType)
