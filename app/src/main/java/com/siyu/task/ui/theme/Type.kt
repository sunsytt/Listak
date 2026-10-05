package com.siyu.task.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.siyu.task.R

val appFont = FontFamily(
    Font(R.font.quicksand, FontWeight.Normal),
    Font(R.font.clarity_city, FontWeight.Bold)
)

val AppTypography = Typography(
    headlineMedium = TextStyle(fontFamily = appFont, fontWeight = FontWeight.Bold, fontSize = 28.sp),
    titleLarge = TextStyle(fontFamily = appFont, fontWeight = FontWeight.Bold, fontSize = 22.sp),
    titleMedium = TextStyle(fontFamily = appFont, fontWeight = FontWeight.Bold, fontSize = 18.sp),
    bodyLarge = TextStyle(fontFamily = appFont, fontWeight = FontWeight.Normal, fontSize = 16.sp),
    labelLarge = TextStyle(fontFamily = appFont, fontWeight = FontWeight.Bold, fontSize = 14.sp),
    labelMedium = TextStyle(fontFamily = appFont, fontWeight = FontWeight.Normal, fontSize = 12.sp, letterSpacing = 0.5.sp)
)