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

package com.ferelin.feature_chart.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ferelin.core.customView.chart.ChartPastPrices
import com.ferelin.core.mapper.StockPriceMapper
import com.ferelin.core.params.ChartParams
import com.ferelin.core.resolvers.NetworkResolver
import com.ferelin.core.utils.SHARING_STOP_TIMEOUT
import com.ferelin.core.utils.ifNotEmpty
import com.ferelin.core.viewData.StockPriceViewData
import com.ferelin.domain.interactors.StockPriceInteractor
import com.ferelin.domain.useCases.pastPrice.GetPastPriceUseCase
import com.ferelin.domain.useCases.pastPrice.LoadPastPriceUseCase
import com.ferelin.feature_chart.mapper.PastPriceTypeMapper
import com.ferelin.feature_chart.viewData.ChartViewMode
import com.ferelin.feature_chart.viewData.PastPriceViewData
import com.ferelin.shared.LoadState
import com.ferelin.shared.NetworkListener
import com.ferelin.shared.ifPrepared
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChartViewModel @Inject constructor(
    stockPriceInteractor: StockPriceInteractor,
    private val getPastPriceUseCase: GetPastPriceUseCase,
    private val loadPastPriceUseCase: LoadPastPriceUseCase,
    private val pastPriceTypeMapper: PastPriceTypeMapper,
    private val stockPriceMapper: StockPriceMapper,
    private val networkResolver: NetworkResolver
) : ViewModel(), NetworkListener {

    private var pastPrices: List<PastPriceViewData> = emptyList()

    private val _pastPriceLoad = MutableStateFlow<LoadState<ChartPastPrices>>(LoadState.None())
    val pastPriceLoadState: StateFlow<LoadState<ChartPastPrices>> = _pastPriceLoad.asStateFlow()

    val isNetworkAvailable: Boolean
        get() = networkResolver.isNetworkAvailable

    var chartParams = ChartParams()

    val actualStockPrice: SharedFlow<StockPriceViewData> = stockPriceInteractor
        .observeActualStockPriceResponses()
        .filter { it is LoadState.Prepared && it.data.relationCompanyId == chartParams.companyId }
        .map { stockPriceMapper.map((it as LoadState.Prepared).data) }
        .shareIn(viewModelScope, SharingStarted.WhileSubscribed(SHARING_STOP_TIMEOUT), 1)

    var chartMode: ChartViewMode = ChartViewMode.All

    init {
        loadPastPrices()
        networkResolver.registerNetworkListener(this)
    }

    override suspend fun onNetworkAvailable() {
        viewModelScope.launch {
            _pastPriceLoad.value.ifPrepared {
                loadFromNetwork()
            } ?: loadPastPrices()
        }
    }

    override suspend fun onNetworkLost() {
        // Do nothing
    }

    override fun onCleared() {
        networkResolver.unregisterNetworkListener(this)
        super.onCleared()
    }

    fun onNewChartMode(chartViewMode: ChartViewMode) {
        this.chartMode = chartViewMode
        onNewPastPrices(pastPrices)
    }

    private fun loadPastPrices() {
        viewModelScope.launch {
            _pastPriceLoad.value = LoadState.Loading()

            loadFromDb()
            loadFromNetwork()
        }
    }

    private fun onNewPastPrices(pastPrices: List<PastPriceViewData>) {
        this.pastPrices = pastPrices

        pastPriceTypeMapper
            .mapByViewMode(chartMode, pastPrices)
            ?.let { _pastPriceLoad.value = LoadState.Prepared(it) }
    }

    private suspend fun loadFromDb() {
        getPastPriceUseCase
            .getAllBy(chartParams.companyId)
            .map(pastPriceTypeMapper::map)
            .ifNotEmpty { dbPrices -> onNewPastPrices(dbPrices) }
    }

    private suspend fun loadFromNetwork() {
        loadPastPriceUseCase
            .loadAllBy(chartParams.companyId, chartParams.companyTicker)
            .ifPrepared { preparedState ->
                onNewPastPrices(
                    pastPrices = preparedState.data.map(pastPriceTypeMapper::map)
                )
            } ?: run {
            _pastPriceLoad.value = LoadState.Error()
        }
    }
}