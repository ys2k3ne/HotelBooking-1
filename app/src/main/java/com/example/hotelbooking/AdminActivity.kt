package com.example.hotelbooking

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.hotelbooking.adapter.HotelAdapter
import com.example.hotelbooking.databinding.ActivityAdminBinding
import com.example.hotelbooking.models.DataClass
import com.google.firebase.database.*
import java.util.*

class AdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var dataList: MutableList<DataClass>
    private lateinit var adapter: HotelAdapter
    private lateinit var dialog: AlertDialog
    private lateinit var eventListener: ValueEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gridLayoutManager = GridLayoutManager(this@AdminActivity, 1)
        binding.recyclerView.layoutManager = gridLayoutManager
        binding.searchView.clearFocus()

        dialog = createProgressDialog()
        dialog.show()

        dataList = mutableListOf()
        adapter = HotelAdapter(this@AdminActivity, dataList, true)
        binding.recyclerView.adapter = adapter
        databaseReference = FirebaseDatabase.getInstance().getReference("hotels")

        eventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dataList.clear()
                for (itemSnapshot in snapshot.children) {
                    val dataClass = itemSnapshot.getValue(DataClass::class.java)
                    if (dataClass != null) {
                        dataList.add(dataClass)
                    }
                }
                adapter.notifyDataSetChanged()
                dialog.dismiss()
            }

            override fun onCancelled(error: DatabaseError) {
                dialog.dismiss()
            }
        }

        databaseReference.addValueEventListener(eventListener)

        binding.fab.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@AdminActivity, UploadActivity::class.java)
            startActivity(intent)
        })

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                searchList(newText)
                return true
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        databaseReference.removeEventListener(eventListener)
    }

    private fun createProgressDialog(): AlertDialog {
        val builder = AlertDialog.Builder(this@AdminActivity)
        builder.setCancelable(false)
        builder.setView(layoutInflater.inflate(R.layout.progress_layout, null))
        return builder.create()
    }

    private fun searchList(text: String) {
        val searchText = text.lowercase(Locale.getDefault())
        val filteredList = dataList.filter { it.hotelName?.lowercase()?.contains(searchText) == true }
        adapter.searchDataList(filteredList)
    }
}
