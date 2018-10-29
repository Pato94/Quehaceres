package dadm.frba.utn.edu.ar.quehaceres.fragments

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import dadm.frba.utn.edu.ar.quehaceres.R


import dadm.frba.utn.edu.ar.quehaceres.fragments.SelectMembersFragment.OnListFragmentInteractionListener
import dadm.frba.utn.edu.ar.quehaceres.fragments.dummy.DummyContent.DummyItem

import kotlinx.android.synthetic.main.fragment_members.view.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class SelectedMembersRecyclerViewAdapter(
    private val mValues: List<DummyItem>,
    private val mListener: (DummyItem) -> Unit)
  : RecyclerView.Adapter<SelectedMembersRecyclerViewAdapter.ViewHolder>() {

  private val mOnClickListener: View.OnClickListener

  init {
    mOnClickListener = View.OnClickListener { v ->
      val item = v.tag as DummyItem
      // Notify the active callbacks interface (the activity, if the fragment is attached to
      // one) that an item has been selected.
      mListener(item)
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.context)
        .inflate(R.layout.selected_fragment_members, parent, false)
    return ViewHolder(view)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val item = mValues[position]
    holder.mContentView.text = item.content

    with(holder.mView) {
      tag = item
      setOnClickListener(mOnClickListener)
    }
  }

  override fun getItemCount(): Int = mValues.size

  inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
    val mContentView: TextView = mView.content

    override fun toString(): String {
      return super.toString() + " '" + mContentView.text + "'"
    }
  }
}