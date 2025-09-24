package com.example.a1week

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 클릭 이벤트
        faceClick(R.id.yellowFace, R.id.yellowText, R.color.yellowFace)
        faceClick(R.id.blueFace, R.id.blueText, R.color.blueFace)
        faceClick(R.id.purpleFace, R.id.purpleText, R.color.purpleFace)
        faceClick(R.id.greenFace, R.id.greenText, R.color.greenFace)
        faceClick(R.id.redFace, R.id.redText, R.color.redFace)
    }

    // 클릭 이벤트 처리 함수
    private fun faceClick(faceId : Int, textId : Int, colorId : Int){
        val face = findViewById<ImageView>(faceId)
        val faceText = findViewById<TextView>(textId)

        face.setOnClickListener {
            // 텍스트 색상 변경
            faceText.setTextColor(ContextCompat.getColor(this, colorId))
            // 토스트 메세지
            val message = faceText.text.toString()
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
}