package com.app.citycare.ui.Profile

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.citycare.R
import com.app.citycare.ui.LoginActivity
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView

class ProfileFragment : Fragment() {
    private lateinit var inviteFriend: MaterialButton
    private lateinit var aboutApp: MaterialButton
    private lateinit var faq: MaterialButton
    private lateinit var logOut: MaterialButton
    private lateinit var name: TextView
    private lateinit var email: TextView

    private lateinit var googleAuth: FirebaseAuth

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var storage: FirebaseStorage
    private lateinit var selectedImg: Uri
    private lateinit var alertDialog: AlertDialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inviteFriend = view.findViewById(R.id.inviteFriends)
        aboutApp = view.findViewById(R.id.aboutApp)
        faq = view.findViewById(R.id.faq)
        logOut = view.findViewById(R.id.logOut)
        name = view.findViewById(R.id.personNameContent)
        email = view.findViewById(R.id.personEmailContent)

        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()

        val img = view.findViewById<CircleImageView>(R.id.personImageContent)

        img.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"

            startActivityForResult(intent, 1)
        }

        val account = context?.let { GoogleSignIn.getLastSignedInAccount(it) }
        name.text = account?.displayName.toString()
        email.text = account?.email.toString()
        context?.let { Glide.with(it).load(account?.photoUrl).into(img) }


        googleAuth = FirebaseAuth.getInstance()

        logOut.setOnClickListener {
            MaterialAlertDialogBuilder(
                it.context
            )
                .setTitle("LogOut")
                .setMessage(R.string.message)
                .setNegativeButton(R.string.no_btn) { dialog, which ->
                    dialog.cancel()
                }
                .setPositiveButton(R.string.yes_btn) { dialog, which ->
                    dialog.dismiss()
                    googleAuth.signOut()
                    startActivity(Intent(activity, LoginActivity::class.java))

                }
                .show()
        }

        inviteFriend.setOnClickListener {
            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            val shareBody =
                getString(R.string.share_content)
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here")
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
            startActivity(Intent.createChooser(sharingIntent, "Share via"))
        }

        faq.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_faqFragment)
        }

        aboutApp.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_aboutAppFragment)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {
            if (data.data != null) {
                selectedImg = data.data!!
                val img = view?.findViewById<CircleImageView>(R.id.personImageContent)
                img?.setImageURI(selectedImg)
            }

        }
    }

}