package com.imamsubekti.storyapp.view.create

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.imamsubekti.storyapp.R
import com.imamsubekti.storyapp.ViewModelFactory
import com.imamsubekti.storyapp.databinding.ActivityCreateBinding
import com.imamsubekti.storyapp.helper.ImageTransform

class CreateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateBinding
    private lateinit var model: CreateViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        model = ViewModelProvider(this, ViewModelFactory.getInstance(this))[CreateViewModel::class.java]

        setupActionBar()
        setupLocation()

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.previewImageView.setOnClickListener { startGallery() }
        binding.buttonSubmitNewStory.setOnClickListener { uploadImage() }

        setupTokenAndImagePreview()
        observeUploadResponse()

    }

    private fun setupActionBar(){
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun setupLocation(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        binding.locationSwitch.setOnCheckedChangeListener { _, isActive ->
            if (isActive){
                getMyLastLocation()
            } else {
                model.setLocation(0.0,0.0)
                Toast.makeText(this, "Location access disabled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeUploadResponse(){
        model.status.observe(this){
            when(it) {
                CreateViewModel.CreateStatus.SUCCESS -> {
                    finish()
                }
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
    }

    private fun setupTokenAndImagePreview(){
        model.getToken().observe(this){
            token = it
        }
        model.currentImageUri.observe(this){
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
        if (uri != null) {
            model.setCurrentImageUri(uri)
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun uploadImage(){
        model.currentImageUri.observe(this) {
            if (it == null) {
                Toast.makeText(this, getString(R.string.upload_image_first), Toast.LENGTH_SHORT).show()
            } else {
                binding.buttonSubmitNewStory.text = getString(R.string.loading)
                model.uploadNewStory(
                    token = token as String,
                    description = binding.textDescription.text.toString(),
                    photo = ImageTransform(this, it).toMultipart()
                )
            }
        }
    }

    private val launcherLocation =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
            when {
                it[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> getMyLastLocation()
                it[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> getMyLastLocation()
                else -> {}
            }
        }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyLastLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) && checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)){
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                model.setLocation(
                    lon = location?.longitude ?: 0.0,
                    lat = location?.latitude ?: 0.0
                )
            }
        } else {
            launcherLocation.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }
}