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
import com.ferelin.domain.useCases.firstLaunch.GetFirstLaunchUseCase
import com.ferelin.domain.useCases.firstLaunch.SetFirstLaunchUseCase
import com.ferelin.navigation.Router
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoadingViewModel @Inject constructor(
    private val getFirstLaunchUseCase: GetFirstLaunchUseCase,
    private val setFirstLaunchUseCase: SetFirstLaunchUseCase,
    private val router: Router
) : ViewModel() {

    private val _loadPreparedState = MutableStateFlow(false)
    val loadPreparedState: StateFlow<Boolean> = _loadPreparedState.asStateFlow()

    private var isFirstTimeLaunch = false

    init {
        prepareLaunch()
    }

    fun onAnimationsStopped() {
        viewModelScope.launch {
            if (isFirstTimeLaunch) {
                router.fromLoadingToStocksPager()
                setFirstLaunchUseCase.set(false)
            } else {
                router.fromLoadingToStocksPager()
            }
        }
    }

    private fun prepareLaunch() {
        viewModelScope.launch {
            isFirstTimeLaunch = getFirstLaunchUseCase.get()
            _loadPreparedState.value = true
        }
    }
}