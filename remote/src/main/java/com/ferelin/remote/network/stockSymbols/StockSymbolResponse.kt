package com.ferelin.remote.network.stockSymbols

import com.ferelin.remote.base.BaseResponse
import com.squareup.moshi.Json

// TODO Check response
data class StockSymbolResponse(
    @Json(name = "symbol") val stockSymbols: List<String>
) : BaseResponse()