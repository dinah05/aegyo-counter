package com.aegyocounter.app.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aegyocounter.app.R

@Composable
fun CharacterSection(
    catMessage: String,
    girlMessage: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .offset(y = (-28).dp),
        contentAlignment = Alignment.Center
    ) {

        Image(
            painter = painterResource(R.drawable.main_character),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth(0.84f)
        )

        // 왼쪽 말풍선
        Text(
            text = catMessage,
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(
                    x = 58.dp,
                    y = 58.dp
                ),
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )

        // 오른쪽 말풍선
        Text(
            text = girlMessage,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(
                    x = (-58).dp,
                    y = 56.dp
                ),
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )
    }
}