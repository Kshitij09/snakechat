package cc.snakechat.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SnakeTopBar(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    leadingIcon: @Composable (() -> Unit)? = null,
    centerContent: @Composable (() -> Unit)? = null,
    trailingActions: @Composable (RowScope.() -> Unit)? = null,
) {
    Surface(color = containerColor, modifier = modifier) {
        if (centerContent != null) {
            Row(
                modifier = modifier
                    .windowInsetsPadding(TopAppBarDefaults.windowInsets)
                    .height(64.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (leadingIcon != null) leadingIcon()
                Box(modifier = Modifier.weight(1f)) { centerContent() }
                if (trailingActions != null) trailingActions()
            }
        } else {
            Box(modifier = modifier) {
                if (leadingIcon != null) {
                    Box(modifier = Modifier.align(Alignment.CenterStart)) {
                        leadingIcon()
                    }
                }
                if (trailingActions != null) {
                    Row(modifier = Modifier.align(Alignment.CenterEnd)) {
                        trailingActions()
                    }
                }
            }
        }
    }
}

@Composable
internal fun SnakeSearch(
    leadingIcon: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    modifier: Modifier = Modifier,
) {
    Surface(
        color = containerColor,
        shape = RoundedCornerShape(24.dp),
        modifier = modifier.height(48.dp),
    ) {
        // We can't avoid nesting of Boxes here. Event the custom `Layout` needs it to be wrapped
        // with `Box` to attach layoutId
        Row(modifier = Modifier.padding(12.dp)) {
            val contentColor = LocalContentColor.current.copy(alpha = 0.6f)
            CompositionLocalProvider(LocalContentColor provides contentColor) {
                if (leadingIcon != null) {
                    leadingIcon()
                }
                if (placeholder != null) {
                    Box(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .weight(1f),
                    ) {
                        placeholder()
                    }
                }
                if (trailingIcon != null) {
                    trailingIcon()
                }
            }
        }
    }
}
