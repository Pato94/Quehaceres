package dadm.frba.utn.edu.ar.quehaceres.fragments

import android.support.v4.widget.CircularProgressDrawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.facebook.drawee.view.SimpleDraweeView
import dadm.frba.utn.edu.ar.quehaceres.R

import dadm.frba.utn.edu.ar.quehaceres.models.User
import kotlinx.android.synthetic.main.list_item_member_points.view.*

class SelectMemberPointsAdapter(
        private val mValues: List<SelectMemberPointsFragment.IdAndPoints>,
        private val mListener: (User) -> Unit)
    : RecyclerView.Adapter<SelectMemberPointsAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as SelectMemberPointsFragment.IdAndPoints
            mListener(item.user)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_member_points, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.avatar.hierarchy.setProgressBarImage(CircularProgressDrawable(holder.avatar.context))
        holder.avatar.setImageURI(item.user.avatar)
        holder.fullName.text = item.user.fullName
        holder.weeklyPoints.text = "Puntos iniciales: ${item.points}"

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val avatar: SimpleDraweeView = mView.avatar
        val fullName: TextView = mView.full_name
        val weeklyPoints: TextView = mView.weekly_points
    }
}
