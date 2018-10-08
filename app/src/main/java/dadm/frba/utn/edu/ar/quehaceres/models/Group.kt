package dadm.frba.utn.edu.ar.quehaceres.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Group(val name: String, val lastAction: String, val members: List<User>): Parcelable
