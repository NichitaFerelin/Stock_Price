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

package com.ferelin.local.reposirotires

import com.ferelin.domain.entities.StockPrice
import com.ferelin.domain.repositories.StockPriceRepo
import com.ferelin.local.database.StockPriceDao
import com.ferelin.local.mappers.StockPriceMapper
import com.ferelin.shared.CoroutineContextProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockPriceRepoImpl @Inject constructor(
    private val mStockPriceDao: StockPriceDao,
    private val mStockPriceMapper: StockPriceMapper,
    private val mCoroutineContextProvider: CoroutineContextProvider
) : StockPriceRepo {

    override suspend fun getStockPrice(companyId: Int): StockPrice? =
        withContext(mCoroutineContextProvider.IO) {
            val dbo = mStockPriceDao.getStockPrice(companyId)
            return@withContext dbo?.let { mStockPriceMapper.map(it) }
        }

    override suspend fun cacheStockPrice(stockPrice: StockPrice) =
        withContext(mCoroutineContextProvider.IO) {
            mStockPriceDao.insertStockPrice(
                dbo = mStockPriceMapper.map(stockPrice)
            )
        }

    override suspend fun updateStockPrice(companyId: Int, price: String, profit: String) =
        withContext(mCoroutineContextProvider.IO) {
            mStockPriceDao.updateStockPrice(companyId, price, profit)
        }
}