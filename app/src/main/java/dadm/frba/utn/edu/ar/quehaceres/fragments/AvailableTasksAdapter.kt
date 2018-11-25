package dadm.frba.utn.edu.ar.quehaceres.fragments

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import dadm.frba.utn.edu.ar.quehaceres.R
import dadm.frba.utn.edu.ar.quehaceres.api.Api
import dadm.frba.utn.edu.ar.quehaceres.fragments.AvailableTasksFragment.Listener

class AvailableTasksAdapter(
        private val mValues: List<Api.Task>,
        private val mListener: Listener?)
    : RecyclerView.Adapter<AvailableTasksAdapter.AvailableTaskViewHolder>() {

    private val mOnClickListener: View.OnClickListener
    private var items: Int = 1

    fun AvailableTasksAdapter(numberOfItems: Int){
        this.items = numberOfItems
    }

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Api.Task
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
//        holder.mCoinsView.text = item.coins
        holder.mTaskView.text = item.name

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
