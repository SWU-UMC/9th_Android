package com.example.umc_dori_week1

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DimenRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.umc_dori_week1.databinding.ActivityMainBinding


fun Context.getFloatCompat(@DimenRes resId: Int): Float {
    val outValue = TypedValue()
    resources.getValue(resId, outValue, true)
    return outValue.float
}


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    private var currentEnlargedImage: ImageView? = null
    private var currentEnlargedTextView: TextView? = null
    private var selectedEmotionId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        binding.ivEmotionHappy.setOnClickListener {
            handleEmotionClick(binding.ivEmotionHappy, binding.tvEmotionHappy, R.string.emotion_happy)
        }

        binding.ivEmotionExcited.setOnClickListener {
            handleEmotionClick(binding.ivEmotionExcited, binding.tvEmotionExcited, R.string.emotion_excited)
        }

        binding.ivEmotionNormal.setOnClickListener {
            handleEmotionClick(binding.ivEmotionNormal, binding.tvEmotionNormal, R.string.emotion_normal)
        }

        binding.ivEmotionNervous.setOnClickListener {
            handleEmotionClick(binding.ivEmotionNervous, binding.tvEmotionNervous, R.string.emotion_nervous)
        }


        binding.ivEmotionAngry.setOnClickListener {
            handleEmotionClick(binding.ivEmotionAngry, binding.tvEmotionAngry, R.string.emotion_angry)
        }
    }



    private fun handleEmotionClick(clickedImage: ImageView, clickedTextView: TextView, emotionStringResId: Int) {

        handleClick(clickedImage, clickedTextView)
        val emotionName = getString(emotionStringResId)
        val toastMessage = getString(R.string.emotion_clicked_message, emotionName)
        Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()
    }



    private fun handleClick(clickedImage: ImageView, clickedTextView: TextView) {
        val currentId = clickedImage.id

        if (selectedEmotionId == currentId) {
            reset(clickedImage, clickedTextView)
            selectedEmotionId = 0
            currentEnlargedImage = null
            currentEnlargedTextView = null
        } else {


            if (currentEnlargedImage != null) {
                reset(currentEnlargedImage!!, currentEnlargedTextView!!)
            }

            changeScale(clickedImage, clickedTextView)
            currentEnlargedImage = clickedImage
            currentEnlargedTextView = clickedTextView
            selectedEmotionId = currentId
        }
    }


    private fun changeScale(imageView: ImageView, textView: TextView) {
        val scaleUp = getFloatCompat(R.dimen.scale_up_size)
        imageView.animate()
            .scaleX(scaleUp)
            .scaleY(scaleUp)
            .setDuration(300)
            .start()

        textView.setTextColor(ContextCompat.getColor(this, R.color.deep_blue))
    }

    private fun reset(imageView: ImageView, textView: TextView){
        val scaleReset = getFloatCompat(R.dimen.scale_reset_size)
        imageView.animate()
            .scaleX(scaleReset)
            .scaleY(scaleReset)
            .setDuration(300)
            .start()

        textView.setTextColor(ContextCompat.getColor(this, R.color.black))
    }
}