package dadm.frba.utn.edu.ar.quehaceres.fragments.dummy

import android.provider.Settings.Global.getString
import dadm.frba.utn.edu.ar.quehaceres.R
import java.lang.reflect.Member
import java.util.ArrayList
import java.util.HashMap

object MemberPoints {

    /**
     * An array of sample (dummy) items.
     */
    val ITEMS: MutableList<dadm.frba.utn.edu.ar.quehaceres.fragments.dummy.MemberPoints> = ArrayList()

    /**
     * A map of sample (dummy) items, by ID.
     */
    val ITEM_MAP: MutableMap<String, dadm.frba.utn.edu.ar.quehaceres.fragments.dummy.MemberPoints> = HashMap()

    private val COUNT = 25

    init {
        // Add some sample items.
        for (i in 1..COUNT) {
            addItem(createDummyItem(i))
        }
    }

    private val id: String = "0"

    private fun addItem(item: dadm.frba.utn.edu.ar.quehaceres.fragments.dummy.MemberPoints) {
        ITEMS.add(item)
        ITEM_MAP.put(item.id, item)
    }

    private fun createDummyItem(position: Int): dadm.frba.utn.edu.ar.quehaceres.fragments.dummy.MemberPoints {
        return dadm.frba.utn.edu.ar.quehaceres.fragments.dummy.MemberPoints(position.toString(), "Item " + position, makeDetails(position))
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
     * A dummy item representing a piece of content.
     */
    data class MemberPoints(val id: String, val name: String, val weeklyPoints: String) {
        override fun toString(): String = name + getString(R.string.member_points_qty) + weeklyPoints + getString(R.string.points)
    }

}