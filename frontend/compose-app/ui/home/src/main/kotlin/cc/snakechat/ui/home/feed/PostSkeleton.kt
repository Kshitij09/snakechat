package cc.snakechat.ui.home.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cc.snakechat.design.SnakeChatTheme

@Composable
fun FeedSkeleton(
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceContainerLow,
    modifier: Modifier
) {
    Surface(
        modifier = modifier,
        color = backgroundColor,
    ) {
        Column {
            PostSkeleton()
            Spacer(modifier = Modifier.height(20.dp))
            PostSkeleton()
        }
    }
}

@Composable
fun PostSkeleton(
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    modifier: Modifier = Modifier
) {
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp.dp
    val tinyTextBlockWidth = remember(config) { screenWidth / 9 }
    val smallTextBlockWidth = remember(config) { screenWidth / 3 }
    val largeTextBlockWidth = remember(config) { screenWidth * .8f }
    val mediumTextBlockWidth = remember(config) { screenWidth * .6f }
    val blockBackground = MaterialTheme.colorScheme.surfaceDim
    val tinyHeight = 10.dp

    @Composable
    fun TextBlock(width: Dp = smallTextBlockWidth, height: Dp = 20.dp) {
        Box(
            modifier = Modifier
                .size(width = width, height = height)
                .background(blockBackground, RoundedCornerShape(40)),
        )
    }

    @Composable
    fun SmallCircle() {
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(blockBackground, CircleShape),
        )
    }

    Surface(color = containerColor) {
        Column(modifier = modifier) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                SmallCircle()
                Spacer(modifier = Modifier.width(4.dp))
                TextBlock()
            }

            val contentModifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 300.dp)
            PostContentPlaceholder(contentModifier)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .padding(start = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                SmallCircle()
                TextBlock(width = tinyTextBlockWidth)
                SmallCircle()
                TextBlock(width = tinyTextBlockWidth)
            }
            Column(modifier = Modifier.padding(4.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                TextBlock(width = largeTextBlockWidth, height = tinyHeight)
                TextBlock(width = mediumTextBlockWidth, height = tinyHeight)
                TextBlock(height = tinyHeight)
            }
        }
    }
}

@Preview
@Composable
private fun PostSkeletonPreview() {
    SnakeChatTheme {
        Surface {
            PostSkeleton()
        }
    }
}
