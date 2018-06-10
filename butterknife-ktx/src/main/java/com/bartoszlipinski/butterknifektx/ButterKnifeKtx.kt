/*
 * Copyright 2018 Bartosz Lipinski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@file:Suppress("UNUSED_PARAMETER", "unused")

package com.bartoszlipinski.butterknifektx

import android.app.Activity
import android.app.Dialog
import android.support.annotation.UiThread
import android.view.View
import butterknife.Unbinder

// Extension Bridge "End Cap"
class ButterKnifeKtx

@UiThread
fun ButterKnifeKtx.bind(target: Activity): Unbinder {
    throw IllegalStateException("ButterKnife ViewBinding wasn't generated. " +
            "Are you sure you used ButterKnife annotations in the target class?")
}

@UiThread
fun ButterKnifeKtx.bind(target: View): Unbinder {
    throw IllegalStateException("ButterKnife ViewBinding wasn't generated. " +
        "Are you sure you used ButterKnife annotations in the target class?")
}

@UiThread
fun ButterKnifeKtx.bind(target: Dialog): Unbinder {
    throw IllegalStateException("ButterKnife ViewBinding wasn't generated. " +
        "Are you sure you used ButterKnife annotations in the target class?")
}

@UiThread
fun ButterKnifeKtx.bind(target: Any, source: Activity): Unbinder {
    throw IllegalStateException("ButterKnife ViewBinding wasn't generated. " +
        "Are you sure you used ButterKnife annotations in the target class?")
}

@UiThread
fun ButterKnifeKtx.bind(target: Any, source: View): Unbinder {
    throw IllegalStateException("ButterKnife ViewBinding wasn't generated. " +
        "Are you sure you used ButterKnife annotations in the target class?")
}

@UiThread
fun ButterKnifeKtx.bind(target: Any, source: Dialog): Unbinder {
    throw IllegalStateException("ButterKnife ViewBinding wasn't generated. " +
        "Are you sure you used ButterKnife annotations in the target class?")
}

