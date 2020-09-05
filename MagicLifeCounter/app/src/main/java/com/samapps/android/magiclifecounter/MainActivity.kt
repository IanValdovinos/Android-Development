package com.samapps.android.magiclifecounter

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.samapps.android.magiclifecounter.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    var player1: Player = Player(50)
    var player2: Player = Player(50)

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        refreshCounter()
        setCounterColor(player1, binding.player1Counter)
        setCounterColor(player2, binding.player2Counter)

        binding.player1PlusButton.setOnClickListener{
            player1.life += 1
            refreshCounter()
            setCounterColor(player1, binding.player1Counter)
        }
        binding.player1MinusButton.setOnClickListener{
            player1.life -= 1
            refreshCounter()
            setCounterColor(player1, binding.player1Counter)
        }


        binding.player2PlusButton.setOnClickListener {
            player2.life += 1
            refreshCounter()
            setCounterColor(player2, binding.player2Counter)
        }
        binding.player2MinusButton.setOnClickListener {
            player2.life -= 1
            refreshCounter()
            setCounterColor(player2, binding.player2Counter)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setCounterColor(player: Player, view: TextView){
        if (player.life >= 0){
            when(player.life){
                in 0..15 -> view.setTextColor(getColor(R.color.red))
                in 16..25 -> view.setTextColor(getColor(R.color.orange))
                in 26..40 -> view.setTextColor(getColor(R.color.blue))
                else -> view.setTextColor(getColor(R.color.green))
            }
        }
    }

    private fun refreshCounter(){
        binding.player1Counter.text = player1.life.toString()
        binding.player2Counter.text = player2.life.toString()
    }
}
