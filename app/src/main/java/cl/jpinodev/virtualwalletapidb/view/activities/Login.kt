package cl.jpinodev.virtualwalletapidb.view.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import cl.jpinodev.virtualwalletapidb.data.appdata.SharedPreferencesHelper
import cl.jpinodev.virtualwalletapidb.data.local.database.AppDatabase
import cl.jpinodev.virtualwalletapidb.data.network.api.AccountApiService
import cl.jpinodev.virtualwalletapidb.data.network.api.UserApiService
import cl.jpinodev.virtualwalletapidb.data.network.retrofit.RetrofitHelper
import cl.jpinodev.virtualwalletapidb.data.repository.AccountsRepositoryImpl
import cl.jpinodev.virtualwalletapidb.data.repository.UsersRepositoryImpl
import cl.jpinodev.virtualwalletapidb.databinding.ActivityLoginBinding
import cl.jpinodev.virtualwalletapidb.domain.AccountsUseCase
import cl.jpinodev.virtualwalletapidb.domain.UsersUseCase
import cl.jpinodev.virtualwalletapidb.view.utils.ToastUtils
import cl.jpinodev.virtualwalletapidb.viewmodel.AccountsViewModel
import cl.jpinodev.virtualwalletapidb.viewmodel.AccountsViewModelFactory
import cl.jpinodev.virtualwalletapidb.viewmodel.UsersViewModel
import cl.jpinodev.virtualwalletapidb.viewmodel.UsersViewModelFactory

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.linkCrearCuenta.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

        //config users
        val userApiService =
            RetrofitHelper.getRetrofit().create(UserApiService::class.java)
        val dataBaseUser = AppDatabase.getDatabase(this)
        val usersRepository = UsersRepositoryImpl(userApiService, dataBaseUser.UserDao())
        val usersUseCase = UsersUseCase(usersRepository)
        val usersViewModel: UsersViewModel by viewModels { UsersViewModelFactory(usersUseCase) }

        //config arquitectura cuentas
        val accountApiService: AccountApiService =
            RetrofitHelper.getRetrofit().create(AccountApiService::class.java)
        val databaseAccount = AppDatabase.getDatabase(this)
        val accountRepository =
            AccountsRepositoryImpl(accountApiService, databaseAccount.AccountDao())
        val accountUseCase = AccountsUseCase(accountRepository)
        val accountsViewModel: AccountsViewModel by viewModels {
            AccountsViewModelFactory(
                accountUseCase
            )
        }

        /*btnLogin*/
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            if (email.isEmpty() || password.isEmpty()) {
                ToastUtils.showCustomToast(this, "Todos los campos son obligatorios")
            } else {
                //llamamos el motodo logiUser del viewmodel para activar su liveData
                usersViewModel.loginUser(email, password)
            }
        }

        /*
        *  Observador de la respuesta del login de usuario
        *
        * */
        usersViewModel.loginLD.observe(this, Observer { result ->
            result.onSuccess { response ->
                //si hizo login desde api devuelve token desde la api y userId = 0
                // si hizo login desde db devuelve tokenfake y userId del usuario encontrado
                val accessToken = response.body()?.accessToken
                val userIdFromDb = response.body()?.userId
                if (accessToken != null && userIdFromDb != null) {
                    if (userIdFromDb <= 0) { // login desde api si userId es 0
                        //llamamos el metodo para obtener y la cuenta y usuario conectado desde api y activar el liveData
                        usersViewModel.getConnectedUser(accessToken)

                        accountsViewModel.getOwnAccountsFromApi("Bearer $accessToken")
                        //guardamos el token en el sharedPreferences
                        SharedPreferencesHelper.saveToken(this, accessToken)
                    } else {//login desde db
                        //llamamos el metodo para obtener y la cuenta y usuario conectado desde db y activar el liveData
                        usersViewModel.getUserByIdFromDb(userIdFromDb)
                        accountsViewModel.getOwnAccountsFromDBbyUserId(userIdFromDb)
                    }
                }
                ToastUtils.showCustomToast(this, "Login exitoso")
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
            result.onSuccess { user ->
                if (user != null) {
                    val userId: Int = user.id
                    SharedPreferencesHelper.saveConnectedUser(this, user)
                }
            }
            result.onFailure {
                ToastUtils.showCustomToast(this, "Error al obtener usuario: ${it.message}")
            }
        })

        accountsViewModel.ownAccountsLD.observe(this, Observer { result ->
            result.onSuccess { accounts ->
                if (!accounts.isNullOrEmpty()) {
                    SharedPreferencesHelper.saveAccount(this, accounts[0])
                    Log.d("TAGaccDB", "Login: ${accounts[0]}")
                }
                val intent = Intent(this, MainContainer::class.java)
                startActivity(intent)
            }
            result.onFailure {
                ToastUtils.showCustomToast(this, "Error al obtener cuentas: ${it.message}")
                val intent = Intent(this, MainContainer::class.java)
                startActivity(intent)
            }
        })
    } //end of oncreate
}//end of class

