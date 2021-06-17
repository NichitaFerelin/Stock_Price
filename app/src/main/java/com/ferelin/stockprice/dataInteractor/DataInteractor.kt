package com.ferelin.stockprice.dataInteractor

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

import com.ferelin.stockprice.dataInteractor.dataManager.workers.companies.CompaniesWorkerStates
import com.ferelin.stockprice.dataInteractor.dataManager.workers.errors.ErrorsWorkerStates
import com.ferelin.stockprice.dataInteractor.dataManager.workers.favouritesCompanies.FavouriteCompaniesWorkerStates
import com.ferelin.stockprice.dataInteractor.dataManager.workers.login.LoginWorkerStates
import com.ferelin.stockprice.dataInteractor.dataManager.workers.menuItems.MenuItemsWorkerStates
import com.ferelin.stockprice.dataInteractor.dataManager.workers.messages.MessagesWorkerStates
import com.ferelin.stockprice.dataInteractor.dataManager.workers.network.NetworkConnectivityWorkerStates
import com.ferelin.stockprice.dataInteractor.dataManager.workers.searchRequests.SearchRequestsWorkerStates
import com.ferelin.stockprice.dataInteractor.helpers.apiHelper.ApiHelper
import com.ferelin.stockprice.dataInteractor.helpers.authenticationHelper.AuthenticationHelper
import com.ferelin.stockprice.dataInteractor.helpers.favouriteCompaniesHelper.FavouriteCompaniesHelper
import com.ferelin.stockprice.dataInteractor.helpers.messagesHelper.MessagesHelper
import com.ferelin.stockprice.dataInteractor.helpers.registerHelper.RegisterHelper
import com.ferelin.stockprice.dataInteractor.helpers.webHelper.WebHelper
import com.ferelin.stockprice.utils.StockHistoryConverter
import kotlinx.coroutines.flow.Flow

interface DataInteractor :
// States
    CompaniesWorkerStates,
    ErrorsWorkerStates,
    FavouriteCompaniesWorkerStates,
    LoginWorkerStates,
    MenuItemsWorkerStates,
    MessagesWorkerStates,
    NetworkConnectivityWorkerStates,
    SearchRequestsWorkerStates,
    // Helpers
    ApiHelper,
    AuthenticationHelper,
    FavouriteCompaniesHelper,
    MessagesHelper,
    RegisterHelper,
    WebHelper {

    val stockHistoryConverter: StockHistoryConverter

    suspend fun prepareData()

    suspend fun cacheNewSearchRequest(searchText: String)

    suspend fun setFirstTimeLaunchState(state: Boolean)

    suspend fun getFirstTimeLaunchState(): Boolean

    fun provideNetworkStateFlow(): Flow<Boolean>
}