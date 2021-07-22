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

package com.ferelin.stockprice

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ferelin.stockprice.ui.MainActivity
import com.ferelin.stockprice.ui.stocksSection.common.adapter.StockViewHolder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StocksFragmentTest {

    @get:Rule
    val activityRule = activityScenarioRule<MainActivity>()

    @Test
    fun widgetsDisplayed() {
        onView(withId(R.id.recyclerViewStocks)).check(matches(isDisplayed()))
    }

    @Test
    fun navigationByClickOnStock() {
        onView(withId(R.id.recyclerViewStocks))
            .perform(actionOnItemAtPosition<StockViewHolder>(0, click()))
        onView(withId(R.id.appBar)).check(matches(isDisplayed()))
        onView(withId(R.id.textViewCompanySymbol)).check(matches(isDisplayed()))
    }
}