package dadm.frba.utn.edu.ar.quehaceres.fragments.dummy

import java.util.ArrayList
import java.util.HashMap

object Notification {
    /**
     * An array of sample NotificationItem's.
     */
    val ITEMS: MutableList<NotificationItem> = ArrayList()

    /**
     * A map of sample NotificationItem's, by ID.
     */
    val ITEM_MAP: MutableMap<String, NotificationItem> = HashMap()

    private val COUNT = 25

    init {
        // Add some sample items.
        for (i in 1..COUNT) {
            addItem(createNotificationItem(i))
        }
    }


    private fun addItem(item: NotificationItem) {
        ITEMS.add(item)
        ITEM_MAP.put(item.id, item)
    }

    //TODO: Replace dummy with actual Notification creation
    private fun createNotificationItem(position: Int): NotificationItem {
        return NotificationItem(position.toString(), "Item " + position, "dummy Action","dummy description")
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
     * A Notification representing a piece of content.
     */
    data class NotificationItem(val id: String, val task_headline: String,val task_action: String, val task_description: String) {
        override fun toString(): String {
            return task_headline
        }
    }

}