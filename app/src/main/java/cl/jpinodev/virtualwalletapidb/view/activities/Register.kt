package cl.jpinodev.virtualwalletapidb.view.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import cl.jpinodev.virtualwalletapidb.data.local.database.AppDatabase
import cl.jpinodev.virtualwalletapidb.data.model.entities.Users
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

        val userApiService =
            RetrofitHelper.getRetrofit().create(UserApiService::class.java)
        val dataBase = AppDatabase.getDatabase(this)
        val usersRepository = UsersRepositoryImpl(userApiService, dataBase.UserDao())
        val usersUseCase = UsersUseCase(usersRepository)
        val usersViewModel: UsersViewModel by viewModels { UsersViewModelFactory(usersUseCase) }



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
                val newUser = Users(
                    id = 0,
                    firstName = nombre,
                    lastName = apellido,
                    email = email,
                    password = contrasena
                )
                // Llamamos al metodo createUser del ViewModel para crear el usuario
                usersViewModel.createUser(newUser)

            }
        }

        // observer de cambios en userLiveDAta, llamamos la funcion handleSuccess o handleError
        usersViewModel.userLD.observe(this, Observer { result ->
            result.onSuccess { response ->
                val response = response.body()
                    Log.i("HomePageLog", response.toString())
                ToastUtils.showCustomToast(this, "Usuario registrado exitosamente")

                val intent = Intent(this, Login::class.java)
                startActivity(intent)
            }.onFailure { throwable ->
                ToastUtils.showCustomToast(this, "Error al registrar usuario: ${throwable.message}")
            }
        })

    } //end onCreate
}//end class