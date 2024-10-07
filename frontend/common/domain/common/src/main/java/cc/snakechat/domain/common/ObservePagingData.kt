package cc.snakechat.domain.common

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface ObservePagingData<in Request, Data : Any> {
    fun observe(request: Request): Flow<PagingData<Data>>
}
