package cc.snakechat.design

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImagePainter.Companion.DefaultTransform
import coil3.compose.AsyncImagePainter.State
import coil3.compose.rememberAsyncImagePainter

@Composable
fun SnakeAsyncImage(
    modifier: Modifier = Modifier,
    url: String?,
    transform: (State) -> State = DefaultTransform,
    contentScale: ContentScale = ContentScale.Fit,
    onLoadComplete: ((State) -> Unit)? = null,
    loadingView: @Composable (() -> Unit)? = null,
    fallbackView: @Composable (() -> Unit)? = null,
    contentDescription: String? = null,
    isDebug: Boolean = false,
) {
    if (isDebug) {
        Box(modifier = modifier.background(MaterialTheme.colorScheme.surfaceDim))
    } else {
        val painter = rememberAsyncImagePainter(
            model = url,
            transform = transform,
            onState = { if (it.isLoaded && onLoadComplete != null) onLoadComplete(it) },
            contentScale = contentScale,
        )
        val state by painter.state.collectAsState()
        when {
            state is State.Error && fallbackView != null -> {
                fallbackView()
            }
            state is State.Loading && loadingView != null -> {
                loadingView()
            }
            else -> {
                Image(
                    painter = painter,
                    contentScale = contentScale,
                    contentDescription = contentDescription,
                    modifier = modifier,
                )
            }
        }
    }
}

private val State.isLoaded: Boolean get() = this is State.Success || this is State.Error
