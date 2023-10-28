package com.enescanpolat.xoxoyunuonline

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.enescanpolat.xoxoyunuonline.databinding.ActivityPlayerNameBinding

class PlayerName : AppCompatActivity() {

    lateinit var binding: ActivityPlayerNameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityPlayerNameBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.StartGameBtn.setOnClickListener {

            val getPlayerName = binding.PlayerNameText.text.toString()

            if (getPlayerName.isEmpty()){
                Toast.makeText(this,"Lutfen kullanici adinizi giriniz", Toast.LENGTH_LONG).show()
            }
            else{
                val intent = Intent(this@PlayerName,MainActivity::class.java)
                intent.putExtra("playerName",getPlayerName)
                startActivity(intent)
                finish()
            }


        }
    }
}