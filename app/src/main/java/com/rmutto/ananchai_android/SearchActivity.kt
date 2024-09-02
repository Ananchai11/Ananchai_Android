package com.rmutto.ananchai_android

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        apiService = RetrofitClient.getClient().create(ApiService::class.java)

        val searchButton = findViewById<Button>(R.id.buttonsearch)
        val inputButton = findViewById<Button>(R.id.buttonkup)
        val searchEditText = findViewById<EditText>(R.id.editTextsearch)

        searchButton.setOnClickListener {
            val idInput = searchEditText.text.toString()

            if (idInput.isNotEmpty()) {
                val computerId = idInput.toIntOrNull()
                if (computerId != null) {
                    // เรียก API เพื่อตรวจสอบว่ามี ID นี้อยู่หรือไม่
                    apiService.getComputerById(computerId).enqueue(object : Callback<Computer> {
                        override fun onResponse(call: Call<Computer>, response: Response<Computer>) {
                            if (response.isSuccessful && response.body() != null) {
                                // ส่ง ID ไปยัง ShowdataActivity
                                val intent = Intent(this@SearchActivity, ShowdataActivity::class.java)
                                intent.putExtra("COMPUTER_ID", computerId)
                                startActivity(intent)
                            } else {
                                Toast.makeText(this@SearchActivity, "ไม่พบข้อมูลคอมพิวเตอร์ที่มี ID นี้", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<Computer>, t: Throwable) {
                            Toast.makeText(this@SearchActivity, "เกิดข้อผิดพลาดในการเชื่อมต่อ", Toast.LENGTH_SHORT).show()
                        }
                    })
                } else {
                    Toast.makeText(this, "กรุณากรอก ID ที่ถูกต้อง", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "กรุณากรอก ID", Toast.LENGTH_SHORT).show()
            }
        }

        // กดปุ่ม "กรอกข้อมูล" เพื่อกลับไปยังหน้า InsertActivity
        inputButton.setOnClickListener {
            val intent = Intent(this, InsertActivity::class.java)
            startActivity(intent)
        }
    }
}
