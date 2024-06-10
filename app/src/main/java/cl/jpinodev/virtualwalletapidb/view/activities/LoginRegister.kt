package cl.jpinodev.virtualwalletapidb.view.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cl.jpinodev.virtualwalletapidb.R
import cl.jpinodev.virtualwalletapidb.databinding.ActivityLoginRegisterBinding

class LoginRegister : AppCompatActivity() {
    private lateinit var binding: ActivityLoginRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnCrearCuenta.setOnClickListener{
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
        binding.linkLoginCuenta.setOnClickListener{
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
    } // end of onCreate
} // end of class