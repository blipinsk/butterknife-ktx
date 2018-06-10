package com.bartoszlipinski.butterknifektx.sample

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife

class ExampleCustomView : View {

    @BindView(R.id.example_layout)
    lateinit var exampleLayout: ViewGroup

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    class ViewHolder(view: View) {
        @BindView(R.id.example_text)
        lateinit var name: TextView

        init {
            ButterKnife.bind(this, view)
        }
    }
}