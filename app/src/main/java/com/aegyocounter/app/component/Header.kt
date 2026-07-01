package com.aegyocounter.app.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aegyocounter.app.R
import com.aegyocounter.app.ui.theme.Ongeulip

@Composable
fun Header(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.ic_fire),
                contentDescription = null,
                modifier = Modifier.size(34.dp)
            )

            Text(
                text = buildAnnotatedString {
                    append("분노유발 ")

                    withStyle(
                        SpanStyle(color = Color(0xFFC54135))
                    ) {
                        append("애교카운터")
                    }
                },
                fontSize = 44.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = Ongeulip,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Image(
                painter = painterResource(R.drawable.ic_fire),
                contentDescription = null,
                modifier = Modifier.size(34.dp)
            )
        }

        Text(
            text = "오늘도 애교를 참는 당신, 존경합니다.",
            fontSize = 24.sp,
            fontFamily = Ongeulip,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 0.dp),
            textAlign = TextAlign.Center
        )
    }
}