package com.jessemanarim.entregaexpressa.common.util

object CPFMask {
    const val CPF_MASK = "###.###.###-##"

    fun unmask(s: String): String {
        return s.replace("[^0-9]".toRegex(), "")
    }

    fun mask(cpf: String): String {
        val maskedCPF = StringBuilder()
        var i = 0
        for (c in CPF_MASK) {
            if (c != '#') {
                maskedCPF.append(c)
                continue
            }
            try {
                maskedCPF.append(cpf[i])
            } catch (e: Exception) {
                break
            }
            i++
        }
        return maskedCPF.toString()
    }
}