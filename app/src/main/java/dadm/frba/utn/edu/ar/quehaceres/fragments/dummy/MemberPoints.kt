package dadm.frba.utn.edu.ar.quehaceres.fragments.dummy


import android.content.res.Resources
import dadm.frba.utn.edu.ar.quehaceres.R
import java.util.ArrayList
import java.util.HashMap



object MemberPoints {

    /**
     * An array of sample MemberPointItem's.
     */
    val ITEMS: MutableList<MemberPointsItem> = ArrayList()

    /**
     * A map of sample MemberPointItem's, by ID.
     */
    val ITEM_MAP: MutableMap<String, MemberPointsItem> = HashMap()

    private val COUNT = 25

    init {
        // Add some sample items.
        for (i in 1..COUNT) {
            addItem(createMemberPointsItem(i))
        }
    }


    private fun addItem(item: MemberPointsItem) {
        ITEMS.add(item)
        ITEM_MAP.put(item.id, item)
    }

    //TODO: Replace o remove Id
    private fun createMemberPointsItem(position: Int, name: String, points: Int): MemberPointsItem {
        return MemberPointsItem(position.toString(), name, points.toString())
    }

    /**
     * A MemberPoint representing a piece of content.
     */
    data class MemberPointsItem(val id: String, val name: String, val weeklyPoints: String) {
        override fun toString(): String {
            return name + Resources.getSystem().getString(R.string.member_points_qty) + weeklyPoints + Resources.getSystem().getString(R.string.points)
        }
    }
}