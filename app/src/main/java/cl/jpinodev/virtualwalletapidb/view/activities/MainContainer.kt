package cl.jpinodev.virtualwalletapidb.view.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cl.jpinodev.virtualwalletapidb.data.appdata.SharedPreferencesHelper
import cl.jpinodev.virtualwalletapidb.databinding.ActivityMainContainerBinding

class MainContainer : AppCompatActivity() {

    private lateinit var binding: ActivityMainContainerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainContainerBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


    override fun onBackPressed() {
        // Limpiar SharedPreferences
        SharedPreferencesHelper.clearAll(this)

        // Navegar de regreso a LoginActivity
        val intent = Intent(this, Login::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish() // Llama a finish() para cerrar MainContainer
    }
}