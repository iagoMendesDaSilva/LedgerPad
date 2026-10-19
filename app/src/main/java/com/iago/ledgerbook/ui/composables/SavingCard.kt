package com.iago.ledgerbook.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.iago.ledgerbook.R
import com.iago.ledgerbook.data.Transaction
import com.iago.ledgerbook.ui.previews.PreviewDataTransaction
import com.iago.ledgerbook.ui.theme.LedgerBookTheme
import java.util.Locale

@Composable
fun SavingCard(
    transaction: Transaction,
    onLongPress: () -> Unit,onPress: () -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.7f)
                .combinedClickable(
                    onClick = {
                        onPress()
                    },
                    onLongClick = {
                        onLongPress()
                    }
                ),
            shape = RoundedCornerShape(
                topStart = MaterialTheme.shapes.medium.topStart,
                bottomStart = MaterialTheme.shapes.medium.topStart,
                topEnd = MaterialTheme.shapes.large.topStart,
                bottomEnd = MaterialTheme.shapes.large.topStart,
            ), colors = CardDefaults.cardColors(containerColor = transaction.category.color),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Box(Modifier.fillMaxSize()) {

                Box(
                    Modifier
                        .fillMaxHeight()
                        .width(10.dp)
                        .background(Color.Black.copy(alpha = 0.3f))
                        .zIndex(0f)
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .zIndex(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = transaction.category.icon,
                        contentDescription = null,
                        tint = Color.Black.copy(alpha = 0.85f),
                        modifier = Modifier.fillMaxSize(.25f)
                    )
                }
            }
        }
        Column(Modifier.padding(top = 16.dp)) {
            Text(
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                text = transaction.title,
                style = MaterialTheme.typography.displaySmall,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = buildAnnotatedString {
                    append(
                        "+R$ ${
                            String.format(
                                Locale("pt", "BR"),
                                "%,.2f",
                                transaction.value
                            )
                        } "
                    )
                    addStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = MaterialTheme.typography.labelLarge.fontSize,
                            fontWeight = MaterialTheme.typography.labelLarge.fontWeight
                        ),
                        start = 0,
                        end = length
                    )

                    val start = length

                    append(stringResource(R.string.saving_per_month_suffix))
                    addStyle(
                        style = SpanStyle(
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = MaterialTheme.typography.bodySmall.fontSize,
                            fontWeight = MaterialTheme.typography.bodySmall.fontWeight,
                        ),
                        start = start,
                        end = length
                    )
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SavingCardPreview() {
    LedgerBookTheme {
        Surface {
            SavingCard(
                PreviewDataTransaction.transactionSaving,
                onLongPress={}
            ) {}
        }
    }
}