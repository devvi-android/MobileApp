package com.app.citycare.ui.Event

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.app.citycare.R
import com.app.citycare.ui.News.NewsDetailFragmentArgs
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore

class BlogDetailFragment : Fragment() {
    var id: String? = null
    var title: String? = null
    var desc: String? = null
    var count: String? = null
    var n_count: Int = 0

    private val args: BlogDetailFragmentArgs by navArgs()

    private lateinit var imageView3: ImageView
    private lateinit var imageView4: ImageView
    private lateinit var floatingActionButton: FloatingActionButton


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_blog_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageView3 = view.findViewById<ImageView>(R.id.imageView3)
        imageView4 = view.findViewById<ImageView>(R.id.imageView4)
        floatingActionButton = view.findViewById(R.id.floatingActionButton)
        var model = args.model

        showdata()
    }

    private fun showdata() {
//        id = arguments?.getString("model").toString()
        id = args.model.id.toString()
        FirebaseFirestore.getInstance().collection("Blogs").document(id!!)
            .addSnapshotListener { value, error ->
                context?.let {
                    Glide.with(it).load(value!!.getString("img"))
                        .into(imageView3)
                }
                view?.findViewById<TextView>(R.id.textView4)?.text = (
                        Html.fromHtml(
                            "<font color='B7B7B7'>By </font> <font color='#000000'>" + value?.getString(
                                "author"
                            )
                        )
                        )
                view?.findViewById<TextView>(R.id.textView5)?.text = value?.getString("tittle")
                view?.findViewById<TextView>(R.id.textView6)?.text = value?.getString("desc")
                title = value?.getString("tittle").toString()
                desc = value?.getString("desc").toString()
                count = value?.getString("share_count").toString()
                val i_count: Int? = count?.toInt()
                if (i_count != null) {
                    n_count = i_count + 1
                }
            }
        floatingActionButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            val shareBody: String = desc.toString()
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_SUBJECT, title)
            intent.putExtra(Intent.EXTRA_TEXT, shareBody)
            startActivity(Intent.createChooser(intent, "Share Using"))
            val map = HashMap<String, Any>()
            map["share_count"] = n_count.toString()
            FirebaseFirestore.getInstance().collection("Blogs").document(id!!).update(map)
        })

        imageView4.setOnClickListener(View.OnClickListener { findNavController().popBackStack() })
    }

}