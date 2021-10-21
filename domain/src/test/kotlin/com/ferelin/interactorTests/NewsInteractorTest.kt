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

package com.ferelin.interactorTests

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.ferelin.di.DaggerTestAppComponent
import com.ferelin.domain.interactors.NewsInteractor
import com.ferelin.domain.repositories.NewsRepo
import com.ferelin.fakeData.FakeData
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject

@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
class NewsInteractorTest {

    lateinit var testCoroutineDispatcher: TestCoroutineDispatcher

    @Inject
    lateinit var newsInteractor: NewsInteractor

    @Inject
    lateinit var newsRepo: NewsRepo

    @Before
    fun before() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val component = DaggerTestAppComponent.factory().create(context)
        component.inject(this)

        testCoroutineDispatcher = TestCoroutineDispatcher()
    }

    @After
    fun after() {
        testCoroutineDispatcher.cancel()
    }

    @Test
    fun getAllBy() = testCoroutineDispatcher.runBlockingTest {

        newsRepo.insertAll(FakeData.news)
        val actual = newsInteractor.getAllBy(FakeData.relationId)

        Assert.assertEquals(FakeData.defaultSizeByRelationId, actual.size)

        actual.forEach {
            val exists = FakeData.news.contains(it)
            Assert.assertEquals(true, exists)
        }
    }
}