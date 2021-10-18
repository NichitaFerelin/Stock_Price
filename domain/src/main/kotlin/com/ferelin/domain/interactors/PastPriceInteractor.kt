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

package com.ferelin.domain.interactors

import com.ferelin.domain.entities.PastPrice
import com.ferelin.domain.repositories.PastPriceRepo
import com.ferelin.domain.sources.PastPriceSource
import com.ferelin.shared.DispatchersProvider
import com.ferelin.shared.LoadState
import com.ferelin.shared.ifPrepared
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

class PastPriceInteractor @Inject constructor(
    private val pastPriceRepo: PastPriceRepo,
    private val pastPriceSource: PastPriceSource,
    private val dispatchersProvider: DispatchersProvider,
    @Named("ExternalScope") private val externalScope: CoroutineScope
) {
    suspend fun getAllBy(relationCompanyId: Int): List<PastPrice> {
        return pastPriceRepo.getAllBy(relationCompanyId)
    }

    suspend fun loadAllBy(
        relationCompanyId: Int,
        relationCompanyTicker: String
    ): LoadState<List<PastPrice>> {
        return pastPriceSource.loadBy(relationCompanyId, relationCompanyTicker)
            .also { cacheIfLoaded(it, relationCompanyId) }
    }

    private fun cacheIfLoaded(responseState: LoadState<List<PastPrice>>, relationCompanyId: Int) {
        responseState.ifPrepared { preparedState ->
            externalScope.launch(dispatchersProvider.IO) {
                pastPriceRepo.eraseBy(relationCompanyId)
                pastPriceRepo.insertAll(preparedState.data)
            }
        }
    }
}