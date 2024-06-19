package cl.jpinodev.virtualwalletapidb.view.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import cl.jpinodev.virtualwalletapidb.data.model.apientities.UsersResponse
import cl.jpinodev.virtualwalletapidb.data.network.api.UserApiService
import cl.jpinodev.virtualwalletapidb.data.network.retrofit.RetrofitHelper
import cl.jpinodev.virtualwalletapidb.data.repository.UsersRepositoryImpl
import cl.jpinodev.virtualwalletapidb.databinding.ActivityRegisterBinding
import cl.jpinodev.virtualwalletapidb.domain.UsersUseCase
import cl.jpinodev.virtualwalletapidb.view.utils.ToastUtils
import cl.jpinodev.virtualwalletapidb.viewmodel.UsersViewModel
import cl.jpinodev.virtualwalletapidb.viewmodel.UsersViewModelFactory

class Register : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.linkLogin.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        // instancia de Retrofit que está configurada para comunicarse con la API.
        val userApiService: UserApiService = RetrofitHelper.getRetrofit().create(UserApiService::class.java)
        //instancia de UsersRepositoryImpl
        val usersRepository: UsersRepositoryImpl = UsersRepositoryImpl(userApiService)
        //instancia de UsersUseCase le pasamos el repositorio (usersRepository).
        val usersUseCase: UsersUseCase = UsersUseCase(usersRepository)
        //instancia de UsersViewModel usando fabrica UsersViewModelFactory
        val usersViewModel: UsersViewModel by viewModels { UsersViewModelFactory(usersUseCase) }


        // observer de cambios en userLiveDAta, llamamos la funcion handleSuccess o handleError
        usersViewModel.userLD.observe(this, Observer { result ->
            result.onSuccess { response ->
                //TODO("Implementar crear cuenta con id de usuario ? ")
                ToastUtils.showCustomToast(this, "Usuario registrado exitosamente")
            }.onFailure { throwable ->
                ToastUtils.showCustomToast(this, "Error al registrar usuario: ${throwable.message}")
            }
        })

        // Boton para crear cuenta
        binding.crearCuentaBtn.setOnClickListener {
            val nombre = binding.nombreEditText.text.toString()
            val apellido = binding.apellidoEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val contrasena = binding.passwordEditText.text.toString()
            val contrasenaConfirm = binding.passwordRepeatEditText.text.toString()

            // validacion de campos vacios y contraseña
            if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || contrasena.isEmpty()) {
                ToastUtils.showCustomToast(this, "Todos los campos son obligatorios")
            } else if (contrasenaConfirm != contrasena) {
                ToastUtils.showCustomToast(this, "Las contraseñas no coinciden")
            } else {
                //creamos nuevo usuario con los datos ingresados
                val newUser = UsersResponse(
                    id = 0,
                    firstName = nombre,
                    lastName = apellido,
                    email = email,
                    password = contrasena
                )
                // Llamamos al metodo createUser del ViewModel para crear el usuario
                usersViewModel.createUser(newUser)
              val intent = Intent(this, Login::class.java)
               startActivity(intent)
            }
        }
    }
}