package com.aegyocounter.app.ui.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aegyocounter.app.R
import com.aegyocounter.app.ui.theme.Ongeulip

@Composable
fun CounterSection(
    count: Int,
    modifier: Modifier = Modifier
) {

    val scale = remember { Animatable(1f) }

    LaunchedEffect(count) {
        scale.snapTo(1f)

        scale.animateTo(
            targetValue = 1.2f,
            animationSpec = tween(
                durationMillis = 110,
                easing = FastOutSlowInEasing
            )
        )

        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 120)
        )
    }

    val currentScale = scale.value

    val numberColor = when {
        count >= 100 -> Color(0xFFFFC107)
        count >= 75 -> Color(0xFFE91E63)
        count >= 50 -> Color(0xFFD32F2F)
        count >= 25 -> Color(0xFFC54439)
        else -> Color(0xFF9B4038)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "오늘 애교 기록",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = Ongeulip,
            modifier = Modifier
                .clip(RoundedCornerShape(30.dp))
                .background(Color.Black)
                .padding(
                    horizontal = 26.dp,
                    vertical = 10.dp
                )
        )

        Spacer(modifier = Modifier.height(2.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .offset(y = (-8).dp),   // ← 숫자와 빠직아 함께 이동되게 수정
            contentAlignment = Alignment.Center
        ) {

            // 숫자
            Text(
                text = count.toString(),
                fontSize = 120.sp,
                fontWeight = FontWeight.ExtraBold,
                color = numberColor,
                modifier = Modifier.scale(currentScale)
            )

            // 왼쪽 빠직
            Image(
                painter = painterResource(R.drawable.ic_ppazzic),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(
                        x = (-112).dp,
                        y = 5.dp
                    )
                    .rotate(-12f)
                    .size(36.dp)
                    .scale(currentScale)
            )

            // 오른쪽 빠직
            Image(
                painter = painterResource(R.drawable.ic_ppazzic),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(
                        x = 120.dp,
                        y = 23.dp
                    )
                    .rotate(10f)
                    .size(42.dp)
                    .scale(currentScale)
            )
        }
    }
}