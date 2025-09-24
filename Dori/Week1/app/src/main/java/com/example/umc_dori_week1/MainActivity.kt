package com.example.umc_dori_week1

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.nio.channels.IllegalChannelGroupException

class MainActivity : AppCompatActivity() {

    private var currentEnlargedImage: ImageView? = null
    private var currentEnlargedTextView: TextView? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)


        val happy = findViewById<ImageView>(R.id.main_happy_iv)
        val excited = findViewById<ImageView>(R.id.main_excited_iv)
        val Normal = findViewById<ImageView>(R.id.main_Normal_iv)
        val nervous = findViewById<ImageView>(R.id.main_nervous_iv)
        val angry = findViewById<ImageView>(R.id.main_angry_iv)

        val happy_tv = findViewById<TextView>(R.id.main_happy_tv)
        val excited_tv = findViewById<TextView>(R.id.main_excited_tv)
        val Normal_tv = findViewById<TextView>(R.id.main_Normal_tv)
        val nervous_tv = findViewById<TextView>(R.id.main_nervous_tv)
        val angry_tv = findViewById<TextView>(R.id.main_angry_tv)


        happy.setOnClickListener {

            handleClick(happy, happy_tv)
            Toast.makeText(this, "행복 버튼이 클릭 됐습니다!", Toast.LENGTH_SHORT).show()
        }

        excited.setOnClickListener {
            handleClick(excited, excited_tv)
            Toast.makeText(this, "흥분 버튼이 클릭 됐습니다!", Toast.LENGTH_SHORT).show()
        }

        Normal.setOnClickListener {

            handleClick(Normal, Normal_tv)
            Toast.makeText(this, "평범 버튼이 클릭 됐습니다!", Toast.LENGTH_SHORT).show()
        }

        nervous.setOnClickListener {

            handleClick(nervous, nervous_tv)
            Toast.makeText(this, "불안 버튼이 클릭 됐습니다!", Toast.LENGTH_SHORT).show()
        }

        angry.setOnClickListener {

            handleClick(angry, angry_tv)
            Toast.makeText(this, "분노 버튼이 클릭 됐습니다!", Toast.LENGTH_SHORT).show()
        }

    }


    private fun handleClick(clickedImage: ImageView, clickedTextView: TextView) {

        if (currentEnlargedImage != null) {
            reset(currentEnlargedImage!!, currentEnlargedTextView!!)
        }

        Change(clickedImage, clickedTextView)
        currentEnlargedImage = clickedImage
        currentEnlargedTextView = clickedTextView

    }



    private fun Change(imageView: ImageView, textView: TextView) {
        val ChangeSize = 200
        imageView.layoutParams.width = ChangeSize
        imageView.layoutParams.height = ChangeSize
        imageView.requestLayout()


        textView.setTextColor(ContextCompat.getColor(this, R.color.deep_blue))
    }

    private fun reset(imageView: ImageView, textView: TextView){
        val resetSize = 135
        imageView.layoutParams.width = resetSize
        imageView.layoutParams.height = resetSize
        imageView.requestLayout()

        textView.setTextColor(ContextCompat.getColor(this, R.color.black))



    }



}