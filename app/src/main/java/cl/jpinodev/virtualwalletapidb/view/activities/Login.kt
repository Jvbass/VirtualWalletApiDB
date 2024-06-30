package cl.jpinodev.virtualwalletapidb.view.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import cl.jpinodev.virtualwalletapidb.data.appdata.SharedPreferencesHelper
import cl.jpinodev.virtualwalletapidb.data.local.database.AppDatabase
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

        //SharedPreferencesHelper.clearAll(this)

        binding.linkCrearCuenta.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

        /***Dependencias para el login de usuario***/
        // instancia de Retrofit que está configurada para comunicarse con la API.
        val userApiService =
            RetrofitHelper.getRetrofit().create(UserApiService::class.java)
        val dataBase = AppDatabase.getDatabase(this)

        //instancia de UsersRepositoryImpl
        val usersRepository = UsersRepositoryImpl(userApiService, dataBase.UserDao())
        //instancia de UsersUseCase le pasamos el repositorio (usersRepository).
        val usersUseCase = UsersUseCase(usersRepository)
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
        * observa el resultado del login en el livedata loginLD.
        * Si es exitosa (success) toma la respuesta de la api o db
        * y obtiene el valor (token)(se valida si el token viene vacio ).
        * Si falla captura el error y lo muestra en toast
        * */
        usersViewModel.loginLD.observe(this, Observer { result ->
            result.onSuccess { response ->
                val accessToken = response.body()?.accessToken.toString()
                val userIdFromDb = response.body()?.userId
                Log.i("LoginActvt", accessToken)
                Log.i("LoginActvt", userIdFromDb.toString())
                if (accessToken.isEmpty()) {
                    ToastUtils.showCustomToast(this, "Error al iniciar sesión")
                } else {
                    SharedPreferencesHelper.saveToken(this, accessToken)
                    usersViewModel.getConnectedUser(accessToken)
                    if (userIdFromDb != null) {
                        usersViewModel.getUserByIdFromDb(userIdFromDb)
                    }
                    Log.i("LoginActvt", accessToken)
                    ToastUtils.showCustomToast(this, "Login exitoso")
                }
                finish()
            }
            result.onFailure {
                ToastUtils.showCustomToast(this, "Error al iniciar sesión: ${it.message}")
            }
        })

        /*
        * Observa la obtencion del usuario conectado en el livedata connectedUserLD.
        * Si el resultado es exitoso (success) toma el usuario de la respuesta de la api o db ,
        * si falla muestra un toast
        * */
        usersViewModel.connectedUserLD.observe(this, Observer { result ->
            result.onSuccess { user->
                user?.let {
                    SharedPreferencesHelper.saveConnectedUser(this, user)
                    val intent = Intent(this, MainContainer::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            result.onFailure {
                ToastUtils.showCustomToast(this, "Error al obtener usuario: ${it.message}")
            }
        })
    }
}
