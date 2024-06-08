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
    private var currentImageUri: Uri? = null
    private var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        model = ViewModelProvider(this, ViewModelFactory.getInstance(this))[CreateViewModel::class.java]

        setupActionBar()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        binding.locationSwitch.setOnCheckedChangeListener { _, isActive ->
            if (isActive){
                getMyLastLocation()
            } else {
                model.setLocation(
                    lon = 0.0,
                    lat = 0.0
                )
                Toast.makeText(this, "Location access disabled", Toast.LENGTH_SHORT).show()
            }
        }

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.buttonSubmitNewStory.setOnClickListener { uploadImage() }

        observeResponse()

        model.currentImageUri.observe(this){
            Log.d("Image URI", "showImage: $it")
            currentImageUri = it
            binding.previewImageView.setImageURI(it)
        }

        model.getToken().observe(this){
            token = it
        }
    }

    private fun setupActionBar(){
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun observeResponse(){
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