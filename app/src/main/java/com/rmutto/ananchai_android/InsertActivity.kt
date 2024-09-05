package com.rmutto.ananchai_android

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.InputStream

class InsertActivity : AppCompatActivity() {
    private lateinit var apiService: ApiService
    private lateinit var selectedImageUri: Uri
    private lateinit var imageViewSelectedImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_insert)

        // Initialize Retrofit and ApiService
        apiService = RetrofitClient.getClient().create(ApiService::class.java)

        // Handle window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val button2 = findViewById<Button>(R.id.button2)
        val saveButton = findViewById<Button>(R.id.button4)
        val selectImageButton = findViewById<Button>(R.id.button_select_image)
        imageViewSelectedImage = findViewById(R.id.imageView_selected_image)
        val brandNameEditText = findViewById<EditText>(R.id.editTextbrand_name)
        val modelNameEditText = findViewById<EditText>(R.id.editTextmodel_name)
        val serialNumberEditText = findViewById<EditText>(R.id.editTextserial_number)
        val stockQuantityEditText = findViewById<EditText>(R.id.editTextstock_quantity)
        val priceEditText = findViewById<EditText>(R.id.editTextprice)
        val cpuSpeedEditText = findViewById<EditText>(R.id.editTextcpu_speed)
        val memoryCapacityEditText = findViewById<EditText>(R.id.editTextmemory_capacity)
        val hardDiskCapacityEditText = findViewById<EditText>(R.id.editTexthard_disk_capacity)

        // Set up listener for the "Search" button
        button2.setOnClickListener{
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        // Set up listener for the "Select Image" button
        selectImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 1000)
        }

        // Set up listener for the "Save" button
        saveButton.setOnClickListener {
            val brandName = brandNameEditText.text.toString()
            val modelName = modelNameEditText.text.toString()
            val serialNumber = serialNumberEditText.text.toString()
            val stockQuantity = stockQuantityEditText.text.toString().toIntOrNull() ?: 0
            val price = priceEditText.text.toString().toDoubleOrNull() ?: 0.0
            val cpuSpeed = cpuSpeedEditText.text.toString()
            val memoryCapacity = memoryCapacityEditText.text.toString()
            val hardDiskCapacity = hardDiskCapacityEditText.text.toString()

            // Convert image to base64 or a file
            val imageStream: InputStream? = contentResolver.openInputStream(selectedImageUri)
            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImageUri)
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray = stream.toByteArray()

            // Create a Computer object
            val imageUriString = selectedImageUri.toString()  // เก็บ URI ของภาพเป็น String

            val computer = Computer(
                image = imageUriString, // ส่งรูปภาพในรูปแบบ ByteArray
                brand_name = brandName,
                model_name = modelName,
                serial_number = serialNumber,
                stock_quantity = stockQuantity,
                price = price,
                cpu_speed = cpuSpeed,
                memory_capacity = memoryCapacity,
                hard_disk_capacity = hardDiskCapacity
            )

            // Call the API to save the computer
            apiService.addComputer(computer).enqueue(object : Callback<ApiResponse> {
                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@InsertActivity, "Computer added successfully!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@InsertActivity, "Failed to add computer!", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    Toast.makeText(this@InsertActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 1000) {
            selectedImageUri = data?.data ?: return
            imageViewSelectedImage.setImageURI(selectedImageUri)
        }
    }
}
