package dadm.frba.utn.edu.ar.quehaceres.activities

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import dadm.frba.utn.edu.ar.quehaceres.R
import dadm.frba.utn.edu.ar.quehaceres.fragments.SelectMemberPointsFragment
import dadm.frba.utn.edu.ar.quehaceres.fragments.SelectMembersFragment
import dadm.frba.utn.edu.ar.quehaceres.fragments.dummy.Member
import dadm.frba.utn.edu.ar.quehaceres.fragments.dummy.MemberPoints
import dadm.frba.utn.edu.ar.quehaceres.models.User

class CreateGroupActivity : AppCompatActivity(), SelectMembersFragment.OnListFragmentInteractionListener, SelectMemberPointsFragment.OnListFragmentInteractionListener {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.create_group_activity)
    if (savedInstanceState == null) {
      supportFragmentManager.beginTransaction()
          .replace(R.id.container, SelectMembersFragment.newInstance())
          .commitNow()
    }
  }

  override fun onPointsSelected(selected: List<MemberPoints.MemberPointsItem>) {
  }

  override fun onMembersSelected(selected: List<User>) {
    supportFragmentManager.beginTransaction()
        .replace(R.id.container, SelectMemberPointsFragment.newInstance(Member.ITEMS))
        .commitNow()
  }

  companion object {
    fun newIntent(context: Context): Intent {
      return Intent(context, CreateGroupActivity::class.java)
    }
  }
}
