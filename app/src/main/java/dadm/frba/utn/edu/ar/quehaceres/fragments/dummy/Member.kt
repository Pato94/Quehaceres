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
            addItem(createMemberItem(i, "Member "+i.toString()))
        }
    }

    private fun addItem(item: MemberItem) {
        ITEMS.add(item)
        ITEM_MAP.put(item.id, item)
    }

    //TODO: Replace or remove id
    private fun createMemberItem(position: Int, name: String): MemberItem {
        return MemberItem(position.toString(), name)
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