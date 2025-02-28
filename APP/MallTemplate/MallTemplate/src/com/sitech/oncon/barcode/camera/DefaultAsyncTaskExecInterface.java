/*
 * Copyright (C) 2012 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sitech.oncon.barcode.camera;

import android.os.AsyncTask;

/**
 * Before Honeycomb, {@link AsyncTask} uses parallel execution by default, which is desired. Good thing
 * too since there is no API to request otherwise.
 */
public final class DefaultAsyncTaskExecInterface implements AsyncTaskExecInterface {

  @Override
  public <T> void execute(AsyncTask<T,?,?> task, T... args) {
    task.execute(args);
  }

}
