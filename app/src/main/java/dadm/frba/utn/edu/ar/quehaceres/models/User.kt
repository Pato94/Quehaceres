package dadm.frba.utn.edu.ar.quehaceres.models

import android.os.Parcelable
import dadm.frba.utn.edu.ar.quehaceres.api.Api
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
        val id: Int,
        val email: String,
        val fullName: String,
        val avatar: String
): Parcelable {
    constructor(remoteUser: Api.RemoteUser) : this(remoteUser.id, remoteUser.username, remoteUser.fullName, remoteUser.photoUrl)
}
