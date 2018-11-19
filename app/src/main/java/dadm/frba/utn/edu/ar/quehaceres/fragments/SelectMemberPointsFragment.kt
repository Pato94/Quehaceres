package dadm.frba.utn.edu.ar.quehaceres.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import dadm.frba.utn.edu.ar.quehaceres.R
import dadm.frba.utn.edu.ar.quehaceres.fragments.dummy.MemberPoints
import dadm.frba.utn.edu.ar.quehaceres.models.User
import kotlinx.android.synthetic.main.fragment_member_points_list.*

class SelectMemberPointsFragment : Fragment() {

    private var listener: OnListFragmentInteractionListener? = null
    private lateinit var selectedMembers: ArrayList<Pair<User, Int>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            @Suppress("UNCHECKED_CAST")
            selectedMembers = it.getSerializable(ARG_SELECTED_MEMBERS) as ArrayList<Pair<User, Int>>
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_member_points_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        list.adapter = SelectMemberPointsAdapter(selectedMembers) { user ->
            val dialog = SelectWeeklyPointsDialog(context!!, getCurrentPoints(user)) { setNewPointsForUser(user, it) }
            dialog.show()
        }

        next.setOnClickListener {
            if (group_name.text.toString().isEmpty()) {
                group_name.error = "Elige un nombre para el grupo"
            } else {
                activity?.finish()
            }
        }
    }

    private fun getCurrentPoints(user: User) = selectedMembers.first { it.first == user }.second

    private fun setNewPointsForUser(user: User, newPoints: Int) {
        selectedMembers[selectedMembers.indexOfFirst { it.first == user }] = Pair(user, newPoints)
        list.adapter?.notifyDataSetChanged()
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        activity?.title = "Crear Grupo"
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
        fun onPointsSelected(selected: List<MemberPoints.MemberPointsItem>)
    }

    companion object {
        const val ARG_SELECTED_MEMBERS = "selected-members"

        fun newInstance(selectedMembers: List<User>): SelectMemberPointsFragment {
            val arguments = Bundle()
            val memberPoints = selectedMembers.map { Pair(it, 100) }
            arguments.putSerializable(ARG_SELECTED_MEMBERS, ArrayList(memberPoints))

            val fragment = SelectMemberPointsFragment()
            fragment.arguments = arguments

            return fragment
        }
    }
}
