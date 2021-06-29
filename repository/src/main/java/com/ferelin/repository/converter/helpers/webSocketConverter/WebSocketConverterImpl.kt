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

package com.ferelin.repository.converter.helpers.webSocketConverter

import com.ferelin.remote.base.BaseResponse
import com.ferelin.remote.utils.Api
import com.ferelin.remote.webSocket.response.WebSocketResponse
import com.ferelin.repository.adaptiveModels.AdaptiveWebSocketPrice
import com.ferelin.repository.converter.adapter.DataAdapter
import com.ferelin.repository.utils.RepositoryResponse
import com.ferelin.shared.formatPrice
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebSocketConverterImpl @Inject constructor(
    private val mAdapter: DataAdapter
) : WebSocketConverter {

    override fun convertWebSocketResponseForUi(
        response: BaseResponse<WebSocketResponse>
    ): RepositoryResponse<AdaptiveWebSocketPrice> {
        return if (response.responseCode == Api.RESPONSE_OK) {
            val itemResponse = response.responseData as WebSocketResponse
            val formattedPrice = formatPrice(itemResponse.lastPrice)
            RepositoryResponse.Success(
                owner = itemResponse.symbol,
                data = AdaptiveWebSocketPrice(
                    formattedPrice,
                    mAdapter.buildProfitString(
                        currentPrice = itemResponse.lastPrice,
                        previousPrice = response.additionalMessage?.toDouble() ?: 0.0
                    )
                )
            )
        } else RepositoryResponse.Failed()
    }
}