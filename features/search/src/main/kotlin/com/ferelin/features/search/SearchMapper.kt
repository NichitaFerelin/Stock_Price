package com.ferelin.features.search

import com.ferelin.core.domain.entity.SearchRequest

internal object SearchRequestMapper {
  fun map(search: SearchRequest): SearchViewData {
    return SearchViewData(search.id, search.request)
  }
}