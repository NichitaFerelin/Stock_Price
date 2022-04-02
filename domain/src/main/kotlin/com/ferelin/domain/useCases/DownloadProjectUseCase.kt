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

package com.ferelin.domain.useCases

import com.ferelin.domain.sources.ProjectSource
import java.io.File
import javax.inject.Inject

class DownloadProjectUseCase @Inject constructor(
    private val projectSource: ProjectSource
) {
    /**
     * Loads source project from github
     * @param destinationFile is a document file to start .zip downloading
     * */
    suspend fun download(
        downloadTitle: String,
        downloadDescription: String,
        destinationFile: File? = null,
        resultFileName: String? = null
    ) {
        projectSource.download(downloadTitle, downloadDescription, destinationFile, resultFileName)
    }
}