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

package com.ferelin.feature_loading.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ferelin.domain.interactors.FirstLaunchInteractor
import com.ferelin.navigation.Router
import com.ferelin.shared.DispatchersProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoadingViewModel @Inject constructor(
    private val mFirstLaunchInteractor: FirstLaunchInteractor,
    private val mRouter: Router,
    private val mDispatchersProvider: DispatchersProvider
) : ViewModel() {

    private val mLoadPreparedState = MutableStateFlow(false)
    val loadPreparedState: StateFlow<Boolean>
        get() = mLoadPreparedState

    private var mIsFirstTimeLaunch = false

    init {
        viewModelScope.launch(mDispatchersProvider.IO) {
            prepareLaunch()
        }
    }

    fun onAnimationsStopped() {
        viewModelScope.launch(mDispatchersProvider.IO) {
            if (mIsFirstTimeLaunch) {
                // TODO to welcome fragment
                mRouter.fromLoadingToStocksPager()
                mFirstLaunchInteractor.cacheFirstTimeLaunch(false)
            } else {
                mRouter.fromLoadingToStocksPager()
            }
        }
    }

    private suspend fun prepareLaunch() {
        mIsFirstTimeLaunch = mFirstLaunchInteractor.getFirstTimeLaunch()
        mLoadPreparedState.value = true
    }
}