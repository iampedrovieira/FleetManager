package com.example.fleetmanager

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition

private lateinit var vehicle_name : TextView
private lateinit var vin : TextView
private lateinit var brand_input : Spinner
private lateinit var model_input : EditText
private lateinit var plate_input : EditText
private lateinit var avg_input : EditText
private lateinit var km_input : EditText
private lateinit var last_revision_input : EditText
private lateinit var revision_value_input : EditText
private lateinit var last_revision_km_input : EditText
private lateinit var insurance_date_input : EditText
private lateinit var insurance_value_input : EditText
private lateinit var add_vehicle : Button
private lateinit var scan_plate : ImageButton
private lateinit var motorcycle : ImageButton
private lateinit var car : ImageButton
private lateinit var truck : ImageButton
private lateinit var bike : ImageButton

//image to auto complite
val REQUEST_IMAGE_CAPTURE = 1
private val LOCATION_PERMISSION_REQUEST_CODE = 1
private lateinit var  imageBitmap_autocomplite: Bitmap

class VehicleRegistration : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle_registration)

        vehicle_name = findViewById(R.id.vehicle_name)
        vin = findViewById(R.id.vehicle_vin)
        brand_input = findViewById(R.id.brand_input)
        model_input = findViewById(R.id.model_input)
        plate_input = findViewById(R.id.plate_input)
        avg_input = findViewById(R.id.consumption_input)
        km_input = findViewById(R.id.kilometers_input)
        last_revision_input = findViewById(R.id.last_revision_input)
        revision_value_input = findViewById(R.id.revision_value_input)
        last_revision_km_input = findViewById(R.id.last_revision_km_input)
        insurance_date_input = findViewById(R.id.insurance_date_input)
        insurance_value_input = findViewById(R.id.insurance_value_input)
        add_vehicle = findViewById(R.id.add_vehicle)
        scan_plate = findViewById(R.id.tensorflow)
        motorcycle = findViewById(R.id.motorcycle)
        car = findViewById(R.id.car)
        truck = findViewById(R.id.truck)
        bike = findViewById(R.id.bike)
    }


    fun autoCompliteHandler(view: View){
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            imageBitmap_autocomplite = data?.extras?.get("data") as Bitmap
            val image = InputImage.fromBitmap(imageBitmap_autocomplite, 0)
            //text recognition
            val recognizer = TextRecognition.getClient()
            val result = recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    val plate = visionText.text.toUpperCase()
                    Log.v("asdasdasd",plate)

                    //Call external API to autocomplite
                        //IF fail send toast saying 'try again'



                }
                .addOnFailureListener { e ->
                    // Send Toast saying try again
                    // ...
                }

        }
    }
}