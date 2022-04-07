package com.ferelin.stockprice.androidApp.domain.repository

import kotlinx.coroutines.flow.Flow

interface StoragePathRepository {
  val path: Flow<String>
  val authority: Flow<String>
  suspend fun setStoragePath(path: String, authority: String)
}