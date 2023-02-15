package com.jessemanarim.entregaexpressa.common.presentation.components

import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import com.jessemanarim.entregaexpressa.common.util.NumberAndSpecialCharacterFilter

const val CPF_CHARACTER_LIMIT = 14
const val CEP_CHARACTER_LIMIT = 9

fun cpfMaskTextWatcher(updatedText: (String) -> Unit): TextWatcher {
    return object : TextWatcher {
        var oldValue = ""

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            oldValue = s.toString()
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable) {
            if(s.length > oldValue.length){
                if(s.length == 3 || s.length == 7)  s.append(".")
                if(s.length == 11) s.append("-")
            }
            updatedText(s.toString())
        }
    }
}

fun cpfFieldFilters() = arrayOf(
    NumberAndSpecialCharacterFilter(),
    InputFilter.LengthFilter(CPF_CHARACTER_LIMIT)
)

fun cepMaskTextWatcher(updatedText: (String) -> Unit): TextWatcher {
    return object : TextWatcher {
        var oldValue = ""

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            oldValue = s.toString()
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable) {
            if(s.length > oldValue.length){
                if(s.length == 5)  s.append("-")
            }
            updatedText(s.toString())
        }
    }
}

fun cepFieldFilters() = arrayOf(
    NumberAndSpecialCharacterFilter(),
    InputFilter.LengthFilter(CEP_CHARACTER_LIMIT)
)