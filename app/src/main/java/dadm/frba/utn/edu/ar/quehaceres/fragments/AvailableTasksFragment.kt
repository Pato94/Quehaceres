package dadm.frba.utn.edu.ar.quehaceres.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import dadm.frba.utn.edu.ar.quehaceres.R
import dadm.frba.utn.edu.ar.quehaceres.api.Api
import dadm.frba.utn.edu.ar.quehaceres.services.Services
import kotlinx.android.synthetic.main.fragment_tasks_list.*
import org.greenrobot.eventbus.EventBus
import java.lang.IllegalStateException

class AvailableTasksFragment : Fragment() {

    private val services by lazy { Services(context!!) }
    private var listener: Listener? = null
    private var groupId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            groupId = it.getInt(ARG_GROUP_ID)
        }

        if (groupId == null) {
            throw IllegalStateException("Group ID cannot be null")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tasks_list, container, false)
    }

    @SuppressLint("CheckResult")
    override fun onResume() {
        super.onResume()
        services.availableTasks(groupId!!)
                .doOnSubscribe {
                    loading.visibility = View.VISIBLE
                    list.visibility = View.GONE
                }
                .subscribe(
                        {
                            loading.visibility = View.GONE
                            list.visibility = View.VISIBLE
                            list.adapter = AvailableTasksAdapter(it, listener)
                        },
                        {
                            loading.visibility = View.GONE
                            list.visibility = View.VISIBLE
                            Toast.makeText(context!!, "Hubo un error al cargar la lista de tareas disponibles", Toast.LENGTH_SHORT).show()
                        }
                )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Listener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement Listener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface Listener {
        fun onAvailableTaskClicked(item: Api.Task)
    }

    companion object {
        const val ARG_GROUP_ID = "group-id"

        @JvmStatic
        fun newInstance(groupId: Int) =
                AvailableTasksFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_GROUP_ID, groupId)
                    }
                }
    }
}
