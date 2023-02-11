package com.jessemanarim.entregaexpressa.common.presentation.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.jessemanarim.entregaexpressa.R
import com.jessemanarim.entregaexpressa.databinding.CustomDividerBinding

class CustomDivider @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyle, defStyleRes) {

    private var binding: CustomDividerBinding

    init {
        binding = CustomDividerBinding.inflate(LayoutInflater.from(context), this, true)

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.CustomDivider, 0, 0)
            val title = resources.getText(typedArray
                .getResourceId(R.styleable.CustomDivider_custom_divider_description, R.string.divider_default_value))

            with(binding) {
                tvDividerDescription.text = title
            }

            typedArray.recycle()
        }
    }
}