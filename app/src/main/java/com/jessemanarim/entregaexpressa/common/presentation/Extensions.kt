package com.jessemanarim.entregaexpressa.common.presentation

import android.content.Context

fun Context.getErrorOrNull(resource: Int?): String? {
    return if(resource != null) this.getString(resource) else null
}