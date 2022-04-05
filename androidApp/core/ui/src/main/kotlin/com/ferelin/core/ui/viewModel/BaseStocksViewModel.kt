package com.ferelin.core.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ferelin.core.coroutine.DispatchersProvider
import com.ferelin.core.ui.mapper.CompanyMapper
import com.ferelin.core.ui.viewData.StockViewData
import com.ferelin.common.domain.usecase.CompanyUseCase
import com.ferelin.common.domain.usecase.FavouriteCompanyUseCase
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

open class BaseStocksViewModel(
  private val favouriteCompanyUseCase: FavouriteCompanyUseCase,
  protected val dispatchersProvider: DispatchersProvider,
  companyUseCase: CompanyUseCase
) : ViewModel() {
  protected val companies = companyUseCase.companies
    .combine(
      flow = favouriteCompanyUseCase.favouriteCompanies,
      transform = { companies, favouriteCompaniesIds ->
        CompanyMapper.map(companies, favouriteCompaniesIds)
      }
    )
    .flowOn(dispatchersProvider.IO)

  fun onFavouriteIconClick(stockViewData: StockViewData) {
    viewModelScope.launch(dispatchersProvider.IO) {
      if (stockViewData.isFavourite) {
        favouriteCompanyUseCase.removeFromFavourite(stockViewData.id)
      } else {
        favouriteCompanyUseCase.addToFavourite(stockViewData.id)
      }
    }
  }
}