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
import dadm.frba.utn.edu.ar.quehaceres.fragments.MyTasksFragment.Listener
import kotlinx.android.synthetic.main.list_item_my_tasks.view.*

class MyTasksAdapter(
    private val mValues: List<Api.Task>,
    private val mListener: Listener?)
  : RecyclerView.Adapter<MyTasksAdapter.ViewHolder>() {

  private val mOnClickListener: View.OnClickListener

  init {
    mOnClickListener = View.OnClickListener { v ->
      val item = v.tag as Api.Task
      mListener?.onMyTaskClicked(item)
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.context)
        .inflate(R.layout.list_item_my_tasks, parent, false)
    return ViewHolder(view)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val item = mValues[position]

    holder.avatar.hierarchy.setProgressBarImage(CircularProgressDrawable(holder.itemView.context))
    holder.avatar.setImageURI(item.createdBy.avatar)
    holder.producerName.text = "Creada por ${item.createdBy.fullName}"
    holder.reward.text = "Recompensa: ${item.reward}"
    holder.title.text = item.name

    val toValidate = item.status == "to_validate"
    holder.overlay.visibility = if (toValidate) View.VISIBLE else View.GONE

    with(holder.mView) {
      tag = item
      setOnClickListener(if (toValidate) null else mOnClickListener)
    }
  }

  override fun getItemCount(): Int = mValues.size

  inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
    val avatar: SimpleDraweeView = mView.avatar
    var producerName: TextView = mView.producer_name
    var reward: TextView = mView.reward
    var title: TextView = mView.title
    val overlay: View = mView.disabled_overlay
  }
}
