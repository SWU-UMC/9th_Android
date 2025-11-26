package com.example.week2

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.week2.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 회원가입 버튼 누르면 이동
        binding.btnGoSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        val userDB = SongDatabase.getInstance(this)!!

        binding.btnLoginAction.setOnClickListener {
            val email = binding.etId.text.toString()
            val password = binding.etPw.text.toString()

            Thread {
                // DB에서 유저 조회
                val user = userDB.userDao().getUser(email, password)

                if (user != null) {
                    val sharedPreferences = getSharedPreferences("auth", MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putInt("jwtId", user.id) // 유저 고유 ID 저장
                    editor.apply()

                    runOnUiThread {
                        Toast.makeText(this, "로그인 성공!", Toast.LENGTH_SHORT).show()
                        finish() // 로그인 창 닫고 보관함으로 복귀
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this, "로그인 실패. 정보를 확인해주세요.", Toast.LENGTH_SHORT).show()
                    }
                }
            }.start()
        }
    }
}