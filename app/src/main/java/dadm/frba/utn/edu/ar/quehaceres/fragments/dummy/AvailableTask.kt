package dadm.frba.utn.edu.ar.quehaceres.fragments.dummy

import android.content.res.Resources
import dadm.frba.utn.edu.ar.quehaceres.R
import java.util.ArrayList
import java.util.HashMap

object AvailableTask {


    /**
     * An array of sample AvailableTaskItem's.
     */
    val ITEMS: MutableList<AvailableTaskItem> = ArrayList()

    /**
     * A map of sample AvailableTaskItem's, by ID.
     */
    val ITEM_MAP: MutableMap<String, AvailableTaskItem> = HashMap()

    private val COUNT = 25

    init {
        // Add some sample items.
        for (i in 1..COUNT) {
            addItem(createAvailableTaskItem(i, "task "+i.toString(),i.toString()))
        }
    }


    private fun addItem(item: AvailableTaskItem) {
        ITEMS.add(item)
        ITEM_MAP.put(item.id, item)
    }

    //TODO: Replace or remove ID
    private fun createAvailableTaskItem(position: Int, task_name: String, coins: String): AvailableTaskItem {
        return AvailableTaskItem(position.toString(), task_name, coins)
    }

    /**
     * An AvailableTask representing a piece of content.
     */
    data class AvailableTaskItem(val id: String, val task: String, val coins: String) {
        override fun toString(): String {
            return task + Resources.getSystem().getString(R.string.costs) + coins
        }
    }

}