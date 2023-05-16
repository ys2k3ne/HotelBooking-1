package com.example.hotelbooking

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.example.hotelbooking.models.DataClass
import com.example.hotelbooking.databinding.ActivityUploadBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.text.DateFormat
import java.util.Calendar

class UploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding
    private lateinit var dbRef: DatabaseReference
    var imageURL: String = ""
    var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbRef = FirebaseDatabase.getInstance().getReference("hotels")

        val activityResultLauncher = registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                uri = data!!.data
                binding.uploadImage.setImageURI(uri)
            } else {
                Toast.makeText(this@UploadActivity, "No Image Selected", Toast.LENGTH_SHORT).show()
            }
        }
        binding.uploadImage.setOnClickListener {
            val photoPicker = Intent(Intent.ACTION_PICK)
            photoPicker.type = "image/*"
            activityResultLauncher.launch(photoPicker)
        }
        binding.saveButton.setOnClickListener {
            saveData()
        }
    }
    private fun saveData(){
        val storageReference = FirebaseStorage.getInstance().reference.child("Task Images")
            .child(uri!!.lastPathSegment!!)

        val builder = AlertDialog.Builder(this@UploadActivity)
        builder.setCancelable(false)
        builder.setView(R.layout.progress_layout)
        val dialog = builder.create()
        dialog.show()

        storageReference.putFile(uri!!).addOnSuccessListener { taskSnapshot ->
            val uriTask = taskSnapshot.storage.downloadUrl
            while (!uriTask.isComplete);
            val urlImage = uriTask.result
            imageURL = urlImage.toString()
            uploadData()
            dialog.dismiss()
        }.addOnFailureListener {
            dialog.dismiss()
        }
    }
    private fun uploadData(){
        val hotelName = binding.uploadTitle.text.toString()
        val hotelAddress = binding.uploadDesc.text.toString()
        val hotelPrice = binding.uploadPriority.text.toString()

        val roomId = dbRef.push().key!!
        val dataClass = DataClass(roomId, hotelName, hotelAddress, hotelPrice,4f, imageURL, "", true)
//        val currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().time)

        dbRef.child(roomId)
            .setValue(dataClass).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this@UploadActivity, "Saved", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }.addOnFailureListener { e ->
                Toast.makeText(
                    this@UploadActivity, e.message.toString(), Toast.LENGTH_SHORT).show()
            }
    }
}