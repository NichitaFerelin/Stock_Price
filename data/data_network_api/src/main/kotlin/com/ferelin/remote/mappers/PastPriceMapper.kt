/*
 * Copyright 2021 Leah Nichita
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ferelin.remote.mappers

import com.ferelin.domain.entities.PastPrice
import com.ferelin.remote.entities.PastPricesResponse
import com.ferelin.remote.utils.toBasicMillisTime
import com.ferelin.remote.utils.toDateStr
import com.ferelin.shared.toStrPrice
import javax.inject.Inject

class PastPriceMapper @Inject constructor() {

    fun map(pastPriceResponse: PastPricesResponse, relationCompanyId: Int): List<PastPrice> {
        with(pastPriceResponse) {
            if (
                closePrices.size != highPrices.size
                || highPrices.size != lowPrices.size
                || lowPrices.size != openPrices.size
                || openPrices.size != timestamps.size
                || timestamps.size != closePrices.size
            ) return emptyList()

            return List(timestamps.size) { index ->
                val openPrice = openPrices[index]
                val highPrice = highPrices[index]
                val lowPrice = lowPrices[index]
                val closePrice = closePrices[index]
                val timestamp = timestamps[index]

                PastPrice(
                    relationCompanyId = relationCompanyId,
                    openPrice = openPrice,
                    openPriceStr = openPrice.toStrPrice(),
                    highPrice = highPrice.toStrPrice(),
                    lowPrice = lowPrice.toStrPrice(),
                    closePrice = closePrice,
                    closePriceStr = closePrice.toStrPrice(),
                    date = timestamp.toBasicMillisTime().toDateStr()
                )
            }
        }
    }
}