package app.what.reservation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import app.what.reservation.core.foundation.services.AppLogger.Companion.Auditor
import app.what.reservation.core.foundation.ui.Gap
import app.what.reservation.core.foundation.ui.applyIf
import app.what.reservation.core.foundation.ui.click
import app.what.reservation.core.foundation.ui.useState
import app.what.reservation.ui.theme.icons.WHATIcons
import app.what.reservation.ui.theme.icons.filled.Features
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.compose.LocalPlatformContext
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import coil3.network.NetworkHeaders
import coil3.network.httpHeaders
import coil3.request.ImageRequest
import coil3.request.crossfade

@Composable
fun AsyncImageWithFallback(
    url: String?,
    modifier: Modifier = Modifier,
    headers: Map<String, String> = emptyMap(),
    enableDetailView: Boolean = false
) {
    var showFullScreen by useState(false)

    SubcomposeAsyncImage(
        model = url?.let {
            ImageRequest.Builder(LocalPlatformContext.current)
                .data(url)
                .httpHeaders(NetworkHeaders.Builder().apply {
                    add("Accept", "image/*,*/*;q=0.8")
                    headers.forEach { (k, v) -> add(k, v) }
                }.build())
                .crossfade(true)
                .build()
        },
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
    ) {
        val state by painter.state.collectAsState()

        when (state) {
            is AsyncImagePainter.State.Success -> {
                SubcomposeAsyncImageContent(
                    modifier = Modifier.applyIf(enableDetailView) {
                        click { showFullScreen = true }
                    }
                )
            }

            is AsyncImagePainter.State.Loading -> {
                LinearProgressIndicator(modifier = Modifier.fillMaxSize())
            }

            else -> {
                (state as? AsyncImagePainter.State.Error)?.let {
                    Auditor.err(
                        "ui",
                        "Image loading error",
                        it.result.throwable
                    )
                }

                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Icon(WHATIcons.Features, null, tint = colorScheme.primary)
                }
            }
        }

        if (showFullScreen) {
            FullScreenImageDialog(
                url = url,
                onDismiss = { showFullScreen = false }
            )
        }
    }
}

@Composable
private fun FullScreenImageDialog(
    url: String?,
    onDismiss: () -> Unit
) = Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(
        usePlatformDefaultWidth = false,
    )
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .click { onDismiss() }
    ) {
        ZoomableBox(
            modifier = Modifier
                .fillMaxSize()
                .click(enabled = false) { /* пустой обработчик */ }
        ) {
            Box(
                Modifier
                    .align(Alignment.Center)
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        translationX = offsetX
                        translationY = offsetY
                    }
            ) {
                AsyncImage(
                    model = url,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Fit
                )
            }
        }


        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
                .padding(horizontal = 12.dp)
                .systemBarsPadding()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.5f))
                    .click {

                    }
            ) {
                Icon(
                    imageVector = WHATIcons.Features,
                    contentDescription = "Загрузить",
                    tint = Color.White,
                    modifier = Modifier
                        .size(40.dp)
                        .padding(6.dp)
                )

                Text(
                    text = "Загрузить",
                    color = Color.White
                )

                Gap(8)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.5f))
                    .click(block = onDismiss)
            ) {
                Icon(
                    imageVector = WHATIcons.Features,
                    contentDescription = "Закрыть",
                    tint = Color.White,
                    modifier = Modifier
                        .size(40.dp)
                        .padding(6.dp)
                )
            }
        }
    }
}

@Composable
fun ZoomableBox(
    modifier: Modifier = Modifier,
    minScale: Float = 1f,
    maxScale: Float = 5f,
    content: @Composable ZoomableBoxScope.() -> Unit
) {
    var scale by useState(1f)
    var offsetX by useState(0f)
    var offsetY by useState(0f)
    var size by useState(IntSize.Zero)

    Box(
        modifier = modifier
            .onSizeChanged { size = it }
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    val oldScale = scale
                    scale = (scale * zoom).coerceIn(minScale, maxScale)

                    // Центрирование зума
                    val scaleRatio = scale / oldScale
                    offsetX = offsetX * scaleRatio + pan.x
//                    offsetY = offsetY * scaleRatio + pan.y

                    // Безопасное ограничение
                    val extraX = ((size.width * (scale - 1f)).coerceAtLeast(0f)) / 2f
                    ((size.height * (scale - 1f)).coerceAtLeast(0f)) / 2f

                    offsetX = offsetX.coerceIn(-extraX, extraX)
//                    offsetY = offsetY.coerceIn(-extraY, extraY)
                }
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        scale = if (scale == 1f) 2.5f else 1f
                        offsetY = 0f
                        offsetX = 0f
                    }
                )
            }
    ) {
        val scope = ZoomableBoxScopeImpl(scale, offsetX, offsetY)
        scope.content()
    }
}

interface ZoomableBoxScope {
    val scale: Float
    val offsetX: Float
    val offsetY: Float
}

private data class ZoomableBoxScopeImpl(
    override val scale: Float,
    override val offsetX: Float,
    override val offsetY: Float
) : ZoomableBoxScope