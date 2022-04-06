package com.ferelin.stockprice.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun CryptoItem(
  modifier: Modifier = Modifier,
  name: String,
  iconUrl: String,
  price: String,
  profit: String
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .height(100.dp)
      .padding(horizontal = 12.dp),
    backgroundColor = com.ferelin.stockprice.theme.AppTheme.colors.backgroundPrimary,
    elevation = 6.dp
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceAround
    ) {
      /*GlideImage(
        modifier = Modifier.size(40.dp),
        imageModel = iconUrl,
        failure = { FailIcon() }
      )*/
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
      ) {
        ConstrainedText(
          text = name,
          style = com.ferelin.stockprice.theme.AppTheme.typography.body2,
          color = com.ferelin.stockprice.theme.AppTheme.colors.textPrimary
        )
        ConstrainedText(
          text = price,
          style = com.ferelin.stockprice.theme.AppTheme.typography.body1,
          color = com.ferelin.stockprice.theme.AppTheme.colors.textPrimary
        )
        ConstrainedText(
          text = profit,
          style = com.ferelin.stockprice.theme.AppTheme.typography.body2,
          color = com.ferelin.stockprice.theme.AppTheme.colors.textPrimary
        )
      }
    }
  }
}

/*
@Preview
@Composable
private fun CryptoItemLight() {
  com.ferelin.stockprice.theme.AppTheme(useDarkTheme = false) {
    CryptoItem(
      name = "Bitcoin",
      iconUrl = "",
      price = "43 333 $",
      profit = "+1333 $"
    )
  }
}

@Preview
@Composable
private fun CryptoItemDark() {
  com.ferelin.stockprice.theme.AppTheme(useDarkTheme = true) {
    CryptoItem(
      name = "Bitcoin",
      iconUrl = "",
      price = "43 333 $",
      profit = "+1333 $"
    )
  }
}

@Preview
@Composable
private fun CryptoItemLongText() {
  com.ferelin.stockprice.theme.AppTheme {
    CryptoItem(
      name = "AaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAa",
      iconUrl = "",
      price = "fffffffffffffffffffffffffffffffffffffffffffffffff",
      profit = "pppppppppppppppppppppppppppppppppppppppppppppppppp"
    )
  }
}*/