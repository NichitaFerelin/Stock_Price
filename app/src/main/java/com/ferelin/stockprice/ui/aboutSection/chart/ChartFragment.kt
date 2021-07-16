package com.ferelin.stockprice.ui.aboutSection.chart

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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.ferelin.repository.adaptiveModels.AdaptiveCompany
import com.ferelin.stockprice.R
import com.ferelin.stockprice.base.BaseFragment
import com.ferelin.stockprice.databinding.FragmentChartBinding
import com.ferelin.stockprice.utils.DataNotificator
import com.ferelin.stockprice.viewModelFactories.CompanyViewModelFactory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChartFragment(
    selectedCompany: AdaptiveCompany? = null
) : BaseFragment<FragmentChartBinding, ChartViewModel, ChartViewController>() {

    override val mViewController = ChartViewController()
    override val mViewModel: ChartViewModel by viewModels {
        CompanyViewModelFactory(selectedCompany)
    }

    override val mBindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentChartBinding
        get() = FragmentChartBinding::inflate

    override fun setUpViewComponents(savedInstanceState: Bundle?) {
        super.setUpViewComponents(savedInstanceState)
        setUpChartWidgetsListeners()
        setUpViewControllerArguments()
    }

    override fun initObservers() {
        super.initObservers()
        viewLifecycleOwner.lifecycleScope.launch(mCoroutineContext.IO) {
            launch { collectStateStockHistory() }
            launch { collectStateOnDayDataChanged() }
            launch { collectEventOnError() }
        }
    }

    private suspend fun collectStateOnDayDataChanged() {
        mViewModel.eventOnDayDataChanged.collect {
            withContext(mCoroutineContext.Main) {
                mViewController.onDayDataChanged(
                    currentPrice = mViewModel.stockPrice,
                    dayProfit = mViewModel.dayProfit,
                    profitBackgroundResource = mViewModel.profitBackgroundResource,
                    hintBuyFor = resources.getString(R.string.hintBuyFor)
                )
            }
        }
    }

    private suspend fun collectStateStockHistory() {
        mViewModel.stateStockHistory.collect { notificator ->
            withContext(mCoroutineContext.Main) {
                when (notificator) {
                    is DataNotificator.DataPrepared -> {
                        mViewController.onStockHistoryChanged(notificator.data!!)
                    }
                    is DataNotificator.Loading -> {
                        mViewController.onDataLoadingStateChanged(true)
                    }
                    is DataNotificator.None -> {
                        mViewController.onDataLoadingStateChanged(false)
                    }
                    else -> Unit
                }
            }
        }
    }

    private suspend fun collectEventOnError() {
        mViewModel.eventOnError.collect { message ->
            withContext(mCoroutineContext.Main) {
                mViewController.onError(message)
            }
        }
    }

    private fun setUpChartWidgetsListeners() {
        mViewController.viewBinding.run {
            cardViewDay.setOnClickListener { onCardClicked(it) }
            cardViewWeek.setOnClickListener { onCardClicked(it) }
            cardViewMonth.setOnClickListener { onCardClicked(it) }
            cardViewHalfYear.setOnClickListener { onCardClicked(it) }
            cardViewYear.setOnClickListener { onCardClicked(it) }
            cardViewAll.setOnClickListener { onCardClicked(it) }

            chartView.setOnTouchListener { clickedMarker ->
                mViewController.onChartClicked(
                    previousClickedMarker = mViewModel.lastClickedMarker,
                    newClickedMarker = clickedMarker
                )
                mViewModel.lastClickedMarker = clickedMarker
            }
        }
    }

    private fun setUpViewControllerArguments() {
        mViewController.setArgumentsViewDependsOn(
            isHistoryForChartEmpty = mViewModel.isHistoryEmpty,
            lastChartViewMode = mViewModel.chartViewMode,
            lastClickedMarker = mViewModel.lastClickedMarker,
            selectedCompany = mViewModel.selectedCompany
        )
    }

    private fun onCardClicked(card: View) {
        mViewController.onCardClicked(
            card = card as CardView,
            onNewCardClicked = { selectedChartViewMode ->
                mViewModel.onChartControlButtonClicked(selectedChartViewMode)
            }
        )
    }
}