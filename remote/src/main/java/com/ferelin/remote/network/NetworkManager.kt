package com.ferelin.remote.network

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

import com.ferelin.remote.base.BaseManager
import com.ferelin.remote.base.BaseResponse
import com.ferelin.remote.network.companyNews.CompanyNewsApi
import com.ferelin.remote.network.companyNews.CompanyNewsResponse
import com.ferelin.remote.network.companyProfile.CompanyProfileApi
import com.ferelin.remote.network.companyProfile.CompanyProfileResponse
import com.ferelin.remote.network.companyQuote.CompanyQuoteApi
import com.ferelin.remote.network.companyQuote.CompanyQuoteResponse
import com.ferelin.remote.network.stockCandles.StockCandlesApi
import com.ferelin.remote.network.stockCandles.StockCandlesResponse
import com.ferelin.remote.network.stockSymbols.StockSymbolApi
import com.ferelin.remote.network.stockSymbols.StockSymbolResponse
import com.ferelin.remote.network.throttleManager.ThrottleManager
import com.ferelin.remote.network.throttleManager.ThrottleManagerHelper
import com.ferelin.remote.utils.Api
import com.ferelin.remote.utils.RetrofitDelegate
import com.ferelin.remote.utils.offerSafe
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import retrofit2.Retrofit

open class NetworkManager : NetworkManagerHelper {

    private val mRetrofit: Retrofit by RetrofitDelegate(Api.FINNHUB_BASE_URL)

    private val mCompanyProfileService = mRetrofit.create(CompanyProfileApi::class.java)
    private val mCompanyNewsService = mRetrofit.create(CompanyNewsApi::class.java)
    private val mCompanyQuoteService = mRetrofit.create(CompanyQuoteApi::class.java)
    private val mStockCandlesService = mRetrofit.create(StockCandlesApi::class.java)
    private val mStockSymbolsService = mRetrofit.create(StockSymbolApi::class.java)

    private val mThrottleManager: ThrottleManagerHelper = ThrottleManager()

    override fun loadStockSymbols(): Flow<BaseResponse<StockSymbolResponse>> = callbackFlow {
        mStockSymbolsService
            .getStockSymbolList(Api.FINNHUB_TOKEN)
            .enqueue(BaseManager<StockSymbolResponse> {
                offerSafe(it)
            })
        awaitClose()
    }

    override fun loadCompanyProfile(symbol: String): Flow<BaseResponse<CompanyProfileResponse>> =
        callbackFlow {
            mCompanyProfileService
                .getCompanyProfile(symbol, Api.FINNHUB_TOKEN)
                .enqueue(BaseManager<CompanyProfileResponse> {
                    offerSafe(it)
                })
            awaitClose()
        }

    override fun loadStockCandles(
        symbol: String,
        from: Long,
        to: Long,
        resolution: String
    ): Flow<BaseResponse<StockCandlesResponse>> = callbackFlow {
        mStockCandlesService
            .getStockCandles(symbol, Api.FINNHUB_TOKEN, from, to, resolution)
            .enqueue(BaseManager<StockCandlesResponse> {
                offerSafe(it)
            })
        awaitClose()
    }

    override fun loadCompanyNews(
        symbol: String,
        from: String,
        to: String
    ): Flow<BaseResponse<List<CompanyNewsResponse>>> = callbackFlow {
        mCompanyNewsService
            .getCompanyNews(symbol, Api.FINNHUB_TOKEN, from, to)
            .enqueue(BaseManager<List<CompanyNewsResponse>> {
                offerSafe(it)
            })
        awaitClose()
    }

    /*
    * Example of ThrottleManager usage
    * */
    override fun loadCompanyQuote(
        symbol: String,
        position: Int,
        isImportant: Boolean
    ): Flow<BaseResponse<CompanyQuoteResponse>> = callbackFlow {

        // Add message(request) to throttle manager.
        mThrottleManager.addMessage(
            symbol = symbol,
            api = Api.COMPANY_QUOTE,
            position = position,
            eraseIfNotActual = !isImportant,
            ignoreDuplicate = isImportant
        )

        // SetUp api to invoke request
        mThrottleManager.setUpApi(Api.COMPANY_QUOTE) { symbolToRequest ->
            mCompanyQuoteService
                .getCompanyQuote(symbolToRequest, Api.FINNHUB_TOKEN)
                .enqueue(BaseManager<CompanyQuoteResponse> {
                    it.additionalMessage = symbolToRequest
                    offerSafe(it)
                })
        }
        awaitClose { mThrottleManager.invalidate() }
    }
}