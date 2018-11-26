package dadm.frba.utn.edu.ar.quehaceres.fragments

import android.support.v4.widget.CircularProgressDrawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.facebook.drawee.view.SimpleDraweeView
import dadm.frba.utn.edu.ar.quehaceres.R
import dadm.frba.utn.edu.ar.quehaceres.api.Api
import dadm.frba.utn.edu.ar.quehaceres.fragments.AvailableTasksFragment.Listener
import kotlinx.android.synthetic.main.fragment_availabletasks.view.*

class AvailableTasksAdapter(
        private val mValues: List<Api.Task>,
        private val mListener: Listener?)
    : RecyclerView.Adapter<AvailableTasksAdapter.AvailableTaskViewHolder>() {

    private val mOnClickListener: View.OnClickListener

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
        holder.avatar.hierarchy.setProgressBarImage(CircularProgressDrawable(holder.itemView.context))
        holder.avatar.setImageURI(item.createdBy.avatar)
        holder.producerName.text = "Creada por ${item.createdBy.fullName}"
        holder.reward.text = "Recompensa: ${item.reward}"
        holder.title.text = item.name

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    class AvailableTaskViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val avatar: SimpleDraweeView = mView.avatar
        var producerName: TextView = mView.producer_name
        var reward: TextView = mView.reward
        var title: TextView = mView.title
    }
}
