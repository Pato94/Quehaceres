package dadm.frba.utn.edu.ar.quehaceres.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

fun ViewGroup.inflateChild(layout: Int) =
        LayoutInflater.from(context).inflate(layout, this, false)

open class QueHaceresViewHolder(layout: Int, parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflateChild(layout))
