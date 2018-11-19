package dadm.frba.utn.edu.ar.quehaceres.fragments

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import dadm.frba.utn.edu.ar.quehaceres.R

import dadm.frba.utn.edu.ar.quehaceres.models.User
import kotlinx.android.synthetic.main.fragment_member_points.view.*

class MemberPointsRecyclerViewAdapter(
        private val mValues: List<Pair<User, Int>>,
        private val mListener: (User) -> Unit)
  : RecyclerView.Adapter<MemberPointsRecyclerViewAdapter.ViewHolder>() {

  private val mOnClickListener: View.OnClickListener

  init {
    mOnClickListener = View.OnClickListener { v ->
      val item = v.tag as Pair<User, Int>
      mListener(item.first)
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.context)
        .inflate(R.layout.fragment_member_points, parent, false)
    return ViewHolder(view)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val item = mValues[position]
    holder.mMemberNameView.text = item.first.fullName
    holder.mPointsView.text = item.second.toString()

    with(holder.mView) {
      tag = item
      setOnClickListener(mOnClickListener)
    }
  }

  override fun getItemCount(): Int = mValues.size

  inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
    val mMemberNameView: TextView = mView.full_name
    val mPointsView: TextView = mView.tv_member_points
  }
}
