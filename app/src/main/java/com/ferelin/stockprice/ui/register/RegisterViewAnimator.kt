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

package com.ferelin.stockprice.ui.register

import android.animation.Animator
import android.animation.AnimatorInflater
import android.content.Context
import android.view.View
import com.ferelin.stockprice.R
import com.ferelin.stockprice.base.BaseViewAnimator
import com.ferelin.stockprice.utils.anim.AnimatorManager
import com.ferelin.stockprice.utils.invalidate

class RegisterViewAnimator : BaseViewAnimator() {

    private lateinit var mScaleInOut: Animator

    override fun loadAnimations(context: Context) {
        mScaleInOut = AnimatorInflater.loadAnimator(context, R.animator.scale_in_out)
    }

    override fun invalidateAnimations() {
        mScaleInOut.invalidate()
    }

    fun runScaleInOut(target: View, listener: AnimatorManager) {
        mScaleInOut.setTarget(target)
        mScaleInOut.addListener(listener)
        mScaleInOut.start()
    }
}