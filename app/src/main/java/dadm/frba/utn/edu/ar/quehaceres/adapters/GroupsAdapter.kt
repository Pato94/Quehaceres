package dadm.frba.utn.edu.ar.quehaceres.adapters

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import dadm.frba.utn.edu.ar.quehaceres.R
import dadm.frba.utn.edu.ar.quehaceres.api.Api
import kotlinx.android.synthetic.main.list_item_group.view.*

class GroupsAdapter(val listener: (Api.Group) -> Unit): RecyclerView.Adapter<GroupsAdapter.ViewHolder>() {
    var groups: List<Api.Group>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent, listener)
    }

    override fun getItemCount(): Int {
        return groups?.size ?: 0
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(groups!![position])
    }

    class ViewHolder(parent: ViewGroup, listener: (Api.Group) -> Unit): QueHaceresViewHolder(R.layout.list_item_group, parent) {
        var group: Api.Group? = null

        init {
            itemView.setOnClickListener { group?.let { listener.invoke(it) }}
        }

        fun bind(group: Api.Group) {
            this.group = group
            itemView.group_name.text = group.name
//            itemView.last_message.text = group.lastAction
        }
    }
}
