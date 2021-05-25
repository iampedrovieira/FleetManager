package com.example.fleetmanager

import android.app.DatePickerDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.KeyEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.fleetmanager.api.Endpoints
import com.example.fleetmanager.api.ServiceBuilder
import com.example.fleetmanager.entities.TruksDetailsPlate
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


private lateinit var vehicle_name : TextView
private lateinit var vin : TextView
private lateinit var brand_input : EditText
private lateinit var model_input : EditText
private lateinit var plate_input : EditText
private lateinit var plate_date_input : TextView
private lateinit var avg_input : EditText
private lateinit var km_input : EditText
private lateinit var last_revision_input : TextView
private lateinit var revision_value_input : EditText
private lateinit var last_revision_km_input : EditText
private lateinit var insurance_date_input : TextView
private lateinit var insurance_value_input : EditText
private lateinit var add_vehicle : Button
private lateinit var scan_plate : ImageButton
private lateinit var motorcycle : Button
private lateinit var car : Button
private lateinit var truck : Button
private lateinit var bike : Button

//image to auto complite
val REQUEST_IMAGE_CAPTURE = 1
val CAMERA_ACTION_PICK_REQUEST_CODE = 1;
private val LOCATION_PERMISSION_REQUEST_CODE = 1
private lateinit var  imageBitmap_autocomplite: Bitmap
private var motorcycleButton: Boolean = false
private var carButton: Boolean = false
private var truckButton: Boolean = false
private var bikeButton: Boolean = false

private var datePickerA: Boolean = false
private var datePickerB: Boolean = false
private var datePickerC: Boolean = false

