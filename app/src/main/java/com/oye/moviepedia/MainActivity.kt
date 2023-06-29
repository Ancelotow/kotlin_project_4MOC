package com.oye.moviepedia

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.oye.moviepedia.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var loaderDialog : AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)
    }

    fun showLoader(){
        if (loaderDialog?.isShowing == true){
            return
        }
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        val alertDialog = builder.create()
        val view= LayoutInflater.from(this).inflate(R.layout.loader, null)
        alertDialog.setView(view)
        alertDialog.window?.setDimAmount(0.4f)
        alertDialog.window?.setBackgroundDrawableResource(R.color.transparent)
        alertDialog.show()
        loaderDialog = alertDialog
    }

    fun hideLoader(){
        loaderDialog?.dismiss()
    }
}