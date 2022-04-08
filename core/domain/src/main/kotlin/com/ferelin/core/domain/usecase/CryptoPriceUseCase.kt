package com.ferelin.core.domain.usecase

import com.ferelin.core.coroutine.DispatchersProvider
import com.ferelin.core.domain.entity.Crypto
import com.ferelin.core.domain.entity.CryptoPrice
import com.ferelin.core.domain.entity.LceState
import com.ferelin.core.domain.repository.CryptoPriceRepository
import kotlinx.coroutines.flow.*

interface CryptoPriceUseCase {
  val cryptoPrices: Flow<List<CryptoPrice>>
  suspend fun fetchPriceFor(cryptos: List<Crypto>)
  val cryptoPricesLce: Flow<LceState>
}

internal class CryptoPriceUseCaseImpl(
  private val cryptoPriceRepository: CryptoPriceRepository,
  private val dispatchersProvider: DispatchersProvider
) : CryptoPriceUseCase {
  override val cryptoPrices: Flow<List<CryptoPrice>>
    get() = cryptoPriceRepository.cryptoPrices
      .zip(
        other = cryptoPriceRepository.fetchError,
        transform = { cryptoPrices, exception ->
          if (exception != null) {
            cryptoPricesLceState.value = LceState.Error(exception.message)
          }
          cryptoPrices
        }
      )
      .onStart { cryptoPricesLceState.value = LceState.Loading }
      .onEach { cryptoPricesLceState.value = LceState.Content }
      .catch { e -> cryptoPricesLceState.value = LceState.Error(e.message) }
      .flowOn(dispatchersProvider.IO)

  override suspend fun fetchPriceFor(cryptos: List<Crypto>) {
    cryptoPricesLceState.value = LceState.Loading
    cryptoPriceRepository.fetchPriceFor(cryptos)
    cryptoPricesLceState.value = LceState.Content
  }

  private val cryptoPricesLceState = MutableStateFlow<LceState>(LceState.None)
  override val cryptoPricesLce: Flow<LceState> = cryptoPricesLceState.asStateFlow()
}