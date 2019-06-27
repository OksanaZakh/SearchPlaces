package com.ozakharchenko.placesearch.utils

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.ozakharchenko.placesearch.R
import kotlinx.android.synthetic.main.category_custom_layout.view.*

class CategoryCustomLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyle, defStyleRes) {

    init {
        LayoutInflater.from(context)
            .inflate(R.layout.category_custom_layout, this, true)
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.CategoryCustomLayout, 0, 0)
            val title = resources.getText(
                typedArray.getResourceId(
                    R.styleable.CategoryCustomLayout_category_name,
                    R.string.educational
                )
            )
            titleCustom.text = title
            val imgSrc = resources.getDrawable(
                typedArray.getResourceId(
                    R.styleable.CategoryCustomLayout_img_src,
                    R.mipmap.ic_education_foreground
                ), null
            )
            imageViewCustom.setImageDrawable(imgSrc)
            typedArray.recycle()
        }
    }

    fun getText(): String{
        return titleCustom.text.toString()
    }
}