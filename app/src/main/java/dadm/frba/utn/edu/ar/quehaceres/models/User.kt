package dadm.frba.utn.edu.ar.quehaceres.models

import android.os.Parcelable
import dadm.frba.utn.edu.ar.quehaceres.api.Api
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize

data class User(
        val id: Int,
        val email: String,
        val fullName: String
): Parcelable {
    constructor(remoteUser: Api.LoginResponse) : this(remoteUser.id, remoteUser.username, remoteUser.fullName)

    @IgnoredOnParcel
    val avatar = "https://api.adorable.io/avatars/64/$email.png"
}
