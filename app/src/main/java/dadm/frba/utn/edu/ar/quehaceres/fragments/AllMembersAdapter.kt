package dadm.frba.utn.edu.ar.quehaceres.fragments

import android.support.v4.widget.CircularProgressDrawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.facebook.drawee.view.SimpleDraweeView
import dadm.frba.utn.edu.ar.quehaceres.R

import dadm.frba.utn.edu.ar.quehaceres.models.User

import kotlinx.android.synthetic.main.list_item_member.view.*

class AllMembersAdapter(
        private val mValues: List<User>,
        private val mSelectedValues: List<User>,
        private val mListener: (User) -> Unit)
    : RecyclerView.Adapter<AllMembersAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as User
            mListener(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_member, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]

        holder.avatar.hierarchy.setProgressBarImage(CircularProgressDrawable(holder.itemView.context))
        holder.avatar.setImageURI(item.avatar)
        holder.username.text = item.email
        holder.fullName.text = item.fullName

        if (mSelectedValues.contains(item)) {
            holder.added.visibility = View.VISIBLE
        } else {
            holder.added.visibility = View.GONE
        }

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val avatar: SimpleDraweeView = mView.avatar
        val username: TextView = mView.username
        val fullName: TextView = mView.full_name
        val added: ImageView = mView.added
    }
}
