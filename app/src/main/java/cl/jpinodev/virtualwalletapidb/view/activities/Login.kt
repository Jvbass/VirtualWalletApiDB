package cl.jpinodev.virtualwalletapidb.view.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import cl.jpinodev.virtualwalletapidb.data.appdata.SharedPreferences
import cl.jpinodev.virtualwalletapidb.data.network.api.UserApiService
import cl.jpinodev.virtualwalletapidb.data.network.retrofit.RetrofitHelper
import cl.jpinodev.virtualwalletapidb.data.repository.UsersRepositoryImpl
import cl.jpinodev.virtualwalletapidb.databinding.ActivityLoginBinding
import cl.jpinodev.virtualwalletapidb.domain.UsersUseCase
import cl.jpinodev.virtualwalletapidb.view.utils.ToastUtils
import cl.jpinodev.virtualwalletapidb.viewmodel.UsersViewModel
import cl.jpinodev.virtualwalletapidb.viewmodel.UsersViewModelFactory

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /***Dependencias para el login de usuario***/
        // instancia de Retrofit que está configurada para comunicarse con la API.
        val userApiService: UserApiService =
            RetrofitHelper.getRetrofit().create(UserApiService::class.java)
        //instancia de UsersRepositoryImpl
        val usersRepository: UsersRepositoryImpl = UsersRepositoryImpl(userApiService)
        //instancia de UsersUseCase le pasamos el repositorio (usersRepository).
        val usersUseCase: UsersUseCase = UsersUseCase(usersRepository)
        //instancia de UsersViewModel usando fabrica UsersViewModelFactory
        val usersViewModel: UsersViewModel by viewModels { UsersViewModelFactory(usersUseCase) }

        /*btnLogin*/
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            if (email.isEmpty() || password.isEmpty()) {
                ToastUtils.showCustomToast(this, "Todos los campos son obligatorios")
            } else {
                usersViewModel.loginUser(email, password)
            }
        }

        /*
        *  Observador para el login de usuario
        * */
        usersViewModel.loginLD.observe(this, Observer { result ->
            result.onSuccess { response ->
                val accessToken = response.body()?.accessToken.toString()
                if (accessToken.isEmpty()) {
                    ToastUtils.showCustomToast(this, "Error al iniciar sesión")
                } else {
                    SharedPreferences.saveToken(this, accessToken)
                    usersViewModel.getConnectedUser(accessToken)
                    Log.i("TOKEN", accessToken)
                    ToastUtils.showCustomToast(this, "Login exitoso")
                    val intent = Intent(this, MainContainer::class.java)
                    startActivity(intent) //si la respuesta es exitosa se inicia la actividad
                    finish() // Llama a finish() para cerrar la actividad de login
                }
            }
            result.onFailure {
                Log.i("ErrorLogin", it.message.toString())
                ToastUtils.showCustomToast(this, "Error al iniciar sesión: ${it.message}")
            }
        })

        usersViewModel.connectedUserLD.observe(this, Observer { result ->
            result.onSuccess { response ->
                val user = response.body()
                user?.let {
                    SharedPreferences.saveConnectedUser( this, it)
                }
            }
            result.onFailure {
                ToastUtils.showCustomToast(this, "Error al obtener usuario: ${it.message}")
            }
        })
    }
}
