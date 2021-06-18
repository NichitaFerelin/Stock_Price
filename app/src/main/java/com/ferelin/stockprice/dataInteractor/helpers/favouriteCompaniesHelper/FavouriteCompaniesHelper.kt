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

package com.ferelin.stockprice.dataInteractor.helpers.favouriteCompaniesHelper

import com.ferelin.repository.adaptiveModels.AdaptiveCompany

interface FavouriteCompaniesHelper {

    suspend fun addCompanyToFavourites(adaptiveCompany: AdaptiveCompany)

    suspend fun removeCompanyFromFavourites(adaptiveCompany: AdaptiveCompany)

    suspend fun addCompanyToFavourites(symbol: String)

    suspend fun removeCompanyFromFavourites(symbol: String)
}