package com.compose.news.presentation.article.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.compose.news.R
import com.compose.news.data.model.article.Article
import com.compose.news.util.convertDateFormat
import com.compose.news.util.orDash

@Composable
fun ItemArticle(article: Article?, onClick: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .clickable { onClick(article?.url.orEmpty()) }
    ) {
        AsyncImage(article = article)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(
                    brush = Brush.verticalGradient(listOf(Color.Black, Color.Black)),
                    shape = RoundedCornerShape(8.dp),
                    alpha = 0.6F
                )
        ) {
            Text(
                text = article?.title.orDash(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = stringResource(
                    id = R.string.two_params,
                    article?.author.orDash(),
                    article?.publishedAt?.substringAfter('\'').convertDateFormat()
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.End,
                color = Color.White
            )
        }
    }
}

@Composable
fun AsyncImage(article: Article?, modifier: Modifier = Modifier) {
    SubcomposeAsyncImage(
        model = article?.urlToImage,
        contentDescription = article?.title,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.LightGray),
        contentScale = ContentScale.Crop,
        loading = {
            LinearProgressIndicator()
        },
        error = {
            Image(
                modifier = modifier.height(160.dp),
                painter = painterResource(id = R.drawable.ic_broken_image),
                contentDescription = null,
                contentScale = ContentScale.FillHeight
            )
        }
    )
}