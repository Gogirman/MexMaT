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
import com.google.android.gms.maps.model.MarkerOptions
import java.io.Serializable


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    // Маркеры на карте
    // Храним координаты - путь к файлу
    private var mapMarkers: MutableMap<LatLng, Uri?> = mutableMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Восстанавливаем все маркеры после onRestoreInstanceState
        for ((coord, photoPath) in mapMarkers) {
            mMap.addMarker(MarkerOptions().position(coord))
            //marker.tag = photoPath
        }

//            Обрабатываем событие нажатия на карту
        mMap.setOnMapClickListener { latlng ->
            //                Получаем широту - долготу
            //                Создаем маркер
            //                добавляем в список
            val location = LatLng(latlng.latitude,latlng.longitude)
            val newMarker = mMap.addMarker(MarkerOptions().position(location))
            mapMarkers[newMarker.position] = null
        }

//        Обрабатываем событие нажатия на маркер
        mMap.setOnMarkerClickListener { p0 ->

            // При нажатии на маркер открываем новое активити
            // Передаем туда долготу - широту
            // Путь к файлу
            val location = p0.position
            val photoPath = mapMarkers[location]
            val intent = Intent(this@MapsActivity, MarkerActivity::class.java)

//            if (mapMarkers[location] != null) {
////                intent.putExtra("photoPath", photoPath as String)
////            }
            intent.putExtra("photoPath", photoPath.toString())
            intent.putExtra("coords", location.toString())
//            intent.putExtra("latitude", location.latitude)
//            intent.putExtra("longitude", location.longitude)

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

            lateinit var coord: LatLng
            if (lat != null && lng != null) {
                coord = LatLng(lat, lng)
            }

            val photoPath = Uri.parse(data?.getStringExtra("photoPath"))

            mapMarkers[coord] = photoPath
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    // Сохранение состояния
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("Markers", mapMarkers as Serializable)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        mapMarkers = savedInstanceState.getSerializable("Markers") as MutableMap<LatLng, Uri?>
    }
}
