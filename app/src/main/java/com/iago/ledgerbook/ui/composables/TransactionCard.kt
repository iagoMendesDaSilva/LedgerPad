package com.iago.ledgerbook.ui.composables

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iago.ledgerbook.data.Transaction
import com.iago.ledgerbook.data.TransactionType
import com.iago.ledgerbook.ui.previews.PreviewDataTransaction
import com.iago.ledgerbook.ui.theme.LedgerBookTheme
import com.iago.ledgerbook.ui.theme.Red
import java.util.Locale

@SuppressLint("SuspiciousIndentation")
@Composable
fun TransactionCard(transaction: Transaction, onLongPress: () -> Unit,onPress: () -> Unit) {
    val formattedValue = String.format(Locale("pt", "BR"), "%,.2f", transaction.value)

        Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .combinedClickable(
                onClick = {
                    onPress()
                },
                onLongClick = {
                    onLongPress()
                }
            ),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(MaterialTheme.shapes.small)
                    .background(
                        transaction.category.color.copy(alpha = 0.15f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = transaction.category.icon,
                    contentDescription = null,
                    tint = transaction.category.color,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    text = transaction.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = stringResource(transaction.category.title),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = if (transaction.type == TransactionType.INCOME)
                    "+$formattedValue" else "-$formattedValue",
                style = MaterialTheme.typography.labelLarge,
                color = if (transaction.type == TransactionType.INCOME) MaterialTheme.colorScheme.primary else Red
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TransactionCardIncomePreview() {
    LedgerBookTheme {
        Surface {
            TransactionCard(
                transaction = PreviewDataTransaction.transactionExpense,
                onLongPress = {}
            ) {}
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TransactionCardExpensePreview() {
    LedgerBookTheme {
        Surface {
            TransactionCard(
                transaction = PreviewDataTransaction.transactionIncome,
                onLongPress = {}
            ) {}
        }
    }
}