package com.rmutto.ananchai_android

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val searchButton = findViewById<Button>(R.id.buttonsearch)
        val inputButton = findViewById<Button>(R.id.buttonkup)
        val searchEditText = findViewById<EditText>(R.id.editTextsearch)

        // กดปุ่ม "ค้นหา" เพื่อค้นหาข้อมูลตาม ID ที่กรอก
        searchButton.setOnClickListener {
            val idInput = searchEditText.text.toString()

            if (idInput.isNotEmpty()) {
                val computerId = idInput.toIntOrNull()
                if (computerId != null) {
                    // ส่ง ID ไปยัง ShowdataActivity
                    val intent = Intent(this, ShowdataActivity::class.java)
                    intent.putExtra("COMPUTER_ID", computerId)
                    startActivity(intent)
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
