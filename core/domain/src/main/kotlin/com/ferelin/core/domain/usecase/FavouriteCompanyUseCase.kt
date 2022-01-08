package com.ferelin.core.domain.usecase

import com.ferelin.core.ExternalScope
import com.ferelin.core.coroutine.DispatchersProvider
import com.ferelin.core.domain.entity.CompanyId
import com.ferelin.core.domain.entity.LceState
import com.ferelin.core.domain.repository.AuthUserStateRepository
import com.ferelin.core.domain.repository.FavouriteCompanyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import javax.inject.Inject

interface FavouriteCompanyUseCase {
  val favouriteCompanies: Flow<List<CompanyId>>
  val favouriteCompaniesLce: Flow<LceState>
  suspend fun addToFavourite(companyId: CompanyId)
  suspend fun removeFromFavourite(companyId: CompanyId)
  suspend fun eraseCache()
}

internal class FavouriteCompanyUseCaseImpl @Inject constructor(
  private val favouriteCompanyRepository: FavouriteCompanyRepository,
  authUserStateRepository: AuthUserStateRepository,
  @ExternalScope scope: CoroutineScope,
  dispatchersProvider: DispatchersProvider
) : FavouriteCompanyUseCase {
  init {
    authUserStateRepository.userAuthenticated
      .filter { !it }
      .onEach { favouriteCompanyRepository.eraseAll(clearCloud = false) }
      .launchIn(scope)
  }

  override val favouriteCompanies: Flow<List<CompanyId>> = favouriteCompanyRepository.favouriteCompanies
    .onStart { favouriteCompaniesLceState.value = LceState.Loading }
    .onEach { favouriteCompaniesLceState.value = LceState.Content }
    .catch { e -> favouriteCompaniesLceState.value = LceState.Error(e.message) }
    .flowOn(dispatchersProvider.IO)

  private val favouriteCompaniesLceState = MutableStateFlow<LceState>(LceState.None)
  override val favouriteCompaniesLce: Flow<LceState> = favouriteCompaniesLceState.asStateFlow()

  override suspend fun addToFavourite(companyId: CompanyId) {
    favouriteCompanyRepository.addToFavourite(companyId)
  }

  override suspend fun removeFromFavourite(companyId: CompanyId) {
    favouriteCompanyRepository.removeFromFavourite(companyId)
  }

  override suspend fun eraseCache() {
    favouriteCompanyRepository.eraseAll(clearCloud = true)
  }
}