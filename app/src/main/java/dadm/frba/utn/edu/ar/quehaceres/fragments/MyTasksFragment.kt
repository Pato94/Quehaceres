package dadm.frba.utn.edu.ar.quehaceres.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dadm.frba.utn.edu.ar.quehaceres.R
import dadm.frba.utn.edu.ar.quehaceres.fragments.dummy.AvailableTask

import dadm.frba.utn.edu.ar.quehaceres.fragments.dummy.DummyContent

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [MyTasksFragment.OnListFragmentInteractionListener] interface.
 */
class MyTasksFragment : Fragment() {

  // TODO: Customize parameters
  private var columnCount = 1

  private var listener: MyTasksFragment.Listener? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    arguments?.let {
      columnCount = it.getInt(ARG_COLUMN_COUNT)
    }
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.fragment_availabletasks_list, container, false)

    // Set the adapter
    if (view is RecyclerView) {
      with(view) {
        layoutManager = LinearLayoutManager(context)
        adapter = MyTasksAdapter(AvailableTask.ITEMS, listener)
      }
    }
    return view
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

  interface Listener {
    fun onMyTaskClicked(item: AvailableTask.AvailableTaskItem?)
  }

  companion object {

    // TODO: Customize parameter argument names
    const val ARG_COLUMN_COUNT = "column-count"

    // TODO: Customize parameter initialization
    @JvmStatic
    fun newInstance(columnCount: Int) =
        MyTasksFragment().apply {
          arguments = Bundle().apply {
            putInt(ARG_COLUMN_COUNT, columnCount)
          }
        }
  }
}
