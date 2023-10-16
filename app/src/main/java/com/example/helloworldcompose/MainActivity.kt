package com.example.helloworldcompose

import android.content.Context
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.example.helloworldcompose.ui.theme.HelloWorldComposeTheme
import java.util.UUID

@OptIn( ExperimentalMaterial3Api::class )
class MainActivity : ComponentActivity() {
    private val personAnimeList: List<personAnime> = dataProvider.dataBaseList
    private lateinit var context: Context
    val consolaFontFamily = FontFamily(
        Font(R.font.inconsolata_variablefont_wdth_wght, FontWeight.Medium)
    )

    override fun onCreate( savedInstanceState: Bundle? ) {
        super.onCreate( savedInstanceState )
        setContent {
            ViewContainer()
            context = LocalContext.current
        }
    }

    @Preview( showBackground = true )
    @Composable
    fun ViewContainer() {
        HelloWorldComposeTheme {
            Surface(
                modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
            ) {
                //Variables
                val imageLoader = ImageLoader.Builder( LocalContext.current ).components {
                    if ( SDK_INT >= 28 ) {
                        add( ImageDecoderDecoder.Factory() )
                    } else {
                        add( GifDecoder.Factory() )
                    }
                }.build()

                val sheetState = rememberModalBottomSheetState()
                var isSheetOpen by remember {
                    mutableStateOf( false )
                }
                var codeImage = 0
                var typeFile = ""

                //code here
                LazyColumn {
                    item {
                        personAnimeList.forEach { person ->
                            Row( modifier = Modifier
                                .padding( all = 8.dp )
                                .clickable {
                                    isSheetOpen = true
                                    codeImage = person.photo
                                    typeFile = person.typePhoto
                                    //Toast.makeText(context, codeImage, Toast.LENGTH_SHORT).show()
                                } ) {
                                if ( person.typePhoto == "png" || person.typePhoto == "jpg" ) {
                                    Image(
                                        painter = painterResource( id = person.photo ),
                                        contentDescription = person.tittle,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .size( 60.dp )
                                            .clip( CircleShape )
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
                                            .size( 60.dp )
                                            .clip( CircleShape )
                                    )
                                }
                                Spacer( modifier = Modifier.width(8.dp) )
                                Column( modifier = Modifier.fillMaxWidth() ) {
                                    Text(
                                        text = person.tittle,
                                        fontFamily = consolaFontFamily,
                                        fontWeight = FontWeight.Medium,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Spacer( modifier = Modifier.height(4.dp) )
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

                        if ( isSheetOpen ) {
                            ModalBottomSheet( sheetState = sheetState,
                                onDismissRequest = {
                                isSheetOpen = false
                            } ) {
                                //Content here
                                if ( typeFile == "png" || typeFile == "jpg" ) {
                                    Image(
                                        painter = painterResource( id = codeImage ),
                                        //contentScale = ContentScale.Crop,
                                        contentDescription = "codeImage",
                                        modifier = Modifier.fillMaxWidth(),
                                    )
                                } else {
                                    Image(
                                        painter = rememberAsyncImagePainter(
                                            codeImage,
                                            imageLoader = imageLoader
                                        ),
                                        //contentScale = ContentScale.Crop,
                                        contentDescription = "codeImageGif",
                                        modifier = Modifier.fillMaxWidth(),
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun MessageCard( msg: personAnime ) {
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
    fun MessageCardWithGif( msg: personAnime ) {
        val imageLoader = ImageLoader.Builder( LocalContext.current ).components {
            if ( SDK_INT >= 28 ) {
                add( ImageDecoderDecoder.Factory() )
            } else {
                add( GifDecoder.Factory() )
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
    @Preview(showBackground = true)
    @Composable
    fun PreviewMessageCard() {
        MessageCard(
            msg = personAnime(
                id = UUID.randomUUID(),
                tittle = "Boruto Anime",
                mesagge = "es el hijo de naruto uzumaki, un ninja que se convirtió en el hokage",
                photo = R.drawable.boruto_full_power,
                typePhoto = "gif"
            )
        )
    }
}

