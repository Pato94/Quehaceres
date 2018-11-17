package dadm.frba.utn.edu.ar.quehaceres.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(val id: Int, val email: String, val fullName: String): Parcelable
