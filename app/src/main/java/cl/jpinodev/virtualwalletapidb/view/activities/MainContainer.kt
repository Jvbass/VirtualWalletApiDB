package cl.jpinodev.virtualwalletapidb.view.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import cl.jpinodev.virtualwalletapidb.data.appdata.SharedPreferencesHelper
import cl.jpinodev.virtualwalletapidb.databinding.ActivityMainContainerBinding

class MainContainer : AppCompatActivity() {

    private lateinit var binding: ActivityMainContainerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainContainerBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

   override fun onDestroy() {
        super.onDestroy()
        SharedPreferencesHelper.clearAll(this)
    }
}