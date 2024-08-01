package cc.snakechat.ui.common

import androidx.compose.runtime.Composable
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.slack.circuit.retained.rememberRetained
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

@Composable
inline fun <T : Any> collectLazyRetainedCachedPagingFlow(
    scope: CoroutineScope = rememberRetainedCoroutineScope(),
    crossinline producer: () -> Flow<PagingData<T>>,
): LazyPagingItems<T> {
    val flow = rememberRetained { producer() }
    val cachedFlow = rememberRetained(flow, scope) { flow.cachedIn(scope) }
    return cachedFlow.collectAsLazyPagingItems()
}
