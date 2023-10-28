package com.enescanpolat.xoxoyunuonline

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.NonNull
import com.enescanpolat.xoxoyunuonline.databinding.WinDialogLayoutBinding

class winDialog(context: Context,private val mainActivity: MainActivity,private val message:String) : Dialog(context){

    private  var binding:WinDialogLayoutBinding



    init {
        setContentView(R.layout.win_dialog_layout)

        binding = WinDialogLayoutBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.messageTV.text = message

        binding.starnewgamebtn.setOnClickListener {
            dismiss()
            context.startActivity(Intent(context, PlayerName::class.java))
            mainActivity.finish()
        }
    }


}