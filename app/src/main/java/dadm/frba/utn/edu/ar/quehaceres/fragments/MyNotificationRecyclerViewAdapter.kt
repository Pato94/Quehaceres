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
import dadm.frba.utn.edu.ar.quehaceres.models.User

import kotlinx.android.synthetic.main.fragment_notification.view.*

class MyNotificationRecyclerViewAdapter(
        private val mValues: List<Api.Notification>,
        private val mListener: (Api.Notification) -> Unit)
    : RecyclerView.Adapter<MyNotificationRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Api.Notification
            mListener(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_notification, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        val producer = User(item.producer)
        holder.producerName.text = producer.fullName
        holder.message.text = item.message
        holder.avatar.hierarchy.setProgressBarImage(CircularProgressDrawable(holder.itemView.context))
        holder.avatar.setImageURI(producer.avatar)
        if (item.url != null) {
            holder.photo.visibility = View.VISIBLE
            holder.photo.hierarchy.setProgressBarImage(CircularProgressDrawable(holder.itemView.context))
            holder.photo.setImageURI(item.url)
        } else {
            holder.photo.visibility = View.GONE
        }

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }

        if (item.type == "VERIFICATION") {
            if (item.status == "to_validate") {
                holder.action.text = "VALIDAR"
            } else {
                holder.action.text = "VALIDADA"
            }
            holder.action.visibility = View.VISIBLE
        } else {
            holder.action.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val producerName: TextView = mView.producer_name
        val message: TextView = mView.message
        val avatar: SimpleDraweeView = mView.avatar
        val photo: SimpleDraweeView = mView.photo
        val action: TextView = mView.action
    }
}
