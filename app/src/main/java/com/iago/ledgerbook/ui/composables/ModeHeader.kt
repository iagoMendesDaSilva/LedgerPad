package com.iago.ledgerbook.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.outlined.Assessment
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.iago.ledgerbook.R
import com.iago.ledgerbook.data.AppMode
import com.iago.ledgerbook.ui.theme.LedgerBookTheme

@Composable
fun ModeHeader(
    currentMonthYear: String? = null,
    onToggle: (AppMode) -> Unit,
    onReportClick: () -> Unit,
) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            color = Color.White,
            text = currentMonthYear ?: stringResource(R.string.fixed_mode),
            style = MaterialTheme.typography.body1
        )

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
         currentMonthYear?.let {
             IconButton(
                 onClick = onReportClick
             ) {
                 Icon(
                     imageVector = Icons.Outlined.Assessment,
                     contentDescription = null,
                     tint = Color.White
                 )
             }
         }

            IconButton(
                onClick = {
                    onToggle(
                        if (currentMonthYear == null) AppMode.MONTHLY
                        else AppMode.FIXED
                    )
                }
            ) {
                Icon(
                    imageVector = Icons.Default.CurrencyExchange,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ModeHeaderPreview() {
    LedgerBookTheme {
        Surface {
            ModeHeader(
                currentMonthYear = null,
                onToggle = {}
            ) {}
        }
    }
}