package dadm.frba.utn.edu.ar.quehaceres.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dadm.frba.utn.edu.ar.quehaceres.R

import dadm.frba.utn.edu.ar.quehaceres.fragments.dummy.DummyContent
import dadm.frba.utn.edu.ar.quehaceres.fragments.dummy.DummyContent.DummyItem

class SelectMemberPointsFragment : Fragment() {

  private var listener: OnListFragmentInteractionListener? = null
  private lateinit var selectedMembers: List<DummyItem>

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    arguments?.let {
      @Suppress("UNCHECKED_CAST")
      selectedMembers = it.getSerializable(ARG_SELECTED_MEMBERS) as ArrayList<DummyItem>
    }
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.fragment_member_points_list, container, false)

    // Set the adapter
    if (view is RecyclerView) {
      with(view) {
        layoutManager = LinearLayoutManager(context)
        adapter = MemberPointsRecyclerViewAdapter(selectedMembers, listener)
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

  interface OnListFragmentInteractionListener {
    fun onPointsSelected(selected: List<DummyItem>)
  }

  companion object {

    const val ARG_SELECTED_MEMBERS = "selected-members"

    // TODO: Customize parameter initialization
    @JvmStatic
    fun newInstance(selectedMembers: List<DummyItem>) =
        SelectMemberPointsFragment().apply {
          arguments = Bundle().apply {
            putSerializable(ARG_SELECTED_MEMBERS, ArrayList(selectedMembers))
          }
        }
  }
}