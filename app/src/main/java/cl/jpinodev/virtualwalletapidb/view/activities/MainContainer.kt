package cl.jpinodev.virtualwalletapidb.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cl.jpinodev.virtualwalletapidb.databinding.ActivityMainContainerBinding

class MainContainer : AppCompatActivity() {

    private lateinit var binding: ActivityMainContainerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainContainerBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}