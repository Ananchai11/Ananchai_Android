package com.rmutto.ananchai_android

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.TextView

class ShowdataActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_showdata)

        val button3 = findViewById<Button>(R.id.button3)
        val button5 = findViewById<Button>(R.id.button5)

        button3.setOnClickListener{
            val intent = Intent(this, InsertActivity::class.java)
            startActivity(intent)
        }

        button5.setOnClickListener{
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        // Initialize ApiService
        apiService = RetrofitClient.getClient().create(ApiService::class.java)

        // รับค่า ID จาก Intent
        val computerId = intent.getIntExtra("COMPUTER_ID", -1)

        if (computerId != -1) {
            // เรียก API เพื่อดึงข้อมูลของคอมพิวเตอร์ตาม ID
            loadComputerData(computerId)
        } else {
            Toast.makeText(this, "ไม่พบ ID ของคอมพิวเตอร์", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadComputerData(computerId: Int) {
        apiService.getComputerById(computerId).enqueue(object : Callback<Computer> {
            override fun onResponse(call: Call<Computer>, response: Response<Computer>) {
                if (response.isSuccessful) {
                    val computer = response.body()
                    if (computer != null) {
                        displayComputerData(computer)
                    } else {
                        // ถ้าไม่พบข้อมูลในฐานข้อมูล
                        Toast.makeText(this@ShowdataActivity, "ไม่พบข้อมูลสำหรับ ID ที่ระบุ", Toast.LENGTH_SHORT).show()
                        finish() // ปิด Activity นี้และส่งผู้ใช้กลับไปหน้า SearchActivity
                    }
                } else {
                    Toast.makeText(this@ShowdataActivity, "ไม่สามารถดึงข้อมูลได้", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Computer>, t: Throwable) {
                Toast.makeText(this@ShowdataActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun displayComputerData(computer: Computer) {
        findViewById<TextView>(R.id.textViewbrand_name).text = computer.brand_name
        findViewById<TextView>(R.id.textViewmodel_name).text = computer.model_name
        findViewById<TextView>(R.id.textViewserial_number).text = computer.serial_number
        findViewById<TextView>(R.id.textViewstock_quantity).text = computer.stock_quantity.toString()
        findViewById<TextView>(R.id.textViewprice).text = computer.price.toString()
        findViewById<TextView>(R.id.textViewcpu_speed).text = computer.cpu_speed.toString()
        findViewById<TextView>(R.id.textViewmemory_capacity).text = computer.memory_capacity.toString()
        findViewById<TextView>(R.id.textViewhard_disk_capacity).text = computer.hard_disk_capacity.toString()
    }
}
