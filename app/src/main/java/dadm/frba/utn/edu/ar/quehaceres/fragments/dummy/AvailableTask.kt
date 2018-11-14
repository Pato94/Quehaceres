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
            addItem(createDummyItem(i))
        }
    }


    private fun addItem(item: AvailableTaskItem) {
        ITEMS.add(item)
        ITEM_MAP.put(item.id, item)
    }

    private fun createDummyItem(position: Int): AvailableTaskItem {
        return AvailableTaskItem(position.toString(), "Item " + position, makeDetails(position))
    }

    private fun makeDetails(position: Int): String {
        val builder = StringBuilder()
        builder.append("Details about Item: ").append(position)
        for (i in 0..position - 1) {
            builder.append("\nMore details information here.")
        }
        return builder.toString()
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