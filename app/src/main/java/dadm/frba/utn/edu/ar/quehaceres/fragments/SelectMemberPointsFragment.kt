package dadm.frba.utn.edu.ar.quehaceres.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import dadm.frba.utn.edu.ar.quehaceres.R
import dadm.frba.utn.edu.ar.quehaceres.activities.GroupActivity
import dadm.frba.utn.edu.ar.quehaceres.fragments.dummy.MemberPoints
import dadm.frba.utn.edu.ar.quehaceres.models.User
import dadm.frba.utn.edu.ar.quehaceres.services.Services
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.fragment_member_points_list.*
import java.io.Serializable

class SelectMemberPointsFragment : Fragment() {

    private var listener: OnListFragmentInteractionListener? = null
    private lateinit var selectedMembers: ArrayList<IdAndPoints>
    private val services by lazy { Services(context!!) }
    private var currentImage: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            @Suppress("UNCHECKED_CAST")
            selectedMembers = it.getSerializable(ARG_SELECTED_MEMBERS) as ArrayList<IdAndPoints>
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_member_points_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        list.adapter = SelectMemberPointsAdapter(selectedMembers) { user ->
            val dialog = SelectWeeklyPointsDialog(context!!, getCurrentPoints(user)) { setNewPointsForUser(user, it) }
            dialog.show()
        }

        next.setOnClickListener {
            if (group_name.text.toString().isEmpty()) {
                group_name.error = "Elige un nombre para el grupo"
            } else {
                createGroup()
            }
        }

        camera_layout.setOnClickListener {
            onVerifyClicked()
        }
    }

    private fun startVerification() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(context!!.packageManager)?.also {
                startActivityForResult(takePictureIntent, GroupActivity.CAPTURE_IMAGE_REQUEST)
            }
        }
    }

    private fun onVerifyClicked() {
        if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.CAMERA), GroupActivity.CAMERA_PERMISSION_REQUEST)
        } else {
            startVerification()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when {
            requestCode == GroupActivity.CAPTURE_IMAGE_REQUEST && resultCode == AppCompatActivity.RESULT_OK -> {
                @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
                val imageBitmap = data!!.extras.get("data") as Bitmap
                confirmImage(imageBitmap)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            GroupActivity.CAMERA_PERMISSION_REQUEST -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    startVerification()
                } else {
                    Toast.makeText(context!!, "No tenemos permiso", Toast.LENGTH_SHORT).show()
                }
                return
            }
            else -> {
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun confirmImage(imageBitmap: Bitmap) {
        services.upload(imageBitmap)
                .doOnSubscribe {
                    camera_indicator.visibility = View.GONE
                    loading_image.visibility = View.VISIBLE
                    image.visibility = View.GONE
                }
                .subscribe(
                        {
                            loading_image.visibility = View.GONE
                            image.visibility = View.VISIBLE
                            image.setImageURI(it.file)
                            currentImage = it.file
                        },
                        {
                            loading_image.visibility = View.GONE
                            image.visibility = View.VISIBLE
                            Toast.makeText(context!!, "Error while uploading an image", Toast.LENGTH_SHORT).show()
                        }
                )
    }

    @SuppressLint("CheckResult")
    private fun createGroup() {
        services.createGroup(currentImage, group_name.text.toString(), selectedMembers.map { Pair(it.user, it.points) })
                .subscribe(
                        { activity?.finish() },
                        { Toast.makeText(context!!, "Hubo un error al crear el grupo", Toast.LENGTH_SHORT).show() }
                )
    }

    private fun getCurrentPoints(user: User) = selectedMembers.first { it.user == user }.points

    private fun setNewPointsForUser(user: User, newPoints: Int) {
        selectedMembers[selectedMembers.indexOfFirst { it.user == user }] = IdAndPoints(user, newPoints)
        list.adapter?.notifyDataSetChanged()
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        activity?.title = "Crear Grupo"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement Listener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnListFragmentInteractionListener {
        fun onPointsSelected(selected: List<MemberPoints.MemberPointsItem>)
    }

    @Parcelize
    data class IdAndPoints(val user: User, val points: Int) : Parcelable, Serializable

    companion object {
        const val ARG_SELECTED_MEMBERS = "selected-members"

        fun newInstance(selectedMembers: List<User>): SelectMemberPointsFragment {
            val arguments = Bundle()
            val memberPoints = selectedMembers.map { IdAndPoints(it, 100) }
            arguments.putSerializable(ARG_SELECTED_MEMBERS, ArrayList(memberPoints))

            val fragment = SelectMemberPointsFragment()
            fragment.arguments = arguments

            return fragment
        }
    }
}
