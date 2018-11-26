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
import dadm.frba.utn.edu.ar.quehaceres.OnTaskVerified
import dadm.frba.utn.edu.ar.quehaceres.R
import dadm.frba.utn.edu.ar.quehaceres.api.Api
import dadm.frba.utn.edu.ar.quehaceres.services.Services
import kotlinx.android.synthetic.main.fragment_tasks_list.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class MyTasksFragment : BaseFragment() {

    private val services by lazy { Services(context!!) }
    private var listener: MyTasksFragment.Listener? = null
    private var groupId: Int? = null
    private var eventBus: EventBus = EventBus.getDefault()

//    val taskTrucha: Api.Task = Api.Task(id = 100, name = "Esta tarea no es de verdad. Aca habria una tarea si te hubieras asignado una. Elegi una tarea disponible y asignatela.")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            groupId = it.getInt(ARG_GROUP_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tasks_list, container, false)
    }

    override fun onResume() {
        super.onResume()
        loadMyTasks()
    }

    @SuppressLint("CheckResult")
    private fun loadMyTasks() {
        compositeSubscription.add(
        services.myTasks(groupId!!)
                .doOnSubscribe {
                    loading.visibility = View.VISIBLE
                    list.visibility = View.GONE
                    empty_state.visibility = View.GONE
                }
                .subscribe(
                        {
                            loading.visibility = View.GONE
                            list.visibility = View.VISIBLE

                            if (isEmpty(it)) {
                                list.visibility = View.GONE
                                empty_state.visibility = View.VISIBLE
                            } else {
                                list.adapter = MyTasksAdapter(it, listener)
                            }
                        },
                        {
                            loading.visibility = View.GONE
                            list.visibility = View.VISIBLE
                            Toast.makeText(context!!, "Hubo un error al cargar mis tareas", Toast.LENGTH_SHORT).show()
                        }
                )
        )
    }

    @Subscribe
    fun onTaskAssigned(event: OnTaskAssigned) {
        loadMyTasks()
    }

    @Subscribe
    fun onTaskVerified(event: OnTaskVerified) {
        loadMyTasks()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MyTasksFragment.Listener) {
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
        fun onMyTaskClicked(item: Api.Task)
    }

    companion object {
        const val ARG_GROUP_ID = "group-id"

        @JvmStatic
        fun newInstance(groupId: Int) =
                MyTasksFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_GROUP_ID, groupId)
                    }
                }
    }
}
