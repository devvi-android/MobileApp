package com.app.citycare.ui.Event

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.app.citycare.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.util.*

class AddBlogFragment : Fragment() {
    private lateinit var filepath: Uri
    private lateinit var view1: View
    private lateinit var view2: View
    private lateinit var imgThumbnail: ImageView
    private lateinit var bSelectImage: TextView
    private lateinit var btnPublish: TextView
    private lateinit var bDesc: EditText
    private lateinit var bAuthor: EditText
    private lateinit var bTitle: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_publish, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view1 = view.findViewById(R.id.view)
        view2 = view.findViewById(R.id.view2)
        imgThumbnail = view.findViewById(R.id.img_thumbnail)
        bSelectImage = view.findViewById(R.id.b_selectImage)
        btnPublish = view.findViewById(R.id.btn_publish)
        bTitle = view.findViewById(R.id.b_tittle)
        bDesc = view.findViewById(R.id.b_desc)
        bAuthor = view.findViewById(R.id.b_author)


        selectimage()
    }


    private fun selectimage() {
        view2.setOnClickListener(View.OnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Your Image"), 101)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101 && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            filepath = data.data!!
            imgThumbnail.visibility = View.VISIBLE
            imgThumbnail.setImageURI(filepath)
            view2.visibility = View.INVISIBLE
            bSelectImage.visibility = View.INVISIBLE
            uploaddata(filepath)
        }
    }

    private fun uploaddata(filepath: Uri?) {
        btnPublish.setOnClickListener(View.OnClickListener {
            Dexter.withActivity(activity).withPermissions(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(multiplePermissionsReport: MultiplePermissionsReport) {
                    if (multiplePermissionsReport.areAllPermissionsGranted()) {
                        if (bTitle.text.toString() == "") {
                            bTitle.error = "Field is Required!!"
                        } else if (bDesc.text.toString() == "") {
                            bDesc.error = "Field is Required!!"
                        } else if (bAuthor.text.toString() == "") {
                            bAuthor.error = "Field is Required!!"
                        } else {
                            val pd = ProgressDialog(context)
                            pd.setTitle("Uploading...")
                            pd.setMessage("Please wait for a while until we upload this data to our Firebase Storage and Firestore")
                            pd.setCancelable(false)
                            pd.show()
                            val title: String = bTitle.text.toString()
                            val desc: String = bDesc.text.toString()
                            val author: String = bAuthor.text.toString()
                            if (filepath != null) {
                                val storage = FirebaseStorage.getInstance()
                                val reference = storage.reference.child("images/$filepath.jpg")
                                reference.putFile(filepath).addOnSuccessListener {
                                    reference.downloadUrl.addOnCompleteListener { task ->
                                        val file_url = task.result.toString()
                                        val date = DateFormat.format(
                                            "dd",
                                            Date()
                                        ) as String
                                        val month = DateFormat.format(
                                            "MMM",
                                            Date()
                                        ) as String
                                        val final_date = "$date $month"
                                        val map =
                                            HashMap<String, String>()
                                        map["tittle"] = title
                                        map["desc"] = desc
                                        map["author"] = author
                                        map["date"] = final_date
                                        map["img"] = file_url
                                        map["timestamp"] =
                                            System.currentTimeMillis().toString()
                                        map["share_count"] = "0"
                                        FirebaseFirestore.getInstance().collection("Blogs")
                                            .document().set(map).addOnCompleteListener { task ->
                                                if (task.isSuccessful) {
                                                    pd.dismiss()
                                                    Toast.makeText(
                                                        context,
                                                        "Post Uploaded!!!",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    imgThumbnail.visibility = View.INVISIBLE
                                                    view2.visibility = View.VISIBLE
                                                    bSelectImage.visibility = View.VISIBLE
                                                    bTitle.setText("")
                                                    bDesc.setText("")
                                                    bAuthor.setText("")
                                                }
                                            }
                                    }
                                }
                            }
                        }
                    }
                    if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied) {
                        showsettingdialog()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    list: List<PermissionRequest>,
                    permissionToken: PermissionToken
                ) {
                    permissionToken.continuePermissionRequest()
                }
            }).withErrorListener { requireActivity().finish() }.onSameThread().check()
        })
    }

    private fun showsettingdialog() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Need Permission")
        builder.setMessage("This app needs permission to use this feature. You can grant us these permission manually by clicking on below button")
        builder.setPositiveButton(
            "Next"
        ) { dialog, which ->
            dialog.dismiss()
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", requireActivity().packageName, null)
            intent.data = uri
            startActivityForResult(intent, 101)
        }
        builder.setNegativeButton(
            "Cancel"
        ) { dialog, which ->
            dialog.cancel()
            requireActivity().finish()
        }
        builder.show()
    }
}