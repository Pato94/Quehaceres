package dadm.frba.utn.edu.ar.quehaceres.fragments

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import dadm.frba.utn.edu.ar.quehaceres.R

import dadm.frba.utn.edu.ar.quehaceres.fragments.SelectMembersFragment.OnListFragmentInteractionListener
import dadm.frba.utn.edu.ar.quehaceres.fragments.dummy.DummyContent.DummyItem
import dadm.frba.utn.edu.ar.quehaceres.fragments.dummy.Member

import kotlinx.android.synthetic.main.fragment_members.view.*

/**
 * [RecyclerView.Adapter] that can display a Members and makes a call to the
 * specified [MembersListener].
 * TODO: Replace the implementation with code for your data type.
 */
class MembersRecyclerViewAdapter(
    private val mValues: List<Member.MemberItem>,
    private val mListener: (Member.MemberItem) -> Unit)
  : RecyclerView.Adapter<MembersRecyclerViewAdapter.ViewHolder>() {

  private val mOnClickListener: View.OnClickListener

  init {
    mOnClickListener = View.OnClickListener { v ->
      val item = v.tag as Member.MemberItem
      // Notify the active callbacks interface (the activity, if the fragment is attached to
      // one) that an item has been selected.
      mListener(item)
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.context)
        .inflate(R.layout.fragment_members, parent, false)
    return ViewHolder(view)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val item = mValues[position]
    holder.mMemberNameView.text = item.name

    with(holder.mView) {
      tag = item
      setOnClickListener(mOnClickListener)
    }
  }

  override fun getItemCount(): Int = mValues.size

  inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
    val mMemberNameView: TextView = mView.tv_member_name

    override fun toString(): String {
      return super.toString() + " '" + mMemberNameView.text + "'"
    }
  }
}
