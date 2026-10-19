package com.iago.ledgerbook.utils

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = " Phone",
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview(
    name = " Tablet",
    showSystemUi = true,
    device = "spec:width=800dp,height=1280dp",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
annotation class DevicePreviews()