package com.iago.ledgerbook.data

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.BedroomBaby
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Elderly
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Spa
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.iago.ledgerbook.R
import com.iago.ledgerbook.ui.theme.Blue
import com.iago.ledgerbook.ui.theme.Brown
import com.iago.ledgerbook.ui.theme.Cyan
import com.iago.ledgerbook.ui.theme.DarkOrange
import com.iago.ledgerbook.ui.theme.DeepPurple
import com.iago.ledgerbook.ui.theme.Green
import com.iago.ledgerbook.ui.theme.Grey
import com.iago.ledgerbook.ui.theme.LightBlue
import com.iago.ledgerbook.ui.theme.LightGreen
import com.iago.ledgerbook.ui.theme.LightPink
import com.iago.ledgerbook.ui.theme.LightPurple
import com.iago.ledgerbook.ui.theme.LightYellow
import com.iago.ledgerbook.ui.theme.MintGreen
import com.iago.ledgerbook.ui.theme.Orange
import com.iago.ledgerbook.ui.theme.Pink
import com.iago.ledgerbook.ui.theme.Purple
import com.iago.ledgerbook.ui.theme.Red
import com.iago.ledgerbook.ui.theme.Yellow

enum class TransactionCategory(
    @StringRes val title: Int,
    val icon: ImageVector,
    val color: Color
) {
    HOUSE(
        R.string.house,
        Icons.Default.Home,
        Blue
    ),
    EDUCATION(
        R.string.education,
        Icons.Default.School,
        LightBlue
    ),
    BILLS(
        R.string.bill,
        Icons.Default.Receipt,
        Cyan
    ),
    HEALTH(
        R.string.health,
        Icons.Default.LocalHospital,
        Red
    ),

    FOOD(
        R.string.food,
        Icons.Default.Restaurant,
        DarkOrange
    ),
    TRANSPORT(
        R.string.transport,
        Icons.Default.DirectionsCar,
        Orange
    ),
    SALARY(
        R.string.salary,
        Icons.Default.AttachMoney,
        Green
    ),

    BONUS(
        R.string.bonus,
        Icons.Default.EmojiEvents,
        LightGreen
    ),
    TRIP(
        R.string.trip,
        Icons.Default.FlightTakeoff,
        Yellow
    ),
    PARTY(
        R.string.party,
        Icons.Default.Cake,
        LightYellow
    ),
    PET(
        R.string.pet,
        Icons.Default.Pets,
        Brown
    ),
    SELFCARE(
        R.string.selfcare,
        Icons.Default.Spa,
        DeepPurple
    ),
    LEISURE(
        R.string.leisure,
        Icons.Default.Movie,
        LightPurple
    ),
    SHOPPING(
        R.string.shopping,
        Icons.Default.ShoppingBag,
        Purple
    ),

    GIFT(
        R.string.gift,
        Icons.Default.CardGiftcard,
        Pink
    ),

    WEDDING(
        R.string.wedding,
        Icons.Default.Favorite,
        LightPink
    ),

    CHILDREN(
        R.string.children,
        Icons.Default.BedroomBaby,
        MintGreen
    ),

    RETIREMENT(
        R.string.retirement,
        Icons.Default.Elderly,
        Grey
    ),

}
