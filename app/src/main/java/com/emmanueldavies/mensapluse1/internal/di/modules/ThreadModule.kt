/*
 * Copyright (C) 2018 StepStone Services Sp. z o.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.emmanueldavies.mensapluse1.internal.di.modules

import com.emmanueldavies.mensapluse1.presentation.UIThread
import com.usecase.reactiveusecasessample.data.executor.JobExecutor
import com.usecase.reactiveusecasessample.domain.executor.PostExecutionThread
import com.usecase.reactiveusecasessample.domain.executor.ThreadExecutor
import dagger.Binds
import dagger.Module

@Module
abstract class ThreadModule {

    @Binds
    abstract fun bindThreadExecutor(jobExecutor: JobExecutor): ThreadExecutor

    @Binds
    abstract fun bindPostExecutionThread(uiThread: UIThread): PostExecutionThread

//    @Binds
//    abstract fun bindApplication(app: AndroidApplication): Application
}
