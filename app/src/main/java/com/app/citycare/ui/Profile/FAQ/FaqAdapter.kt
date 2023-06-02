package com.app.citycare.ui.Profile.FAQ

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.citycare.R

class FaqAdapter : RecyclerView.Adapter<FaqAdapter.ViewHolder>() {

    private var titles =
        arrayListOf(
            "What is Citycare?",
            "Our main goal?",
            "What we do?",
            "Why is this application needed and what is its target audience?",
            "What is the financial benefit for sponsors and shareholders of this application?",
            "Why should people do these tasks?",
            "Benefits of being a volunteer in sports?",
            "Benefits if being a volunteer socially?",
            "Benefits of being a volunteer in environmental and animal protection volunteering?"
        )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val root =
            LayoutInflater.from(parent.context).inflate(R.layout.list_faq_items, parent, false)
        return ViewHolder(root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = titles[position]
        holder.itemView.setOnClickListener(View.OnClickListener {

        })
    }


    override fun getItemCount(): Int {
        return titles.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView

        init {
            title = itemView.findViewById(R.id.faqTitle)
        }
    }
}