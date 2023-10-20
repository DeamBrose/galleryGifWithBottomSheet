package com.example.helloworldcompose.ui

import android.content.Context
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.example.helloworldcompose.R
import com.example.helloworldcompose.data.personAnime
import com.example.helloworldcompose.provider.dataProvider
import com.example.helloworldcompose.ui.theme.HelloWorldComposeTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.nio.file.WatchEvent
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    private val personAnimeList: List<personAnime> = dataProvider.dataBaseList
    private lateinit var context: Context
    val consolaFontFamily = FontFamily(
        Font(R.font.inconsolata_variablefont_wdth_wght, FontWeight.Medium)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ViewContainer()
            context = LocalContext.current
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun ViewContainer() {
        HelloWorldComposeTheme {
            Surface(
                modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
            ) {
                //Variables
                val imageLoader = ImageLoader.Builder(LocalContext.current).components {
                    if (SDK_INT >= 28) {
                        add(ImageDecoderDecoder.Factory())
                    } else {
                        add(GifDecoder.Factory())
                    }
                }.build()

                val sheetState = rememberModalBottomSheetState()
                var isSheetOpen by remember {
                    mutableStateOf(false)
                }
                var codeImage = 0
                var typeFile = ""
                var messageBody = ""
                var tittle = ""
                var dominantColor = 0L
                var secondaryColor = 0L

                //code here
                LazyColumn {
                    item {
                        personAnimeList.forEach { person ->
                            Row(modifier = Modifier
                                .padding(all = 8.dp)
                                .clickable {
                                    isSheetOpen = true
                                    codeImage = person.photo
                                    typeFile = person.typePhoto
                                    messageBody = person.mesagge
                                    tittle = person.tittle
                                    dominantColor = person.dominantColor
                                    secondaryColor = person.secondaryColor
                                    //Toast.makeText(context, codeImage, Toast.LENGTH_SHORT).show()
                                }) {
                                if (person.typePhoto == "png" || person.typePhoto == "jpg") {
                                    Image(
                                        painter = painterResource(id = person.photo),
                                        contentDescription = person.tittle,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .size(60.dp)
                                            .clip(CircleShape)
                                    )
                                } else {
                                    Image(
                                        painter = rememberAsyncImagePainter(
                                            person.photo,
                                            imageLoader = imageLoader
                                        ),
                                        contentDescription = "All Gif media",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .size(60.dp)
                                            .clip(CircleShape)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Text(
                                        text = person.tittle,
                                        fontFamily = consolaFontFamily,
                                        fontWeight = FontWeight.Medium,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = person.mesagge,
                                        fontFamily = consolaFontFamily,
                                        fontWeight = FontWeight.Medium,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }

                        if (isSheetOpen) {
                            ModalBottomSheet(
                                sheetState = sheetState,
                                onDismissRequest = {
                                    isSheetOpen = false
                                },
                                modifier = Modifier.fillParentMaxHeight()
                            ) {
                                //Content here
                                Column {
                                    AnimateBorderCard(
                                        modifier = Modifier
                                            .wrapContentSize()
                                            .padding( start = 10.dp, end = 10.dp, bottom = 45.dp ),
                                        shape = RoundedCornerShape(8.dp),
                                        gradient = Brush
                                            .sweepGradient(
                                            listOf(Color(dominantColor), Color(secondaryColor))
                                        ),
                                        onCardClick = {
                                            //Toast.makeText(context, messageBody, Toast.LENGTH_SHORT).show()
                                        }
                                    ) {
                                        Column {
                                            if (typeFile == "png" || typeFile == "jpg") {
                                                Image(
                                                    painter = painterResource(id = codeImage),
                                                    contentDescription = "codeImage",
                                                    modifier = Modifier.fillMaxWidth(),
                                                    contentScale = ContentScale.Crop
                                                )
                                                Text(
                                                    modifier = Modifier.padding(all = 10.dp),
                                                    text = "$tittle",
                                                    fontFamily = consolaFontFamily,
                                                    fontWeight = FontWeight.Bold,
                                                    maxLines = 1,
                                                    fontSize = 20.sp,
                                                )
                                                Text(
                                                    modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 10.dp ),
                                                    text = "$messageBody",
                                                    fontFamily = consolaFontFamily,
                                                    fontWeight = FontWeight.Medium,
                                                    fontSize = 18.sp,
                                                )
                                            } else {
                                                Image(
                                                    painter = rememberAsyncImagePainter(
                                                        codeImage,
                                                        imageLoader = imageLoader
                                                    ),
                                                    contentDescription = "codeImageGif",
                                                    modifier = Modifier.fillMaxWidth(),
                                                    contentScale = ContentScale.Crop
                                                )
                                                Text(
                                                    modifier = Modifier.padding( all = 10.dp ),
                                                    text = "$tittle",
                                                    fontFamily = consolaFontFamily,
                                                    fontWeight = FontWeight.Bold,
                                                    maxLines = 1,
                                                    fontSize = 20.sp,
                                                )
                                                Text(
                                                    modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 10.dp ),
                                                    text = "$messageBody",
                                                    fontFamily = consolaFontFamily,
                                                    fontWeight = FontWeight.Medium,
                                                    fontSize = 18.sp,
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun AnimateBorderCard(
        modifier: Modifier = Modifier,
        shape: RoundedCornerShape = RoundedCornerShape(size = 0.dp),
        borderWith: Dp = 2.dp,
        gradient: Brush = Brush.sweepGradient(listOf(Color.Gray, Color.White)),
        animationDuration: Int = 10000,
        onCardClick: () -> Unit = {},
        content: @Composable () -> Unit
    ) {
        //functionality here
        val infiniteTransition = rememberInfiniteTransition(label = "End color animation")
        val degrees by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = animationDuration, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "Infinite Color"
        )
        Surface(
            modifier = modifier.clickable { onCardClick() },
            shape = shape
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(borderWith)
                    .drawWithContent {
                        rotate(degrees = degrees) {
                            drawCircle(
                                brush = gradient,
                                radius = size.width,
                                blendMode = BlendMode.SrcIn
                            )
                        }
                        drawContent()
                    },
                color = MaterialTheme.colorScheme.surface,
                shape = shape
            ) {
                content()
            }
        }
    }

    @Composable
    fun MessageCard(msg: personAnime) {
        Row(modifier = Modifier
            .padding(all = 8.dp)
            .clickable {
                Toast
                    .makeText(context, msg.tittle, Toast.LENGTH_SHORT)
                    .show()
            }) {
            Image(
                painter = painterResource(id = msg.photo),
                contentDescription = msg.tittle,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = msg.tittle,
                    fontFamily = consolaFontFamily,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = msg.mesagge,
                    fontFamily = consolaFontFamily,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

    @Composable
    fun MessageCardWithGif(msg: personAnime) {
        val imageLoader = ImageLoader.Builder(LocalContext.current).components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }.build()
        Row(modifier = Modifier
            .padding(all = 8.dp)
            .clickable {
                Toast
                    .makeText(context, msg.tittle, Toast.LENGTH_SHORT)
                    .show()
            }) {
            Image(
                painter = rememberAsyncImagePainter(
                    msg.photo, imageLoader = imageLoader
                ),
                contentDescription = "All Gif media",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = msg.tittle,
                    fontFamily = consolaFontFamily,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = msg.mesagge,
                    fontFamily = consolaFontFamily,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

    //Other Preview
    @Composable
    fun PreviewMessageCard() {
        MessageCard(
            msg = personAnime(
                id = UUID.randomUUID(),
                tittle = "Boruto Anime",
                mesagge = "es el hijo de naruto uzumaki, un ninja que se convirtió en el hokage",
                photo = R.drawable.boruto_full_power,
                typePhoto = "gif",
                dominantColor = 1L,
                secondaryColor = 1L,
            )
        )
    }
}

