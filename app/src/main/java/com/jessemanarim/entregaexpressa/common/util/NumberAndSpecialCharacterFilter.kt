package com.jessemanarim.entregaexpressa.common.util

import android.text.InputFilter
import android.text.Spanned

class NumberAndSpecialCharacterFilter : InputFilter {

    private val allowedSpecialCharacters = listOf('#', '.', '-')

    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence {
        if (source.isNullOrEmpty()) return ""

        val builder = StringBuilder()
        for (i in start until end) {
            val character = source[i]
            if (character.isDigit() || allowedSpecialCharacters.contains(character)) {
                builder.append(character)
            }
        }
        return if (builder.isEmpty()) "" else builder.toString()
    }
}