class VehicleRegistration : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle_registration)

        vehicle_name = findViewById(R.id.vehicle_name)
        vin = findViewById(R.id.vehicle_vin)
        brand_input = findViewById(R.id.brand_input)
        model_input = findViewById(R.id.model_input)
        plate_input = findViewById(R.id.plate_input)
        plate_date_input = findViewById(R.id.plate_date_input)
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


        plate_input.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                var plate = plate_input.text
                apiRequest(plate.toString())
                return@OnKeyListener true
            }
            false
        })


    }


    fun autoCompliteHandler(view: View){
        openCamera();
    }


    private fun openCamera(){
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

                    val mAlertDialog = AlertDialog.Builder(
                        ContextThemeWrapper(
                            this,
                            R.style.AlertDialogCustom
                        )
                    )
                    mAlertDialog.setIcon(R.mipmap.ic_launcher_round) //set alertdialog icon
                    mAlertDialog.setTitle(R.string.app_name) //set alertdialog title
                    mAlertDialog.setMessage("Plate: " + plate) //set alertdialog message
                    mAlertDialog.setPositiveButton(R.string.confirm) { dialog, id ->
                        apiRequest(plate);
                    }
                    mAlertDialog.setNegativeButton(R.string.reply, { dialog, id -> openCamera(); })
                    mAlertDialog.show()

                }
                .addOnFailureListener { e ->
                    // Send Toast saying try again
                    // ...
                }

        }
    }

    private fun apiRequest(plate: String) {
        val request = ServiceBuilder.buildService(Endpoints::class.java)
        val call = request.getVehicleDetails(plate)

        call.enqueue(object : Callback<TruksDetailsPlate> {
            override fun onResponse(
                call: Call<TruksDetailsPlate>,
                response: Response<TruksDetailsPlate>,
            ) {
                var c: TruksDetailsPlate = response.body()!!

                when (c.type.toInt()) {
                    1 -> {
                        motorcycle.performClick();
                    }
                    2 -> {
                        car.performClick();
                    }
                    3 -> {
                        truck.performClick();
                    }
                    4 -> {
                        bike.performClick();
                    }
                }

                brand_input.setText(c.brand);
                plate_input.setText(c.plate)
                plate_date_input.text = c.plate_date;
                model_input.setText(c.model);
                avg_input.setText(c.average_consumption.toString());

            }

            override fun onFailure(call: Call<TruksDetailsPlate>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }




    fun vehicleTypeClick(view: View) {
        when (view?.id){
            (R.id.motorcycle) -> {
                if (!motorcycleButton) {
                    motorcycle.setCompoundDrawableTintList(ColorStateList.valueOf(resources.getColor(
                        R.color.primaryBlue)));
                    car.setCompoundDrawableTintList(ColorStateList.valueOf(resources.getColor(R.color.primaryGrey)));
                    truck.setCompoundDrawableTintList(ColorStateList.valueOf(resources.getColor(R.color.primaryGrey)));
                    bike.setCompoundDrawableTintList(ColorStateList.valueOf(resources.getColor(R.color.primaryGrey)));
                    motorcycleButton = true
                    carButton = false
                    truckButton = false
                    bikeButton = false
                } else {
                    motorcycle.setCompoundDrawableTintList(ColorStateList.valueOf(resources.getColor(
                        R.color.primaryGrey)));
                    motorcycleButton = false
                }
            }
            (R.id.car) -> {
                if (!carButton) {
                    car.setCompoundDrawableTintList(ColorStateList.valueOf(resources.getColor(R.color.primaryBlue)));
                    motorcycle.setCompoundDrawableTintList(ColorStateList.valueOf(resources.getColor(
                        R.color.primaryGrey)));
                    truck.setCompoundDrawableTintList(ColorStateList.valueOf(resources.getColor(R.color.primaryGrey)));
                    bike.setCompoundDrawableTintList(ColorStateList.valueOf(resources.getColor(R.color.primaryGrey)));
                    carButton = true
                    truckButton = false
                    bikeButton = false
                    motorcycleButton = false
                } else {
                    car.setCompoundDrawableTintList(ColorStateList.valueOf(resources.getColor(R.color.primaryGrey)));
                    carButton = false
                }
            }
            (R.id.truck) -> {
                if (!truckButton) {
                    truck.setCompoundDrawableTintList(ColorStateList.valueOf(resources.getColor(R.color.primaryBlue)));
                    motorcycle.setCompoundDrawableTintList(ColorStateList.valueOf(resources.getColor(
                        R.color.primaryGrey)));
                    car.setCompoundDrawableTintList(ColorStateList.valueOf(resources.getColor(R.color.primaryGrey)));
                    bike.setCompoundDrawableTintList(ColorStateList.valueOf(resources.getColor(R.color.primaryGrey)));
                    truckButton = true
                    carButton = false
                    bikeButton = false
                    motorcycleButton = false
                } else {
                    truck.setCompoundDrawableTintList(ColorStateList.valueOf(resources.getColor(R.color.primaryGrey)));
                    truckButton = false
                }
            }
            (R.id.bike) -> {
                if (!bikeButton) {
                    bike.setCompoundDrawableTintList(ColorStateList.valueOf(resources.getColor(R.color.primaryBlue)));
                    motorcycle.setCompoundDrawableTintList(ColorStateList.valueOf(resources.getColor(
                        R.color.primaryGrey)));
                    car.setCompoundDrawableTintList(ColorStateList.valueOf(resources.getColor(R.color.primaryGrey)));
                    truck.setCompoundDrawableTintList(ColorStateList.valueOf(resources.getColor(R.color.primaryGrey)));
                    bikeButton = true
                    carButton = false
                    truckButton = false
                    motorcycleButton = false
                } else {
                    bike.setCompoundDrawableTintList(ColorStateList.valueOf(resources.getColor(R.color.primaryGrey)));
                    bikeButton = false
                }
            }

        }


    }

    fun openDatePicker(view: View) {
        when(view.id){
            (R.id.plate_date_input) -> {
                datePickerA = true
                datePickerB = false
                datePickerC = false
            }
            (R.id.last_revision_input) -> {
                datePickerA = false
                datePickerB = true
                datePickerC = false
            }
            (R.id.insurance_date_input) -> {
                datePickerA = false
                datePickerB = false
                datePickerC = true
            }
        }

        showDatePickerDialog();

    }



    fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            this,
            this,
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
        datePickerDialog.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val date = "$dayOfMonth/${month + 1}/$year"
        if(datePickerA){
            plate_date_input.text = date
        }else if(datePickerB){
            last_revision_input.text = date
        }else if(datePickerC){
            insurance_date_input.text = date
        }

    }
}