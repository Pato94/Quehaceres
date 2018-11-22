package dadm.frba.utn.edu.ar.quehaceres.fragments

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import dadm.frba.utn.edu.ar.quehaceres.R


import dadm.frba.utn.edu.ar.quehaceres.fragments.AvailableTasksFragment.Listener
import dadm.frba.utn.edu.ar.quehaceres.fragments.dummy.AvailableTask

/**
 * [RecyclerView.Adapter] that can display an [AvailableTask] and makes a call to the
 * specified [AvailableTasksListener].
 *
 */
class AvailableTasksAdapter(
        private val mValues: List<AvailableTask.AvailableTaskItem>,
        private val mListener: Listener?)
    : RecyclerView.Adapter<AvailableTasksAdapter.AvailableTaskViewHolder>() {

    private val mOnClickListener: View.OnClickListener
    private var items: Int = 1

    fun AvailableTasksAdapter(numberOfItems: Int){
        this.items = numberOfItems
    }

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as AvailableTask.AvailableTaskItem
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onAvailableTaskClicked(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvailableTaskViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_availabletasks, parent, false)
        return AvailableTaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: AvailableTaskViewHolder, position: Int) {
        val item = mValues[position]
        holder.mCoinsView.text = item.coins
        holder.mTaskView.text = item.task

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class AvailableTaskViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mCoinsView: TextView = mView.findViewById(R.id.tv_coins)
        var mTaskView: TextView = mView.findViewById(R.id.tv_task)

        fun AvailableTaskViewHolder (task: View){
            mTaskView =  task.findViewById(R.id.tv_task)
        }

        override fun toString(): String {
            return super.toString() + " '" + mTaskView.text + "'"
        }
    }
}
