package com.example.android.doors

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.android.doors.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var selectedDoor: String
    private var points = 0
    private var attempts = 0

    private lateinit var binding: ActivityMainBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.selectButton.setOnClickListener{
            if (attempts < 10){
                val message = if (selectedDoor == Door.getDoor()){
                    attempts++
                    points++
                    "Correct!"
                } else {
                    attempts++
                    "Wrong!"
                }
                attempt_counter.text = "Attempts: $attempts/10"
                point_counter.text = "Correct answers: $points"
                val myToast = Toast.makeText(this, "Door Selected: $selectedDoor. $message", Toast.LENGTH_SHORT)
                myToast.show()
            } else{
                val myToast = Toast.makeText(this, "Game Over", Toast.LENGTH_SHORT)
                myToast.show()
            }

        }

        binding.clearButton.setOnClickListener{
            attempts = 0
            points = 0

            attempt_counter.text = "Attempts: $attempts/10"
            point_counter.text = "Correct answers: $points"

            clear_button.visibility = View.GONE
            binding.selectButton.visibility = View.GONE

            clearDoorCoor()

            val clearToast = Toast.makeText(this, "Cleared!", Toast.LENGTH_SHORT)
            clearToast.show()
        }

        binding.doorOne.setOnClickListener{
            changeDoorColor(binding.doorOne, binding.doorTwo)
            binding.selectButton.visibility = View.VISIBLE
            clear_button.visibility = View.VISIBLE
            selectedDoor = "door one"
        }
        binding.doorTwo.setOnClickListener{
            changeDoorColor(binding.doorTwo, binding.doorOne)
            binding.selectButton.visibility = View.VISIBLE
            clear_button.visibility = View.VISIBLE
            selectedDoor = "door two"
        }

    }
    private fun changeDoorColor(selectedDoor: TextView, otherDoor: TextView){
        selectedDoor.setBackgroundColor(Color.parseColor("#BF9103"))
        otherDoor.setBackgroundColor(Color.parseColor("#D1A41E"))
    }

    private fun clearDoorCoor(){
        binding.doorOne.setBackgroundColor(Color.parseColor("#D1A41E"))
        binding.doorTwo.setBackgroundColor(Color.parseColor("#D1A41E"))
    }
}
