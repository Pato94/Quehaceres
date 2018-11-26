package dadm.frba.utn.edu.ar.quehaceres.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import dadm.frba.utn.edu.ar.quehaceres.OnTaskValidated
import dadm.frba.utn.edu.ar.quehaceres.R
import dadm.frba.utn.edu.ar.quehaceres.api.Api

import dadm.frba.utn.edu.ar.quehaceres.services.Services
import kotlinx.android.synthetic.main.fragment_notification_list.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.lang.IllegalStateException

class NotificationsFragment : BaseFragment() {

    private val services by lazy { Services(context!!) }
    private val eventBus = EventBus.getDefault()
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

    override fun onResume() {
        super.onResume()
        loadNotifications()
    }

    @SuppressLint("CheckResult")
    private fun loadNotifications() {
        compositeSubscription.add(
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
        )
    }

    fun onNotificationClicked(notification: Api.Notification) {
        if (notification.type == "VERIFICATION") {
            if (notification.producer.id == services.currentUser()!!.id) {
                Toast.makeText(context!!, "No podés validar tu propia tarea", Toast.LENGTH_SHORT).show()
            } else if (notification.status != "to_validate") {
                Toast.makeText(context!!, "La tarea ya está validada", Toast.LENGTH_SHORT).show()
            } else {
                ValidateTaskDialog(activity!!, notification, ::validateTask).show()
            }
        }
    }

    @SuppressLint("CheckResult")
    fun validateTask(notification: Api.Notification) {
        compositeSubscription.add(
                services.validateTask(groupId!!, notification.taskId)
                .subscribe(
                        { eventBus.post(OnTaskValidated()) },
                        { Toast.makeText(context!!, "Error validating task", Toast.LENGTH_SHORT).show() }
                )
        )
    }

    @Subscribe
    fun onTaskValidated(event: OnTaskValidated) {
        loadNotifications()
    }

    override fun onStart() {
        super.onStart()
        eventBus.register(this)
    }

    override fun onStop() {
        super.onStop()
        eventBus.unregister(this)
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
