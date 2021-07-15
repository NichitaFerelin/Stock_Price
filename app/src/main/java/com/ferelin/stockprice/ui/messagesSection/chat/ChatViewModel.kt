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

package com.ferelin.stockprice.ui.messagesSection.chat

import androidx.lifecycle.viewModelScope
import com.ferelin.repository.adaptiveModels.AdaptiveChat
import com.ferelin.stockprice.base.BaseViewModel
import com.ferelin.stockprice.ui.messagesSection.chat.adapter.MessagesRecyclerAdapter
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatViewModel(val chat: AdaptiveChat) : BaseViewModel() {

    val messagesAdapter = MessagesRecyclerAdapter().apply {
        setHasStableIds(true)
    }

    private val mEventNewItem = MutableSharedFlow<Unit>()
    val eventNewItem: SharedFlow<Unit>
        get() = mEventNewItem

    val eventOnError: SharedFlow<String>
        get() = mDataInteractor.sharedLoadMessagesError

    override fun initObserversBlock() {
        viewModelScope.launch(mCoroutineContext.IO) {
            launch { collectStateMessages() }
            launch { collectSharedMessagesUpdates() }
        }
    }

    fun onSendClicked(text: String) {
        if (text.isNotEmpty()) {
            viewModelScope.launch(mCoroutineContext.IO) {
                mDataInteractor.sendMessageTo(chat.associatedUserNumber, text)
            }
        }
    }

    private suspend fun collectStateMessages() {
        mDataInteractor.loadMessagesFor(chat.associatedUserNumber)
            /*.collect { notificator ->
                when (notificator) {
                    is DataNotificator.DataPrepared -> {
                        withContext(mCoroutineContext.Main) {
                            messagesAdapter.setData(notificator.data!!.messages)
                        }
                        findNewMessages()
                    }
                    is DataNotificator.NoData -> {
                        mDataInteractor.loadMessagesAssociatedWithLogin(
                            chat.associatedUserNumber
                        )
                    }
                    else -> Unit
                }
            }*/
    }

    private suspend fun findNewMessages() {
        // mDataInteractor.findNewMessages(mDataInteractor.userLogin, chat.associatedUserLogin)
    }

    private suspend fun collectSharedMessagesUpdates() {
        mDataInteractor.sharedMessagesHolderUpdates.collect { adaptiveMessage ->
            withContext(mCoroutineContext.Main) {
                messagesAdapter.addItem(adaptiveMessage)
            }
            mEventNewItem.emit(Unit)
        }
    }
}