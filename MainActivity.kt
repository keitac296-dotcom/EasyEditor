package com.easyeditor

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.easyeditor.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private var selectedUri: Uri? = null
    
    private val pickMedia = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            selectedUri = result.data?.data
            selectedUri?.let {
                startEditor(it)
            }
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        binding.btnSelectVideo.setOnClickListener {
            selectVideo()
        }
        
        binding.btnSelectPhoto.setOnClickListener {
            selectPhoto()
        }
    }
    
    private fun selectVideo() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        intent.type = "video/*"
        pickMedia.launch(intent)
    }
    
    private fun selectPhoto() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        pickMedia.launch(intent)
    }
    
    private fun startEditor(uri: Uri) {
        val intent = Intent(this, EditorActivity::class.java)
        intent.putExtra("media_uri", uri.toString())
        startActivity(intent)
    }
}