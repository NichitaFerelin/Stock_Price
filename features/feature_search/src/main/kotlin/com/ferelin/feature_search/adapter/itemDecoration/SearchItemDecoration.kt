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

package com.ferelin.feature_search.adapter.itemDecoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ferelin.feature_search.R

open class SearchItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val firstItemStartMargin =
        context.resources.getDimension(R.dimen.searchItemStartMargin).toInt()
    private val defaultStartMargin =
        context.resources.getDimension(R.dimen.searchItemMargin).toInt()

    open val twoRows: Boolean = true

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val finalChildCounter = parent.adapter?.itemCount ?: 0

        when {
            parent.getChildAdapterPosition(view) == 0 -> outRect.left = firstItemStartMargin

            twoRows && parent.getChildAdapterPosition(view) == 1 -> {
                outRect.left = firstItemStartMargin
            }

            parent.getChildAdapterPosition(view) == finalChildCounter - 1 -> {
                addMarginToLastItem(outRect)
            }

            twoRows && parent.getChildAdapterPosition(view) == finalChildCounter - 2 -> {
                addMarginToLastItem(outRect)
            }
            else -> outRect.left = defaultStartMargin
        }
    }

    private fun addMarginToLastItem(outRect: Rect) {
        outRect.left = defaultStartMargin
        outRect.right = firstItemStartMargin
    }
}