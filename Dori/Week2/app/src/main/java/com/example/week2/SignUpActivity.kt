package com.example.week2

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.week2.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userDB = SongDatabase.getInstance(this)!!

        binding.btnSignUpFinish.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val name = binding.etName.text.toString()

            // 간단한 유효성 검사
            if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
                Toast.makeText(this, "모든 정보를 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            Thread {
                val user = User(email = email, password = password, name = name)
                userDB.userDao().insert(user)

                runOnUiThread {
                    Toast.makeText(this, "회원가입 성공!", Toast.LENGTH_SHORT).show()
                    finish() // 회원가입 끝나면 로그인 화면으로 돌아가기
                }
            }.start()
        }
    }
}