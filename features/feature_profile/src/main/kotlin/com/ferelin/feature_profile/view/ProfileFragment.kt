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

package com.ferelin.feature_profile.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ferelin.core.base.BaseFragment
import com.ferelin.core.base.BaseViewModelFactory
import com.ferelin.domain.entities.Profile
import com.ferelin.feature_profile.databinding.FragmentProfileBinding
import com.ferelin.feature_profile.viewModel.ProfileLoadState
import com.ferelin.feature_profile.viewModel.ProfileViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

// TODO temp value
val companyId = 1
val companyName = "Apple"
val companyLogoUrl = ""

class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    override val mBindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentProfileBinding
        get() = FragmentProfileBinding::inflate

    @Inject
    lateinit var viewModelFactory: BaseViewModelFactory<ProfileViewModel>

    private val mViewModel: ProfileViewModel by viewModels(
        factoryProducer = { viewModelFactory }
    )

    override fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch(mDispatchersProvider.IO) {
            mViewModel.profileLoadState.collect { loadState ->
                when (loadState) {
                    is ProfileLoadState.None -> {
                        mViewModel.loadProfile(companyId)
                    }
                    is ProfileLoadState.Loaded -> {
                        setUi(loadState.profile)
                    }
                    else -> {

                    }

                }
            }
        }
    }

    private fun setUi(profile: Profile) {
        with(mViewBinding) {
            textViewName.text = companyName
            textViewWebUrl.text = profile.webUrl
            textViewCountry.text = profile.country
            textViewIndustry.text = profile.industry
            textViewPhone.text = profile.phone
            textViewCapitalization.text = profile.capitalization

            Glide
                .with(root)
                .load(companyLogoUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageViewIcon)
        }
    }
}