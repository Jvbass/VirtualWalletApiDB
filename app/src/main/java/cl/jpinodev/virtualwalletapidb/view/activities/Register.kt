package cl.jpinodev.virtualwalletapidb.view.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import cl.jpinodev.virtualwalletapidb.data.model.Users
import cl.jpinodev.virtualwalletapidb.data.network.api.UserApiService
import cl.jpinodev.virtualwalletapidb.data.network.retrofit.RetrofitHelper
import cl.jpinodev.virtualwalletapidb.data.repository.UsersRepositoryImpl
import cl.jpinodev.virtualwalletapidb.databinding.ActivityRegisterBinding
import cl.jpinodev.virtualwalletapidb.domain.UsersUseCase
import cl.jpinodev.virtualwalletapidb.view.utils.ToastUtils
import cl.jpinodev.virtualwalletapidb.viewmodel.UsersViewModel
import cl.jpinodev.virtualwalletapidb.viewmodel.UsersViewModelFactory
import retrofit2.Response

class Register : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // instancia de Retrofit que está configurada para comunicarse con la API.
        val userApiService: UserApiService = RetrofitHelper.getRetrofit().create(UserApiService::class.java)
        //instancia de UsersRepositoryImpl
        val usersRepository: UsersRepositoryImpl = UsersRepositoryImpl(userApiService)
        //instancia de UsersUseCase le pasamos el repositorio (usersRepository).
        val usersUseCase: UsersUseCase = UsersUseCase(usersRepository)
        //instancia de UsersViewModel usando fabrica UsersViewModelFactory
        val usersViewModel: UsersViewModel by viewModels { UsersViewModelFactory(usersUseCase) }


        // Observa los cambios en userLiveData
        usersViewModel.userLiveData.observe(this, Observer { result ->
            result.onSuccess { response ->
                handleSuccess(response)
            }.onFailure { throwable ->
                handleError(throwable)
            }
        })

        // Botón crear cuenta
        binding.crearCuentaBtn.setOnClickListener {
            val nombre = binding.nombreEditText.text.toString()
            val apellido = binding.apellidoEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val contrasena = binding.passwordEditText.text.toString()
            val contrasenaConfirm = binding.passwordRepeatEditText.text.toString()

            // Validando campos
            if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || contrasena.isEmpty()) {
                ToastUtils.showCustomToast(this, "Todos los campos son obligatorios")
            } else if (contrasenaConfirm != contrasena) {
                ToastUtils.showCustomToast(this, "Las contraseñas no coinciden")
            } else {
                val newUser = Users(
                    id = 0, // La API generará el ID
                    firstName = nombre,
                    lastName = apellido,
                    email = email,
                    password = contrasena
                )
                usersViewModel.createUser(newUser)
            }
        }
    }

    private fun handleSuccess(response: Response<Users>) {
        if (response.isSuccessful) {
            ToastUtils.showCustomToast(this, "Usuario registrado exitosamente")
            // Puedes redirigir al usuario a la pantalla de login o donde desees
        } else {
            ToastUtils.showCustomToast(this, "Error: ${response.message()}")
        }
    }

    private fun handleError(throwable: Throwable) {
        ToastUtils.showCustomToast(this, "Error al registrar usuario: ${throwable.message}")
    }
}