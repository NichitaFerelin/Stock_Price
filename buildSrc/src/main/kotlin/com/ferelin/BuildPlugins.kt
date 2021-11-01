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

package com.ferelin

object BuildPlugins {
    const val gradleTools = "com.android.tools.build:gradle:${Versions.gradleTools}"
    const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlinLib}"
    const val googleServices = "com.google.gms:google-services:${Versions.googleServices}"
    const val versionsChecker =
        "com.github.ben-manes:gradle-versions-plugin:${Versions.versionsChecker}"
    const val firebaseCrashlytics =
        "com.google.firebase:firebase-crashlytics-gradle:${Versions.firebaseCrashlytic}"
}