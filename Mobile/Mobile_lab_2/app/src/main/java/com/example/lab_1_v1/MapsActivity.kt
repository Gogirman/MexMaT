package com.example.lab_1_v1

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions



class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var dataBase: AppDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Подключаем БД
        dataBase = App.instance?.database
    }

    override fun onMapReady(googleMap: GoogleMap) {
        // Карта
        mMap = googleMap

        // Вставляем маркеры
        val mapMarkers: List<MapMarker>? = dataBase?.markerDao()?.getAll
        if (mapMarkers != null) {
            mapMarkers.forEach{
                val location = LatLng(it.lat, it.lng)
                mMap.addMarker(MarkerOptions().position(location))
            }
        }

        // Обрабатываем событие нажатия на карту
        mMap.setOnMapClickListener { latlng ->
            // Получаем широту - долготу
            // Вставляем на карту
            val location = LatLng(latlng.latitude, latlng.longitude)
            mMap.addMarker(MarkerOptions().position(location))
            // Вставляем в БД
            dataBase?.markerDao()?.insert(MapMarker(latlng.latitude, latlng.longitude))
        }

        // Обрабатываем событие нажатия на маркер
        mMap.setOnMarkerClickListener { p0 ->
            // При нажатии на маркер открываем новое активити
            // Берем координаты маркера
            val location = p0.position
            // Берем такой маркер из БД
            val clickedMarker = dataBase?.markerDao()?.getByLatLng(location.latitude, location.longitude)
            // Берем его путь к фотке
            val photoPath = clickedMarker?.photoPath
            val intent = Intent(this@MapsActivity, MarkerActivity::class.java)

            intent.putExtra("photoPath", photoPath.toString())
            intent.putExtra("coords", location.toString())

            startActivityForResult(intent, 123)
            true
        }
    }

    protected override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 123 && resultCode == Activity.RESULT_OK) {
            val str = data
                ?.getStringExtra("coords")
                ?.substringAfter('(')
                ?.substringBefore(')')

            val lat = str?.substringBefore(',')?.toDouble()
            val lng = str?.substringAfter(',')?.toDouble()

            lateinit var clickedMarker: MapMarker
            if (lat != null && lng != null) {
                clickedMarker = dataBase?.markerDao()?.getByLatLng(lat, lng)!!
            }

            val photoPath = data?.getStringExtra("photoPath")
            clickedMarker.photoPath = photoPath
            // Делаем апдейт БД
            dataBase?.markerDao()?.update(clickedMarker)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
