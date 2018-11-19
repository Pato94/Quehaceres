package dadm.frba.utn.edu.ar.quehaceres.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Task(val id: Int, val desc: String, val difLevel: Int, val puajLevel: Int) : Parcelable
