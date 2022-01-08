package com.ferelin.core.data.repository

import com.ferelin.core.checkBackgroundThread
import com.ferelin.core.data.entity.news.NewsApi
import com.ferelin.core.data.entity.news.NewsDao
import com.ferelin.core.data.mapper.NewsMapper
import com.ferelin.core.domain.entity.CompanyId
import com.ferelin.core.domain.repository.NewsRepository
import com.ferelin.core.domain.entity.News
import kotlinx.coroutines.flow.*
import javax.inject.Inject

internal class NewsRepositoryImpl @Inject constructor(
  private val api: NewsApi,
  private val dao: NewsDao
) : NewsRepository {
  override fun getAllBy(companyId: CompanyId): Flow<List<News>> {
    return dao.getAllBy(companyId.value)
      .distinctUntilChanged()
      .map { it.map(NewsMapper::map) }
  }

  override suspend fun fetchNews(companyId: CompanyId, companyTicker: String) {
    checkBackgroundThread()
    try {
      val response = api.load(companyTicker)
      dao.eraseAllBy(companyId.value)
      dao.insertAll(NewsMapper.map(response, companyId))
      fetchErrorState.value = null
    } catch (e: Exception) {
      fetchErrorState.value = e
    }
  }

  private val fetchErrorState = MutableStateFlow<Exception?>(null)
  override val fetchError: Flow<Exception?> = fetchErrorState.asStateFlow()
}