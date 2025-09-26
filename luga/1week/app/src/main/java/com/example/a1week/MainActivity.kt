package com.example.a1week

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.a1week.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // 뷰바인딩 객체 선언
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 바인딩 초기화
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 클릭 이벤트
        faceClick(binding.ivFaceYellow, binding.tvFaceYellow, R.color.color_face_yellow)
        faceClick(binding.ivFaceBlue, binding.tvFaceBlue, R.color.color_face_blue)
        faceClick(binding.ivFacePurple, binding.tvFacePurple, R.color.color_face_purple)
        faceClick(binding.ivFaceGreen, binding.tvFaceGreen, R.color.color_face_green)
        faceClick(binding.ivFaceRed, binding.tvFaceRed, R.color.color_face_red)
    }

    // 클릭 이벤트 처리 함수
    private fun faceClick(faceView: ImageView, textView: TextView, colorId: Int){
        faceView.setOnClickListener {
            Log.d("DEBUG", "클릭됨: ${textView.text}")

            textView.setTextColor(ContextCompat.getColor(this, colorId))
            Toast.makeText(this, textView.text.toString(), Toast.LENGTH_SHORT).show()
        }
    }
}