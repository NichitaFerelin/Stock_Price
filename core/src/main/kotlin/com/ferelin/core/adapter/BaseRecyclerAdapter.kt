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

package com.ferelin.core.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.ferelin.core.utils.recycler.BaseRecyclerViewHolder
import com.ferelin.core.utils.recycler.DiffUtilCallback
import com.ferelin.core.utils.recycler.RecyclerAdapterDelegate
import com.ferelin.core.utils.recycler.ViewHolderType
import com.ferelin.shared.ifExist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BaseRecyclerAdapter(
    vararg delegatesList: RecyclerAdapterDelegate,
    diffUtilCallback: DiffUtilCallback = DiffUtilCallback(),
) : ListAdapter<ViewHolderType, BaseRecyclerViewHolder>(diffUtilCallback) {

    private val mDelegates: List<RecyclerAdapterDelegate> = delegatesList.toList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder {
        return mDelegates[viewType].onCreateViewHolder(parent)
    }

    override fun onBindViewHolder(holder: BaseRecyclerViewHolder, position: Int) {
        holder.bind(currentList[position], null)
    }

    override fun onBindViewHolder(
        holder: BaseRecyclerViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        holder.bind(currentList[position], mutableListOf())
    }

    override fun getItemViewType(position: Int): Int {
        return mDelegates.indexOfFirst {
            it.isForValidType(currentList[position])
        }
    }

    fun getItemByPosition(position: Int): ViewHolderType {
        return currentList[position]
    }

    fun replace(newItem: ViewHolderType, position: Int) {
        currentList[position] = newItem
        notifyItemChanged(position)
    }

    fun replace(newItems: List<ViewHolderType>) {
        submitList(newItems)
    }

    fun replaceAsNew(newItems: List<ViewHolderType>) {
        submitList(emptyList())
        submitList(newItems)
    }

    suspend fun replace(newItem: ViewHolderType, selector: (ViewHolderType) -> Boolean) {
        currentList.ifExist(selector) { indexOfItem ->
            currentList[indexOfItem] = newItem

            withContext(Dispatchers.Main) {
                notifyItemChanged(indexOfItem)
            }
        }
    }

    suspend fun remove(selector: (ViewHolderType) -> Boolean) {
        currentList.ifExist(selector) { indexOfItem ->
            currentList.removeAt(indexOfItem)

            withContext(Dispatchers.Main) {
                notifyItemRemoved(indexOfItem)
            }
        }
    }

    fun add(newItem: ViewHolderType) {
        currentList.add(newItem)
    }
}