package com.app.citycare.ui.Event.Adapters

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.citycare.R
import com.app.citycare.models.events.Model
import com.app.citycare.ui.Event.BlogDetailFragment
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore

class Adapter() : RecyclerView.Adapter<Adapter.ViewHolder>() {
    private var list = arrayListOf<Model>()

    constructor(list: ArrayList<Model>) : this() {
        this.list = list
        notifyDataSetChanged()
    }

    fun filter_list(filter_list: ArrayList<Model>?) {
        if (filter_list != null) {
            list = filter_list
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]
        holder.title.text = model.tittle
        holder.date.text = model.date
        holder.share_count.text = model.share_count
        holder.author.text = model.author
        Glide.with(holder.author.context).load(model.img).into(holder.img)
        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it(model) }


//            val intent = Intent(holder.author.context, BlogDetailFragment::class.java)
//            intent.putExtra("id", model.id)
//            holder.author.context.startActivity(intent)
        }

        holder.itemView.setOnLongClickListener {
            val builder = AlertDialog.Builder(holder.author.context)
            builder.setTitle("What you want to do?")
            builder.setPositiveButton(
                "UPDATE"
            ) { dialog, which ->
                val u_dialog = Dialog(holder.author.context)
                u_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                u_dialog.setCancelable(false)
                u_dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
                u_dialog.setContentView(R.layout.update_dialog)
                u_dialog.show()
                val title = u_dialog.findViewById<EditText>(R.id.b_tittle)
                val desc = u_dialog.findViewById<EditText>(R.id.b_desc)
                val author = u_dialog.findViewById<EditText>(R.id.b_author)
                title.setText(model.tittle)
                desc.setText(model.desc)
                author.setText(model.author)
                val dialogbutton = u_dialog.findViewById<TextView>(R.id.btn_publish)
                dialogbutton.setOnClickListener {
                    if (title.text.toString() == "") {
                        title.error = "Field is Required!!"
                    } else if (desc.text.toString() == "") {
                        desc.error = "Field is Required!!"
                    } else if (author.text.toString() == "") {
                        author.error = "Field is Required!!"
                    } else {
                        val map = HashMap<String, Any>()
                        map["tittle"] = title.text.toString()
                        map["desc"] = desc.text.toString()
                        map["author"] = author.text.toString()
                        model.id?.let { it1 ->
                            FirebaseFirestore.getInstance().collection("Blogs")
                                .document(it1).update(map)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        dialog.dismiss()
                                        u_dialog.dismiss()
                                    }
                                }
                        }
                    }
                }
            }
            builder.setNegativeButton(
                "DELETE"
            ) { dialog, which ->
                val builders = AlertDialog.Builder(holder.author.context)
                builders.setTitle("Are you sure to Delete it??")
                builders.setPositiveButton(
                    "Yes! I am Sure"
                ) { dialog, which ->
                    model.id?.let { it1 ->
                        FirebaseFirestore.getInstance().collection("Blogs").document(it1)
                            .delete()
                    }
                    dialog.dismiss()
                }
                val dialogs = builders.create()
                dialogs.show()
            }
            val dialog = builder.create()
            dialog.show()
            false
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private var onItemClickListener: ((Model) -> Unit)? = null

    fun setOnItemClickListener(listener: (Model) -> Unit) {
        onItemClickListener = listener
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var img: ImageView
        var date: TextView
        var title: TextView
        var share_count: TextView
        var author: TextView

        init {
            img = itemView.findViewById(R.id.imageView3)
            date = itemView.findViewById(R.id.t_date)
            title = itemView.findViewById(R.id.textView9)
            share_count = itemView.findViewById(R.id.textView10)
            author = itemView.findViewById(R.id.textView8)
        }
    }
}