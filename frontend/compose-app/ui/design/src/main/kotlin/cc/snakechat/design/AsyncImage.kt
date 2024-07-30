package cc.snakechat.design

import androidx.compose.foundation.Image
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
    loadingView: @Composable (() -> Unit)? = null,
    fallbackView: @Composable (() -> Unit)? = null,
    contentDescription: String? = null,
) {
    val painter = rememberAsyncImagePainter(
        model = url,
        transform = transform,
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
