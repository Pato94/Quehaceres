package dadm.frba.utn.edu.ar.quehaceres.fragments

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import dadm.frba.utn.edu.ar.quehaceres.R


import dadm.frba.utn.edu.ar.quehaceres.fragments.MyTasksFragment.Listener
import dadm.frba.utn.edu.ar.quehaceres.fragments.dummy.AvailableTask

import kotlinx.android.synthetic.main.fragment_availabletasks.view.*

/**
 * [RecyclerView.Adapter] that can display an [AvailableTaskItem] and makes a call to the
 * specified [Listener].
 * TODO: Replace the implementation with code for your data type.
 */
class MyTasksAdapter(
    private val mValues: List<AvailableTask.AvailableTaskItem>,
    private val mListener: Listener?)
  : RecyclerView.Adapter<MyTasksAdapter.ViewHolder>() {

  private val mOnClickListener: View.OnClickListener

  init {
    mOnClickListener = View.OnClickListener { v ->
      val item = v.tag as AvailableTask.AvailableTaskItem
      // Notify the active callbacks interface (the activity, if the fragment is attached to
      // one) that an item has been selected.
      mListener?.onMyTaskClicked(item)
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.context)
        .inflate(R.layout.fragment_availabletasks, parent, false)
    return ViewHolder(view)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val item = mValues[position]
    holder.mCoinsView.text = item.coins
    holder.mTaskView.text = item.task

    with(holder.mView) {
      tag = item
      setOnClickListener(mOnClickListener)
    }
  }

  override fun getItemCount(): Int = mValues.size

  inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
    val mCoinsView: TextView = mView.tv_coins
    val mTaskView: TextView = mView.tv_task

    override fun toString(): String {
      return super.toString() + " '" + mTaskView.text + "'"
    }
  }
}
