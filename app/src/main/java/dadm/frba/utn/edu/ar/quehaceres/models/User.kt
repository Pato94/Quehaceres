package dadm.frba.utn.edu.ar.quehaceres.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(val email: String): Parcelable
