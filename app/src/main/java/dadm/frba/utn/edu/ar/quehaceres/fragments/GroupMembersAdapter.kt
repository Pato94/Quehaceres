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
import kotlinx.android.synthetic.main.list_item_group_member.view.*

class GroupMembersAdapter(
        private val mValues: List<Api.Member>)
    : RecyclerView.Adapter<GroupMembersAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_group_member, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]

        holder.avatar.hierarchy.setProgressBarImage(CircularProgressDrawable(holder.itemView.context))
        holder.avatar.setImageURI(item.actualUser.avatar)
        holder.username.text = item.actualUser.email
        holder.fullName.text = item.actualUser.fullName
        holder.points.text = "Puntos: ${item.points}"
    }

    override fun getItemCount(): Int = mValues.size

    class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val avatar: SimpleDraweeView = mView.avatar
        val username: TextView = mView.username
        val fullName: TextView = mView.full_name
        val points: TextView = mView.points
    }
}
