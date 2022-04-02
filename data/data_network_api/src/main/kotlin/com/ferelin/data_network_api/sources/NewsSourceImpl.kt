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

package com.ferelin.data_network_api.sources

import com.ferelin.data_network_api.entities.NewsApi
import com.ferelin.data_network_api.mappers.NewsMapper
import com.ferelin.data_network_api.utils.withExceptionHandle
import com.ferelin.domain.entities.News
import com.ferelin.domain.sources.NewsSource
import com.ferelin.shared.DispatchersProvider
import com.ferelin.shared.LoadState
import com.ferelin.shared.NAMED_STOCKS_TOKEN
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

@Suppress("BlockingMethodInNonBlockingContext")
class NewsSourceImpl @Inject constructor(
    @Named(NAMED_STOCKS_TOKEN) private val token: String,
    private val newsApi: NewsApi,
    private val newsMapper: NewsMapper,
    private val dispatchersProvider: DispatchersProvider
) : NewsSource {

    override suspend fun loadBy(
        companyId: Int,
        companyTicker: String,
        from: String,
        to: String
    ): LoadState<List<News>> = withContext(dispatchersProvider.IO) {
        Timber.d("load by (company ticker: $companyTicker)")

        withExceptionHandle(
            request = {
                newsApi
                    .loadBy(companyTicker, token, from, to)
                    .execute()
            },
            onSuccess = { responseBody ->
                LoadState.Prepared(
                    data = responseBody.map {
                        newsMapper.map(it, companyId)
                    }
                )
            },
            onFail = { LoadState.Error() }
        )
    }
}