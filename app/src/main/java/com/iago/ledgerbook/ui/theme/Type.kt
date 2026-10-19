package com.iago.ledgerbook.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Typography = Typography(

    titleMedium = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold
    ),

    bodySmall = TextStyle(
        fontSize = 13.sp,
        fontWeight = FontWeight.Normal
    ),
    bodyLarge = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold
    ),

    labelLarge = TextStyle(
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
    ),
    displaySmall = TextStyle(
        fontSize =20.sp,
        fontWeight = FontWeight.Medium
    ),
    displayMedium = TextStyle(
        fontSize =30.sp,
        fontWeight = FontWeight.Bold
    ),
)