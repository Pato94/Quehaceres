package dadm.frba.utn.edu.ar.quehaceres.fragments.dummy

import java.util.ArrayList
import java.util.HashMap

object Member {

    /**
     * An array of sample MemberItem's.
     */
    val ITEMS: MutableList<MemberItem> = ArrayList()

    /**
     * A map of sample MemberItem's, by ID.
     */
    val ITEM_MAP: MutableMap<String, MemberItem> = HashMap()

    private val COUNT = 25

    init {
        // Add some sample items.
        for (i in 1..COUNT) {
            addItem(createMemberItem(i))
        }
    }


    private fun addItem(item: MemberItem) {
        ITEMS.add(item)
        ITEM_MAP.put(item.id, item)
    }

    //TODO: Replace dummy with actual Member creation
    private fun createMemberItem(position: Int): MemberItem {
        return MemberItem(position.toString(), "Item " + position)
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
     * A Member representing a piece of content.
     */
    data class MemberItem(val id: String, val name: String) {
        override fun toString(): String {
            return name
        }
    }

}