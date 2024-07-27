package cc.snakechat.design

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun SnakeText(
    text: String,
    modifier: Modifier = Modifier,
    typography: TextStyle = LocalTextStyle.current,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign? = null,
    textDecoration: TextDecoration? = null,
    overflow: TextOverflow = TextOverflow.Clip,
) {
    Text(
        text = text,
        style = typography,
        textAlign = textAlign,
        overflow = overflow,
        textDecoration = textDecoration,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        modifier = modifier,
    )
}
