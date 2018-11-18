package dadm.frba.utn.edu.ar.quehaceres.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import dadm.frba.utn.edu.ar.quehaceres.R
import dadm.frba.utn.edu.ar.quehaceres.models.User
import dadm.frba.utn.edu.ar.quehaceres.services.Services
import kotlinx.android.synthetic.main.fragment_members_list.*
import java.util.*

class SelectMembersFragment : Fragment() {

    private var listener: OnListFragmentInteractionListener? = null
    private val services by lazy { Services(context!!) }
    private val selectedMembers = LinkedList<User>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        activity?.title = "Añadir Participantes"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_members_list, container, false)
    }

    @SuppressLint("CheckResult")
    override fun onResume() {
        super.onResume()

        services.allUsers()
                .subscribe({
                    setUpRecyclerViews(it)
                }, {
                    it.printStackTrace()
                })
    }

    private fun setUpRecyclerViews(allUsers: List<User>) {
        val updateSelectedMembers = { selected_list.adapter?.notifyDataSetChanged() }

        selected_list.adapter = SelectedMembersAdapter(selectedMembers) { member ->
            selectedMembers.remove(member)
            updateSelectedMembers()
        }

        list.adapter = AddMembersAdapter(allUsers) { member ->
            selectedMembers.add(member)
            updateSelectedMembers()
        }

        next.setOnClickListener { listener?.onMembersSelected(selectedMembers) }
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
        fun onMembersSelected(selected: List<User>)
    }

    companion object {
        @JvmStatic
        fun newInstance() = SelectMembersFragment()
    }
}
