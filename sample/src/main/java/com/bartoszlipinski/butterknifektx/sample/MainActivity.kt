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
package com.bartoszlipinski.butterknifektx.sample

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.bartoszlipinski.butterknifektx.ButterKnifeKtx
import com.bartoszlipinski.butterknifektx.bind

class MainActivity : Activity() {

    @BindView(R.id.example_layout)
    lateinit var exampleLayout: ViewGroup

    @OnClick(R.id.example_button)
    fun onButtonClick() {
        Toast.makeText(this, "This happened on button click", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        ButterKnife.bind(this) //<-- uses Java Reflection

        ButterKnifeKtx.bind(this) //<-- uses Kotlin extension function
    }

    class ViewHolder(view: View) {
        @BindView(R.id.example_text)
        lateinit var name: TextView

        init {
            ButterKnifeKtx.bind(this, view)
        }

        class InnerViewHolder(view: View) {
            @BindView(R.id.example_text)
            lateinit var name: TextView

            init {
                ButterKnifeKtx.bind(this, view)
            }
        }
    }
}

