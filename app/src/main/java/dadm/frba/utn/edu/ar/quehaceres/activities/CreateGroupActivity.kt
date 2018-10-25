package dadm.frba.utn.edu.ar.quehaceres.activities

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import dadm.frba.utn.edu.ar.quehaceres.R
import dadm.frba.utn.edu.ar.quehaceres.fragments.SelectMembersFragment
import dadm.frba.utn.edu.ar.quehaceres.fragments.dummy.DummyContent

class CreateGroupActivity : AppCompatActivity(), SelectMembersFragment.OnListFragmentInteractionListener {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.create_group_activity)
    if (savedInstanceState == null) {
      supportFragmentManager.beginTransaction()
          .replace(R.id.container, SelectMembersFragment.newInstance(1))
          .commitNow()
    }
  }

  override fun onListFragmentInteraction(item: DummyContent.DummyItem?) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun onMembersSelected(selected: List<DummyContent.DummyItem>) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  companion object {
    fun newIntent(context: Context): Intent {
      return Intent(context, CreateGroupActivity::class.java)
    }
  }
}
