package cc.snakechat.domain.profile

import cc.snakechat.data.profile.ProfileApi
import cc.snakechat.domain.common.ObservePagingData
import me.tatarka.inject.annotations.Provides
import me.tatarka.inject.annotations.Qualifier

interface ProfileComponent {
    @Provides
    fun provideObserveFollowingListPageData(api: ProfileApi): @FollowList(ListType.Following) ObservePagingData<String, Follow> {
        return ObserveFollowingListPageData(
            followListFetcher(api)
        )
    }

    @Provides
    fun provideObserveFollowersListPageData(api: ProfileApi): @FollowList(ListType.Followers) ObservePagingData<String, Follow> {
        return ObserveFollowerListPageData(
            followListFetcher(api)
        )
    }

    private fun followListFetcher(api: ProfileApi) = FollowListFetcher(api)
}

@Qualifier
@Target(
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.TYPE,
)
annotation class FollowList(val type: ListType)
