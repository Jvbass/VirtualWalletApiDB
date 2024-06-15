package cl.jpinodev.virtualwalletapidb.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import cl.jpinodev.virtualwalletapidb.R
import cl.jpinodev.virtualwalletapidb.data.appdata.SharedPreferences
import cl.jpinodev.virtualwalletapidb.data.network.api.AccountApiService
import cl.jpinodev.virtualwalletapidb.data.network.retrofit.RetrofitHelper
import cl.jpinodev.virtualwalletapidb.data.repository.AccountsRepositoryImpl
import cl.jpinodev.virtualwalletapidb.databinding.FragmentHomePageBinding
import cl.jpinodev.virtualwalletapidb.domain.AccountsUseCase
import cl.jpinodev.virtualwalletapidb.view.utils.ToastUtils
import cl.jpinodev.virtualwalletapidb.viewmodel.AccountsViewModel
import cl.jpinodev.virtualwalletapidb.viewmodel.AccountsViewModelFactory
import cl.jpinodev.virtualwalletapidb.viewmodel.UsersViewModel

class HomePage : Fragment() {
    private lateinit var binding: FragmentHomePageBinding
    private lateinit var accountsViewModel: AccountsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomePageBinding.bind(view)

        val accountService: AccountApiService =
            RetrofitHelper.getRetrofit().create(AccountApiService::class.java)
        val accountRepository: AccountsRepositoryImpl = AccountsRepositoryImpl(accountService)
        val accountUseCase: AccountsUseCase = AccountsUseCase(accountRepository)
        val factory = AccountsViewModelFactory(accountUseCase)

        accountsViewModel = ViewModelProvider(this, factory)[AccountsViewModel::class.java]

        //val navController = Navigation.findNavController(view)
        val user = SharedPreferences.getConnectedUser(requireContext())
        user?.let {
            val fullName = "${it.firstName} ${it.lastName}"
            binding.greetingName.text = fullName
        }

        val token = SharedPreferences.getToken(requireContext())
        Log.i("HomePage", token.toString())
        token?.let {
            accountsViewModel.getOwnAccounts("Bearer $token")
        }

        // Observar los cambios en ownAccountsLD
        accountsViewModel.ownAccountsLD.observe(viewLifecycleOwner, Observer { result ->
            result.onSuccess { response ->
                Log.i("HomePage", response.body().toString())

                val accounts = response.body()
                Log.i("HomePage", accounts.toString())
                if (!accounts.isNullOrEmpty()) {
                    // Mostrar la primera cuenta
                    val account = accounts[0]
                    binding.accountResume.visibility = View.VISIBLE
                    binding.btnCreateAccount.visibility = View.GONE

                    binding.accountNumber.text = account.id.toString()
                    binding.balanceAmount.text = account.money.toString()
                    // Aquí puedes actualizar la UI con los datos de la cuenta
                    // Por ejemplo: binding.balanceTitle.text = firstAccount.money.toString()
                } else {
                    // Mostrar el botón de crear cuenta si no hay cuentas
                    binding.btnCreateAccount.visibility = View.VISIBLE
                    binding.accountResume.visibility = View.GONE
                }
            }
            result.onFailure {
                ToastUtils.showCustomToast(requireContext(), "Error al obtener cuentas: ${it.message}")
                Log.e("HomePage", "Error al obtener cuentas: ${it.message}")
            }
        })

        // Manejar el clic del botón de crear cuenta
        binding.btnCreateAccount.setOnClickListener {
            // Aquí puedes manejar la lógica para crear una cuenta
            // Por ejemplo, navegar a otro fragmento o mostrar un diálogo
        }
    }
}