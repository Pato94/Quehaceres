package dadm.frba.utn.edu.ar.quehaceres.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dadm.frba.utn.edu.ar.quehaceres.OnGroupUpdated
import dadm.frba.utn.edu.ar.quehaceres.R
import dadm.frba.utn.edu.ar.quehaceres.api.Api
import kotlinx.android.synthetic.main.fragment_group_member_list.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class GroupFragment : Fragment() {

    private var group: Api.Group? = null
    private var eventBus: EventBus = EventBus.getDefault()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            group = it.getParcelable(ARG_GROUP)
        }
        if (group == null) {
            throw IllegalStateException("Group cannot be null")
        }
        eventBus.register(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_group_member_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
    }

    private fun setUpViews() {
        list.adapter = GroupMembersAdapter(group!!.members)
    }

    @Subscribe
    fun onGroupUpdated(event: OnGroupUpdated) {
        group = event.group
    }

    override fun onResume() {
        super.onResume()
        list.adapter = GroupMembersAdapter(group!!.members)
    }

    override fun onDestroy() {
        super.onDestroy()
        eventBus.unregister(this)
    }

    companion object {
        const val ARG_GROUP = "group"

        @JvmStatic
        fun newInstance(group: Api.Group) =
                GroupFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(ARG_GROUP, group)
                    }
                }
    }
}
