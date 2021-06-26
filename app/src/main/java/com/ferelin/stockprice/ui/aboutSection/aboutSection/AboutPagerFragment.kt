package com.ferelin.stockprice.ui.aboutSection.aboutSection

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
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.ferelin.repository.adaptiveModels.AdaptiveCompany
import com.ferelin.stockprice.base.BaseFragment
import com.ferelin.stockprice.custom.OrderedTextView
import com.ferelin.stockprice.databinding.FragmentAboutPagerBinding
import com.ferelin.stockprice.viewModelFactories.CompanyViewModelFactory
import com.google.android.material.transition.MaterialFadeThrough
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AboutPagerFragment(
    selectedCompany: AdaptiveCompany? = null,
) : BaseFragment<FragmentAboutPagerBinding, AboutPagerViewModel, AboutPagerViewController>() {

    override val mViewController = AboutPagerViewController()
    override val mViewModel: AboutPagerViewModel by viewModels {
        CompanyViewModelFactory(selectedCompany)
    }

    override val mBindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentAboutPagerBinding
        get() = FragmentAboutPagerBinding::inflate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough().apply {
            duration = 200L
        }
    }

    override fun setUpViewComponents(savedInstanceState: Bundle?) {
        super.setUpViewComponents(savedInstanceState)
        setUpViewControllerArguments()
        setUpBackPressedCallback()
        setUpClickListeners()
    }

    override fun initObservers() {
        super.initObservers()
        viewLifecycleOwner.lifecycleScope.launch(mCoroutineContext.IO) {
            collectEventOnDataChanged()
        }
    }

    private suspend fun collectEventOnDataChanged() {
        mViewModel.eventOnDataChanged.collect {
            withContext(mCoroutineContext.Main) {
                mViewController.onDataChanged(
                    mViewModel.companyName,
                    mViewModel.companySymbol,
                    mViewModel.companyFavouriteIconResource
                )
            }
        }
    }

    private fun setUpViewControllerArguments() {
        mViewController.setArgumentsViewDependsOn(
            viewPagerAdapter = AboutPagerAdapter(
                childFragmentManager,
                viewLifecycleOwner.lifecycle,
                mViewModel.selectedCompany
            ),
            lastSelectedTabPosition = mViewModel.selectedTabPagePosition,
            selectedCompany = mViewModel.selectedCompany
        )
    }

    private fun setUpClickListeners() {
        setUpTabClickListeners()
        setUpImageClickListeners()
    }

    private fun onTabClicked(clickedView: OrderedTextView) {
        mViewController.onTabClicked(clickedView)
        mViewModel.selectedTabPagePosition = clickedView.orderNumber
    }

    private fun setUpTabClickListeners() {
        mViewController.viewBinding.run {
            textViewProfile.setOnClickListener { onTabClicked(it as OrderedTextView) }
            textViewChart.setOnClickListener { onTabClicked(it as OrderedTextView) }
            textViewNews.setOnClickListener { onTabClicked(it as OrderedTextView) }
            textViewForecasts.setOnClickListener { onTabClicked(it as OrderedTextView) }
            textViewIdeas.setOnClickListener { onTabClicked(it as OrderedTextView) }
        }
    }

    private fun setUpImageClickListeners() {
        mViewController.viewBinding.imageViewBack.setOnClickListener { popBackStack() }
        mViewController.viewBinding.imageViewStar.setOnClickListener {
            mViewModel.onFavouriteIconClicked()
            mViewController.onFavouriteIconClicked()
        }
    }

    private val mOnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (!mViewController.handleOnBackPressed()) {
                popBackStack()
            }
        }
    }

    private fun setUpBackPressedCallback() {
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            mOnBackPressedCallback
        )
    }

    private fun popBackStack() {
        mOnBackPressedCallback.remove()
        activity?.onBackPressed()
    }
}