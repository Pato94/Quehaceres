package dadm.frba.utn.edu.ar.quehaceres.adapters

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import dadm.frba.utn.edu.ar.quehaceres.R
import dadm.frba.utn.edu.ar.quehaceres.models.Group
import kotlinx.android.synthetic.main.list_item_group.view.*

class GroupsAdapter: RecyclerView.Adapter<GroupsAdapter.ViewHolder>() {
    var groups: List<Group>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return groups?.size ?: 0
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(groups!![position])
    }

    class ViewHolder(parent: ViewGroup): QueHaceresViewHolder(R.layout.list_item_group, parent) {
        fun bind(group: Group) {
            itemView.group_name.text = group.name
            itemView.last_message.text = group.lastAction
        }
    }
}
