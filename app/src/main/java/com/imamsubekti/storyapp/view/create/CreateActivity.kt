package com.imamsubekti.storyapp.view.create

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.imamsubekti.storyapp.R
import com.imamsubekti.storyapp.ViewModelFactory
import com.imamsubekti.storyapp.databinding.ActivityCreateBinding
import com.imamsubekti.storyapp.helper.ImageTransform

class CreateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateBinding
    private lateinit var model: CreateViewModel
    private var currentImageUri: Uri? = null
    private var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        model = ViewModelProvider(this, ViewModelFactory.getInstance(this))[CreateViewModel::class.java]

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.buttonSubmitNewStory.setOnClickListener { uploadImage() }

        model.currentImageUri.observe(this){
            Log.d("Image URI", "showImage: $it")
            currentImageUri = it
            binding.previewImageView.setImageURI(it)
        }

        model.status.observe(this){
            when(it) {
                CreateViewModel.CreateStatus.SUCCESS -> { finish() }
                CreateViewModel.CreateStatus.FAILED -> {
                    Toast.makeText(this, R.string.failed_to_login, Toast.LENGTH_SHORT).show()
                    binding.buttonSubmitNewStory.text = getString(R.string.add_new_story)
                }
                CreateViewModel.CreateStatus.PROGRESS -> {
                    binding.buttonSubmitNewStory.text = getString(R.string.loading)
                }
                else -> { }
            }
        }

        model.getToken().observe(this){
            token = it
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            model.setCurrentImageUri(uri)
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun uploadImage(){
        if (currentImageUri == null) {
            Toast.makeText(this, getString(R.string.upload_image_first), Toast.LENGTH_SHORT).show()
            return
        }
        model.uploadNewStory(
            token = token as String,
            description = binding.textDescription.text.toString(),
            photo = ImageTransform(this, currentImageUri!!).toMultipart()
        )
    }
}