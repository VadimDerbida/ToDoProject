package com.example.todoproject.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class Task (val title: String? = null, val subject: String? = null, val id: String, val date:String? = null, var status:String? = null): Parcelable {
    constructor():this("", "", "", " ", "")
}