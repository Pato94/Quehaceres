package dadm.frba.utn.edu.ar.quehaceres.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dadm.frba.utn.edu.ar.quehaceres.R
import dadm.frba.utn.edu.ar.quehaceres.api.Api

import dadm.frba.utn.edu.ar.quehaceres.services.Services
import kotlinx.android.synthetic.main.fragment_notification_list.*
import java.lang.IllegalStateException

class NotificationsFragment : Fragment() {

    private val services by lazy { Services(context!!) }
    private var groupId: Int? = null
    private var listener: OnListFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        groupId = arguments?.getInt(ARG_GROUP_ID) ?: throw IllegalStateException("A group id must be provided")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_notification_list, container, false)
    }

    @SuppressLint("CheckResult")
    override fun onResume() {
        super.onResume()
        services.getGroupNotifications(groupId!!)
                .doOnSubscribe {
                    loading.visibility = View.VISIBLE
                    list.visibility = View.GONE
                }
                .subscribe(
                        {
                            loading.visibility = View.GONE
                            list.visibility = View.VISIBLE
                            list.adapter = MyNotificationRecyclerViewAdapter(it, ::onNotificationClicked)
                        },
                        {
                            loading.visibility = View.GONE
                            list.visibility = View.VISIBLE
                            it.printStackTrace()
                        }
                )
    }

    fun onNotificationClicked(notification: Api.Notification) {
        if (notification.type == "VERIFICATION") {
            CreateTaskDialog(context!!, 100) { a, b -> }.show()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement Listener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: Api.Notification)
    }

    companion object {
        const val ARG_GROUP_ID = "group-id"

        @JvmStatic
        fun newInstance(groupId: Int) =
                NotificationsFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_GROUP_ID, groupId)
                    }
                }
    }
}
