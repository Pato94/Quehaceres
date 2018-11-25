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
            addItem(createNotificationItem("headline "+i.toString(),"action "+i.toString(),"description "+i.toString()))
        }
    }


    private fun addItem(item: NotificationItem) {
        ITEMS.add(item)
        ITEM_MAP.put(item.id, item)
    }

    //TODO: Replace or remove id
    private fun createNotificationItem(headline: String, action: String, description: String): NotificationItem {
        return NotificationItem("0", headline, action, description)
    }

    //TODO: Create a createNotificationItem function from an AvaliableTask as input

    /**
     * A Notification representing a piece of content.
     */
    data class NotificationItem(val id: String, val task_headline: String,val task_action: String, val task_description: String) {
        override fun toString(): String {
            return task_headline
        }
    }

}