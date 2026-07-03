package com.aegyocounter.app.ui.component

import android.content.Context
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.aegyocounter.app.CounterIntent
import com.aegyocounter.app.R
import com.aegyocounter.app.util.SoundManager

@Composable
fun CounterButtonSection(
    onIntent: (CounterIntent) -> Unit,
    modifier: Modifier = Modifier,
) {

    val context = LocalContext.current

    val plusInteraction = remember { MutableInteractionSource() }
    val minusInteraction = remember { MutableInteractionSource() }
    val resetInteraction = remember { MutableInteractionSource() }

    val plusPressed by plusInteraction.collectIsPressedAsState()
    val minusPressed by minusInteraction.collectIsPressedAsState()
    val resetPressed by resetInteraction.collectIsPressedAsState()

    val pressAnimation = spring<Float>(
        dampingRatio = 0.45f,
        stiffness = 700f
    )

    val plusScale by animateFloatAsState(
        targetValue = if (plusPressed) 0.9f else 1f,
        animationSpec = pressAnimation,
        label = "plusScale"
    )

    val minusScale by animateFloatAsState(
        targetValue = if (minusPressed) 0.9f else 1f,
        animationSpec = pressAnimation,
        label = "minusScale"
    )

    val resetScale by animateFloatAsState(
        targetValue = if (resetPressed) 0.9f else 1f,
        animationSpec = pressAnimation,
        label = "resetScale"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFD6E2)
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 12.dp,
                    vertical = 2.dp      // 4dp → 2dp
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(R.drawable.btn_minus),
                contentDescription = null,
                modifier = Modifier
                    .size(102.dp)        // 110dp → 102dp
                    .scale(minusScale)
                    .clickable(
                        interactionSource = minusInteraction,
                        indication = null
                    ) {
                        onIntent(CounterIntent.Down)
                    }
            )

            Image(
                painter = painterResource(R.drawable.btn_reset),
                contentDescription = null,
                modifier = Modifier
                    .size(56.dp)         // 60dp → 56dp
                    .scale(resetScale)
                    .clickable(
                        interactionSource = resetInteraction,
                        indication = null
                    ) {
                        onIntent(CounterIntent.Reset)
                    }
            )

            Image(
                painter = painterResource(R.drawable.btn_plus),
                contentDescription = null,
                modifier = Modifier
                    .size(102.dp)        // 110dp → 102dp
                    .scale(plusScale)
                    .clickable(
                        interactionSource = plusInteraction,
                        indication = null
                    ) {
                        SoundManager.playPop()
                        vibrate(context)
                        onIntent(CounterIntent.Up)
                    }
            )
        }
    }
}

private fun vibrate(context: Context) {
    val vibrator =
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            val manager = context.getSystemService(
                Context.VIBRATOR_MANAGER_SERVICE
            ) as android.os.VibratorManager
            manager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as android.os.Vibrator
        }

    vibrator.vibrate(
        android.os.VibrationEffect.createOneShot(
            20,
            android.os.VibrationEffect.DEFAULT_AMPLITUDE
        )
    )
}