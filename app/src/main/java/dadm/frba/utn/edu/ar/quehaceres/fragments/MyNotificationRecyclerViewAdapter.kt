package dadm.frba.utn.edu.ar.quehaceres.fragments

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import dadm.frba.utn.edu.ar.quehaceres.R
import dadm.frba.utn.edu.ar.quehaceres.api.Api

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
        holder.producerName.text = item.producer.fullName
        holder.message.text = item.message
        Picasso.get().load(item.producer.avatar).into(holder.avatar)
        if (item.url != null) {
            holder.photo.visibility = View.VISIBLE
            Picasso.get().load(item.url).into(holder.photo)
        } else {
            holder.photo.visibility = View.GONE
        }

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val producerName: TextView = mView.producer_name
        val message: TextView = mView.message
        val avatar: ImageView = mView.avatar
        val photo: ImageView = mView.photo
    }
}
