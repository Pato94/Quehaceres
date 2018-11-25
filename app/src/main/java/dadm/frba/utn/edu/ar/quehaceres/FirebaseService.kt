package dadm.frba.utn.edu.ar.quehaceres

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dadm.frba.utn.edu.ar.quehaceres.services.Services

class FirebaseService: FirebaseMessagingService() {

    val service by lazy { Services(baseContext) }
    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String?) {
        Log.d("FIREBASE", "Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        service.postTokenIfPossible(token)
    }

    override fun onMessageReceived(p0: RemoteMessage?) {
        Log.d("FIREBASE", "onMessageReceived")

        super.onMessageReceived(p0)
    }
}