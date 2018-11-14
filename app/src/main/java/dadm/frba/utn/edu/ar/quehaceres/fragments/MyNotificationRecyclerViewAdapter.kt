package dadm.frba.utn.edu.ar.quehaceres.fragments

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import dadm.frba.utn.edu.ar.quehaceres.R


import dadm.frba.utn.edu.ar.quehaceres.fragments.NotificationsFragment.OnListFragmentInteractionListener
import dadm.frba.utn.edu.ar.quehaceres.fragments.dummy.DummyContent.DummyItem
import dadm.frba.utn.edu.ar.quehaceres.fragments.dummy.Notification

import kotlinx.android.synthetic.main.fragment_notification.view.*

/**
 * [RecyclerView.Adapter] that can display a NotificationItem and makes a call to the
 * specified [NotificationListener].
 */
class MyNotificationRecyclerViewAdapter(
        private val mValues: List<Notification.NotificationItem>,
        private val mListener: OnListFragmentInteractionListener?)  //TODO: create NotificationListener
    : RecyclerView.Adapter<MyNotificationRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Notification.NotificationItem
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_notification, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mTaskActionView.text = item.task_action
        holder.mTaskDescriptionView.text = item.task_description
        holder.mTaskHeadlineView.text = item.task_headline

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mTaskActionView: TextView = mView.tv_task_action
        val mTaskDescriptionView: TextView = mView.tv_task_description
        val mTaskHeadlineView: TextView = mView.tv_task_headline

        override fun toString(): String {
            return super.toString() + " '" + mTaskHeadlineView.text + "'"
        }
    }
}
