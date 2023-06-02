package com.app.citycare.ui.Event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.citycare.R
import com.app.citycare.models.events.Model
import com.app.citycare.ui.Event.Adapters.Adapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.ArrayList

class EventFragment : Fragment() {
    private var list = arrayListOf<Model>()

    private lateinit var adapter: Adapter
    private lateinit var model: Model
    private lateinit var searchview: SearchView
    private lateinit var rvBlogs: RecyclerView
    private lateinit var fab: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchview = view.findViewById(R.id.searchview)
        rvBlogs = view.findViewById(R.id.rv_blogs)
        fab = view.findViewById(R.id.fab)

        fab.setOnClickListener {
            findNavController().navigate(R.id.action_events_to_addBlogFragment)
        }

        setupRv()
        setusearchview()

            adapter.setOnItemClickListener {
                val bundle = Bundle().apply {
                    putSerializable("model", it)
                }
                findNavController().navigate(
                    R.id.action_events_to_blogDetailFragment,
                    bundle
                )
            }
    }


    private fun setusearchview() {
        searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filter(newText)
                return false
            }
        })
    }

    private fun filter(newText: String) {
        val filtered_list = arrayListOf<Model>()
        for (item in list) {
            if (item.tittle?.lowercase(Locale.getDefault())?.contains(newText)!!) {
                filtered_list.add(item)
            }
        }
        if (filtered_list.isEmpty()) {
            //
        } else {
            adapter.filter_list(filtered_list)
        }
    }

    private fun setupRv() {
        list = ArrayList(list)
        FirebaseFirestore.getInstance().collection("Blogs").orderBy("timestamp")
            .addSnapshotListener { value, error ->
                list.clear()
                for (snapshot in value!!.documents) {
                    model = snapshot.toObject(Model::class.java)!!
                    model.id = snapshot.id
                    list.add(model)
                }
                adapter.notifyDataSetChanged()
            }
        adapter = Adapter(list)
        rvBlogs.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvBlogs.adapter = adapter
    }

}