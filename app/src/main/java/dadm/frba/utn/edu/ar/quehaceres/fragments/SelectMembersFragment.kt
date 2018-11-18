package dadm.frba.utn.edu.ar.quehaceres.fragments

import android.content.Context
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import dadm.frba.utn.edu.ar.quehaceres.R

import dadm.frba.utn.edu.ar.quehaceres.fragments.dummy.DummyContent
import dadm.frba.utn.edu.ar.quehaceres.fragments.dummy.DummyContent.DummyItem
import dadm.frba.utn.edu.ar.quehaceres.fragments.dummy.Member
import java.util.*

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [SelectMembersFragment.OnListFragmentInteractionListener] interface.
 */
class SelectMembersFragment : Fragment() {

  private var listener: OnListFragmentInteractionListener? = null
  private val selectedMembers = LinkedList<Member.MemberItem>()

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
    val view = inflater.inflate(R.layout.fragment_members_list, container, false)

    // Set the adapter
    var selectedAdapter: SelectedMembersRecyclerViewAdapter? = null
    with(view.findViewById<RecyclerView>(R.id.selected_list)) {
      selectedAdapter = SelectedMembersRecyclerViewAdapter(selectedMembers) { member ->
        selectedMembers.remove(member)
        selectedAdapter!!.notifyDataSetChanged()
      }
      adapter = selectedAdapter
    }

    with(view.findViewById<RecyclerView>(R.id.list)) {
      adapter = MembersRecyclerViewAdapter(Member.ITEMS) { member ->
        selectedMembers.add(member)
        selectedAdapter!!.notifyDataSetChanged()
      }
    }

    with(view.findViewById<FloatingActionButton>(R.id.next)) {
      setOnClickListener {
        listener?.onMembersSelected(selectedMembers)
      }
    }

    return view
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

  /**
   * This interface must be implemented by activities that contain this
   * fragment to allow an interaction in this fragment to be communicated
   * to the activity and potentially other fragments contained in that
   * activity.
   *
   *
   * See the Android Training lesson
   * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
   * for more information.
   */
  interface OnListFragmentInteractionListener {
    fun onMembersSelected(selected: List<Member.MemberItem>)
  }

  companion object {

    // COMPLETED: Customize parameter argument names
    const val ARG_COLUMN_COUNT = "column-count"

    // COMPLETED: Customize parameter initialization
    @JvmStatic
    fun newInstance(columnCount: Int) =
        SelectMembersFragment().apply {
          arguments = Bundle().apply {
            putInt(ARG_COLUMN_COUNT, columnCount)
          }
        }
  }
}
