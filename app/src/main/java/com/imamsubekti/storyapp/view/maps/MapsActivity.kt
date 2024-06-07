package com.imamsubekti.storyapp.view.maps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.imamsubekti.storyapp.R
import com.imamsubekti.storyapp.ViewModelFactory
import com.imamsubekti.storyapp.databinding.ActivityMapsBinding
import com.imamsubekti.storyapp.entity.AllStoriesResponse

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityMapsBinding
    private lateinit var model: MapsViewModel
    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        model = ViewModelProvider(this, ViewModelFactory.getInstance(this))[MapsViewModel::class.java]

        setupActionBar()
        renderMapToFragment()
        getStoryList()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        model.listStory.observe(this){
            attachDataToMaps(it)

            val firstLatLng = LatLng(it.listStory[0].lat as Double, it.listStory[0].lon as Double)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLatLng, 6f))
        }
    }

    private fun renderMapToFragment(){
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun setupActionBar(){
        with(binding.toolbar){
            setNavigationOnClickListener { finish() }
            inflateMenu(R.menu.map_options)
            setOnMenuItemClickListener {
                when(it.itemId) {
                    R.id.normal_type -> {
                        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                        true
                    }
                    R.id.satellite_type -> {
                        mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                        true
                    }
                    R.id.terrain_type -> {
                        mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
                        true
                    }
                    R.id.hybrid_type -> {
                        mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun getStoryList(){
        model.getToken().observe(this){
            if (it.isNotEmpty()){
                model.updateList(it)
            }
        }
    }

    private fun attachDataToMaps(story: AllStoriesResponse){
        story.listStory.forEach{
            val latLng = LatLng(it.lat as Double, it.lon as Double)
            mMap.addMarker(MarkerOptions().position(latLng).title(it.name))
        }
    }
}