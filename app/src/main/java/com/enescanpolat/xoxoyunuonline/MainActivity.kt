package com.enescanpolat.xoxoyunuonline

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import com.enescanpolat.xoxoyunuonline.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding
    private var combinationList = mutableListOf<List<Int>>()
    private var doneBoxes = mutableListOf<String>()
    private var boxesSelectedby = arrayOf<String>("","","","","","","","","")
    private lateinit var playerUUID :String
    private lateinit var oppenentUUID :String
    private lateinit var connectionUUID:String
    private lateinit var getOpenentPlayerName:String
    private lateinit var turnsEventListener: ValueEventListener
    private lateinit var wonEventListener: ValueEventListener
    private lateinit var connectionId:String
    private var oppenentFound : Boolean = false
    private var playerFound:Boolean =false
    private var playerTurn =""

    private var databaseprefrence = FirebaseDatabase.getInstance().getReferenceFromUrl("https://xoxoyunuonline-default-rtdb.europe-west1.firebasedatabase.app")

    private lateinit var status:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val getPlayername = intent.getStringArrayExtra("playerName")


        val innerList1 = listOf<Int>(0,1,2)
        val innerList2 = listOf<Int>(3,4,5)
        val innerList3 = listOf<Int>(6,7,8)

        val innerList4 = listOf<Int>(0,3,6)
        val innerList5 = listOf<Int>(1,4,7)
        val innerList6 = listOf<Int>(2,5,8)

        val innerList7 = listOf<Int>(2,4,6)
        val innerList8 = listOf<Int>(0,4,8)


        combinationList.add(innerList1)
        combinationList.add(innerList2)
        combinationList.add(innerList3)
        combinationList.add(innerList4)
        combinationList.add(innerList5)
        combinationList.add(innerList6)
        combinationList.add(innerList7)
        combinationList.add(innerList8)

        val progressDialog = AlertDialog.Builder(this)
        val progressBar = ProgressBar(this)
        progressDialog.setView(progressBar)
        progressDialog.setMessage("Rakip Aranıyor")
        progressDialog.setCancelable(false)


        val dialog = progressDialog.create()
        dialog.show()



        playerUUID = System.currentTimeMillis().toString()


        binding.player1TV.setText(getPlayername.toString())
        databaseprefrence.child("connections").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!oppenentFound){

                    if (snapshot.hasChildren()){
                        for (connections in snapshot.children){
                            val conId = connections.key?.toString()?:""
                            val getPlayersCount = connections.childrenCount.toInt()

                            if (status.equals("waiting")){

                                if (getPlayersCount==2){
                                    playerTurn = playerUUID
                                    applyPlayerTurn(playerTurn)
                                    playerFound=false

                                    for (players in snapshot.children){

                                        val getPlayerUUID = players.key

                                        if (getPlayerUUID.equals(playerUUID)){
                                            playerFound=true
                                        }else if(playerFound){
                                            getOpenentPlayerName = players.child("player_name").getValue().toString()
                                            oppenentUUID = players.key.toString()

                                            binding.player2TV.setText(getOpenentPlayerName)

                                            connectionId = conId
                                            oppenentFound = true

                                            databaseprefrence.child("turns").child(connectionId).addValueEventListener(turnsEventListener)
                                            databaseprefrence.child("won").child(connectionId).addValueEventListener(wonEventListener)


                                            if (dialog.isShowing){
                                                dialog.dismiss()
                                            }

                                            databaseprefrence.child("connections").removeEventListener(this)


                                        }


                                    }


                                }else{
                                    if(getPlayersCount==1){

                                        connections.child(playerUUID).child("player_name").ref.setValue(getPlayername)

                                        for (players in connections.children){
                                            val getOpenentName = players.child("player_name").getValue().toString()
                                            oppenentUUID = players.key.toString()

                                            playerTurn = oppenentUUID

                                            applyPlayerTurn(playerTurn)

                                            binding.player2TV.setText(getOpenentName)


                                            connectionId = conId
                                            oppenentFound=true

                                            databaseprefrence.child("turns").child(connectionId).addValueEventListener(turnsEventListener)
                                            databaseprefrence.child("won").child(connectionId).addValueEventListener(wonEventListener)

                                            if (dialog.isShowing){
                                                dialog.dismiss()
                                            }

                                            databaseprefrence.child("connections").removeEventListener(this)
                                        }
                                    }
                                }

                            }

                        }

                        if(!oppenentFound && !status.equals("waiting")){
                            connectionUUID = System.currentTimeMillis().toString()
                            snapshot.child(connectionUUID).child(playerUUID).child("player_name").ref.setValue(getPlayername)
                            status = "waiting"
                        }


                    }

                }else{
                    connectionUUID = System.currentTimeMillis().toString()
                    snapshot.child(connectionUUID).child(playerUUID).child("player_name").ref.setValue(getPlayername)
                    status = "waiting"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        turnsEventListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                snapshot.children.forEach {dataSnapshot ->

                    if (dataSnapshot.childrenCount.toInt()==2){
                        val getBoxPosition = dataSnapshot.child("box_position").getValue().toString()
                        val getPlayerId = dataSnapshot.child("player_id").getValue().toString()

                        if (!doneBoxes.contains(getBoxPosition)){

                            doneBoxes.add(getBoxPosition).toString()

                            if (getBoxPosition.toInt()==1){
                                selectedBox(binding.image1,getBoxPosition.toInt(),getPlayerId)

                            }
                            else if (getBoxPosition.toInt()==2){
                                selectedBox(binding.image2,getBoxPosition.toInt(),getPlayerId)
                            }
                            else if (getBoxPosition.toInt()==3){
                                selectedBox(binding.image3,getBoxPosition.toInt(),getPlayerId)
                            }
                            else if (getBoxPosition.toInt()==4){
                                selectedBox(binding.image4,getBoxPosition.toInt(),getPlayerId)
                            }
                            else if (getBoxPosition.toInt()==5){
                                selectedBox(binding.image5,getBoxPosition.toInt(),getPlayerId)
                            }
                            else if (getBoxPosition.toInt()==6){
                                selectedBox(binding.image6,getBoxPosition.toInt(),getPlayerId)
                            }
                            else if (getBoxPosition.toInt()==7){
                                selectedBox(binding.image7,getBoxPosition.toInt(),getPlayerId)
                            }
                            else if (getBoxPosition.toInt()==8){
                                selectedBox(binding.image8,getBoxPosition.toInt(),getPlayerId)
                            }
                            else if (getBoxPosition.toInt()==9){
                                selectedBox(binding.image9,getBoxPosition.toInt(),getPlayerId)
                            }

                        }


                    }

                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }

        wonEventListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.hasChild("player_id")){

                    val getWinPlayerId = snapshot.child("player_id").getValue().toString()

                    val vinDialog:winDialog

                    if (getWinPlayerId.equals(playerUUID)){

                        vinDialog = winDialog(this@MainActivity,MainActivity(),"You won the game")

                    }else{
                        vinDialog = winDialog(this@MainActivity,MainActivity(),"Openent won the game")
                    }

                    vinDialog.setCancelable(false)
                    vinDialog.show()

                    databaseprefrence.child("turns").child(connectionId).removeEventListener(turnsEventListener)
                    databaseprefrence.child("won").child(connectionId).removeEventListener(wonEventListener)

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }

        binding.image1.setOnClickListener {

            if (!doneBoxes.contains("1")&&playerTurn.equals(playerUUID)){
                binding.image1.setImageResource(R.drawable.x)

                databaseprefrence.child("turns").child(connectionId).child((doneBoxes.size+1).toString()).child("box_position").setValue("1")
                databaseprefrence.child("turns").child(connectionId).child((doneBoxes.size+1).toString()).child("player_id").setValue(playerUUID)

                playerTurn = oppenentUUID
            }


        }
        binding.image2.setOnClickListener {

            if (!doneBoxes.contains("2")&&playerTurn.equals(playerUUID)){
                binding.image2.setImageResource(R.drawable.x)

                databaseprefrence.child("turns").child(connectionId).child((doneBoxes.size+1).toString()).child("box_position").setValue("2")
                databaseprefrence.child("turns").child(connectionId).child((doneBoxes.size+1).toString()).child("player_id").setValue(playerUUID)

                playerTurn = oppenentUUID
            }


        }
        binding.image3.setOnClickListener {

            if (!doneBoxes.contains("3")&&playerTurn.equals(playerUUID)){
                binding.image3.setImageResource(R.drawable.x)

                databaseprefrence.child("turns").child(connectionId).child((doneBoxes.size+1).toString()).child("box_position").setValue("3")
                databaseprefrence.child("turns").child(connectionId).child((doneBoxes.size+1).toString()).child("player_id").setValue(playerUUID)

                playerTurn = oppenentUUID
            }


        }
        binding.image4.setOnClickListener {

            if (!doneBoxes.contains("4")&&playerTurn.equals(playerUUID)){
                binding.image4.setImageResource(R.drawable.x)

                databaseprefrence.child("turns").child(connectionId).child((doneBoxes.size+1).toString()).child("box_position").setValue("4")
                databaseprefrence.child("turns").child(connectionId).child((doneBoxes.size+1).toString()).child("player_id").setValue(playerUUID)

                playerTurn = oppenentUUID
            }


        }
        binding.image5.setOnClickListener {

            if (!doneBoxes.contains("5")&&playerTurn.equals(playerUUID)){
                binding.image5.setImageResource(R.drawable.x)

                databaseprefrence.child("turns").child(connectionId).child((doneBoxes.size+1).toString()).child("box_position").setValue("5")
                databaseprefrence.child("turns").child(connectionId).child((doneBoxes.size+1).toString()).child("player_id").setValue(playerUUID)

                playerTurn = oppenentUUID
            }


        }
        binding.image6.setOnClickListener {

            if (!doneBoxes.contains("6")&&playerTurn.equals(playerUUID)){
                binding.image6.setImageResource(R.drawable.x)

                databaseprefrence.child("turns").child(connectionId).child((doneBoxes.size+1).toString()).child("box_position").setValue("6")
                databaseprefrence.child("turns").child(connectionId).child((doneBoxes.size+1).toString()).child("player_id").setValue(playerUUID)

                playerTurn = oppenentUUID
            }


        }
        binding.image7.setOnClickListener {

            if (!doneBoxes.contains("7")&&playerTurn.equals(playerUUID)){
                binding.image7.setImageResource(R.drawable.x)

                databaseprefrence.child("turns").child(connectionId).child((doneBoxes.size+1).toString()).child("box_position").setValue("7")
                databaseprefrence.child("turns").child(connectionId).child((doneBoxes.size+1).toString()).child("player_id").setValue(playerUUID)

                playerTurn = oppenentUUID
            }


        }
        binding.image8.setOnClickListener {

            if (!doneBoxes.contains("8")&&playerTurn.equals(playerUUID)){
                binding.image2.setImageResource(R.drawable.x)

                databaseprefrence.child("turns").child(connectionId).child((doneBoxes.size+1).toString()).child("box_position").setValue("8")
                databaseprefrence.child("turns").child(connectionId).child((doneBoxes.size+1).toString()).child("player_id").setValue(playerUUID)

                playerTurn = oppenentUUID
            }


        }
        binding.image9.setOnClickListener {

            if (!doneBoxes.contains("9")&&playerTurn.equals(playerUUID)){
                binding.image2.setImageResource(R.drawable.x)

                databaseprefrence.child("turns").child(connectionId).child((doneBoxes.size+1).toString()).child("box_position").setValue("9")
                databaseprefrence.child("turns").child(connectionId).child((doneBoxes.size+1).toString()).child("player_id").setValue(playerUUID)

                playerTurn = oppenentUUID
            }


        }


    }



    private fun applyPlayerTurn(playerUUID2:String){

        if (playerUUID2.equals(playerUUID)){
            binding.player1Layout.setBackgroundResource(R.drawable.round_back_dark_blue_stocke)
            binding.player2Layout.setBackgroundResource(R.drawable.rounded_back_dark_blue_20)
        }else{
            binding.player2Layout.setBackgroundResource(R.drawable.round_back_dark_blue_stocke)
            binding.player1Layout.setBackgroundResource(R.drawable.rounded_back_dark_blue_20)
        }

    }


    private fun selectedBox(imageView: ImageView,selectedBoxPosition:Int, selectedPlayer:String){

        boxesSelectedby[selectedBoxPosition-1] = selectedPlayer

        if (selectedPlayer.equals(playerUUID)){
            imageView.setImageResource(R.drawable.x)
            playerTurn = oppenentUUID
        }else{
            imageView.setImageResource(R.drawable.o)
            playerTurn = playerUUID
        }

        applyPlayerTurn(playerTurn)

        if (checkPlayerWin(selectedPlayer)){
            databaseprefrence.child("won").child(connectionId).child("player_id").setValue(selectedPlayer)
        }

        if (doneBoxes.size.toInt()==9) run {

            val windialog: winDialog = winDialog(this,MainActivity(),"it is a draw")
            windialog.setCancelable(false)
            windialog.show()

        }


    }

    private fun checkPlayerWin(playerId:String):Boolean{

        var isplayerwon = false

        for (i in 0 until  combinationList.size) {


            var  combination = combinationList.get(index = i).toTypedArray()

            if (boxesSelectedby[combination[0]].equals(playerId)&&
                boxesSelectedby[combination[1]].equals(playerId)&&
                boxesSelectedby[combination[2]].equals(playerId)
            ){
                isplayerwon=true
            }

        }

        return isplayerwon

    }
}