package com.lduboscq.appkickstarter.ui

import androidx.compose.foundation.Image

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.layout.ContentScale
import com.seiko.imageloader.AsyncImagePainter
import com.seiko.imageloader.LocalImageLoader
import com.seiko.imageloader.rememberAsyncImagePainter

@Composable
internal fun Image(
    url: String?, modifier: Modifier = Modifier,
    contentDescription: String? = null,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null
) {
    CompositionLocalProvider(
        LocalImageLoader provides generateImageLoader(),
    ) {
        Image(
            painter = rememberAsyncImagePainter(url.toString()),
            contentDescription = contentDescription,
            modifier = modifier,
            alignment = alignment,
            contentScale = contentScale,
            alpha = alpha,
            colorFilter = colorFilter
        )
    }
}

private val AsyncImagePainter.state: Any
    get() {
        TODO("Not yet implemented")
    }

@Composable
fun RemoteImage(
    url: String?,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null
) {
    CompositionLocalProvider(
        LocalImageLoader provides generateImageLoader(),
    ) {
        val painter = rememberAsyncImagePainter(url.toString())

        Image(
            painter = painter,
            contentDescription = contentDescription,
            modifier = modifier,
            alignment = alignment,
            contentScale = contentScale,
            alpha = alpha,
            colorFilter = colorFilter
        )
//        when (painter.state) {
//            is AsyncImagePainter.State.Loading -> {
//                // Display a circular progress indicator while the image is being loaded
//               // CircularProgressIndicator(Modifier.align(Alignment.Center))
//            }
//            is AsyncImagePainter.State.Error -> {
//                // Display an error message if the image failed to load
//                Text("Failed to load image", Modifier.align(Alignment.Center))
//            }
//            else -> {}
//        }
//    }
    }
}

