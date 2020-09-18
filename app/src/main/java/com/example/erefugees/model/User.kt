package com.example.erefugees.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
//
//class User {
//}

@Parcelize
class User(val uid: String, val username: String, val email:String, val password: String) :
    Parcelable {
    constructor(): this("","","",  "")
}