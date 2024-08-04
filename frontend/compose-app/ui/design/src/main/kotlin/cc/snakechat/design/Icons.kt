package cc.snakechat.design

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SnakeBackNavigationIcon(onBack: () -> Unit, modifier: Modifier = Modifier) {
    IconButton(onClick = { onBack() }, modifier = modifier) {
        Icon(
            imageVector = Icons.AutoMirrored.Default.ArrowBackIos,
            contentDescription = "Back Button",
        )
    }
}