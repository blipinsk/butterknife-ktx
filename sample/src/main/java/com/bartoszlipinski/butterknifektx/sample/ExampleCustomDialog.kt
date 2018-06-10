package com.bartoszlipinski.butterknifektx.sample

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife

class ExampleCustomDialog(context: Context) : Dialog(context) {

    @BindView(R.id.example_layout)
    lateinit var exampleLayout: ViewGroup

    class ViewHolder(view: View) {
        @BindView(R.id.example_text)
        lateinit var name: TextView

        init {
            ButterKnife.bind(this, view)
        }
    }
}