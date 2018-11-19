package dadm.frba.utn.edu.ar.quehaceres.activities

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
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

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, SelectMembersFragment.newInstance())
                    .commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPointsSelected(selected: List<MemberPoints.MemberPointsItem>) {
    }

    override fun onMembersSelected(selected: List<User>) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, SelectMemberPointsFragment.newInstance(selected))
                .addToBackStack(null)
                .commit()
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, CreateGroupActivity::class.java)
        }
    }
}
