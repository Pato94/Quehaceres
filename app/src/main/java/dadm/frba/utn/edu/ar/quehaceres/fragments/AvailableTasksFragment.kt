package dadm.frba.utn.edu.ar.quehaceres.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.common.util.CollectionUtils.isEmpty
import dadm.frba.utn.edu.ar.quehaceres.OnTaskAssigned
import dadm.frba.utn.edu.ar.quehaceres.OnTaskCreated
import dadm.frba.utn.edu.ar.quehaceres.R
import dadm.frba.utn.edu.ar.quehaceres.api.Api
import dadm.frba.utn.edu.ar.quehaceres.services.Services
import kotlinx.android.synthetic.main.fragment_available_tasks.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.lang.IllegalStateException

class AvailableTasksFragment : Fragment() {

    private val services by lazy { Services(context!!) }
    private var listener: Listener? = null
    private var groupId: Int? = null
    private val eventBus = EventBus.getDefault()

//    val taskTrucha: Api.Task = Api.Task(id = 200, name = "Esta tarea no es de verdad. Aca habria una tarea si hubieras creado una. Toca el icono con el simbolo '+'", )


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
        return inflater.inflate(R.layout.fragment_available_tasks, container, false)
    }

    override fun onResume() {
        super.onResume()
        fetchTasks()
    }

    @SuppressLint("CheckResult")
    private fun fetchTasks() {
        services.availableTasks(groupId!!)
                .doOnSubscribe {
                    loading.visibility = View.VISIBLE
                    list.visibility = View.GONE
                }
                .subscribe(
                        {
                            loading.visibility = View.GONE
                            list.visibility = View.VISIBLE

                            if (isEmpty(it)) {
                                list.visibility = View.GONE
                                empty_state.visibility = View.VISIBLE
                            } else {
                                list.adapter = AvailableTasksAdapter(it, listener)
                            }
                        },
                        {
                            loading.visibility = View.GONE
                            list.visibility = View.VISIBLE
                            Toast.makeText(context!!, "Hubo un error al cargar la lista de tareas disponibles", Toast.LENGTH_SHORT).show()
                        }
                )
    }

    @Subscribe
    fun onTaskCreated(event: OnTaskCreated) {
        fetchTasks()
    }

    @Subscribe
    fun onTaskAssigned(event: OnTaskAssigned) {
        fetchTasks()
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

    override fun onStart() {
        super.onStart()
        eventBus.register(this)
    }

    override fun onStop() {
        super.onStop()
        eventBus.unregister(this)
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
