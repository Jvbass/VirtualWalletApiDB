package cl.jpinodev.virtualwalletapidb.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import cl.jpinodev.virtualwalletapidb.R
import cl.jpinodev.virtualwalletapidb.data.appdata.SharedPreferencesHelper
import cl.jpinodev.virtualwalletapidb.data.local.database.AppDatabase
import cl.jpinodev.virtualwalletapidb.data.model.apientities.AccountRequest
import cl.jpinodev.virtualwalletapidb.data.network.api.AccountApiService
import cl.jpinodev.virtualwalletapidb.data.network.api.TransactionApiService
import cl.jpinodev.virtualwalletapidb.data.network.retrofit.RetrofitHelper
import cl.jpinodev.virtualwalletapidb.data.repository.AccountsRepositoryImpl
import cl.jpinodev.virtualwalletapidb.data.repository.TransactionsRepositoryImpl
import cl.jpinodev.virtualwalletapidb.databinding.FragmentHomePageBinding
import cl.jpinodev.virtualwalletapidb.domain.AccountsUseCase
import cl.jpinodev.virtualwalletapidb.domain.TransactionsUseCase
import cl.jpinodev.virtualwalletapidb.view.adapter.TransactionAdapter
import cl.jpinodev.virtualwalletapidb.view.utils.ToastUtils
import cl.jpinodev.virtualwalletapidb.viewmodel.AccountsViewModel
import cl.jpinodev.virtualwalletapidb.viewmodel.AccountsViewModelFactory
import cl.jpinodev.virtualwalletapidb.viewmodel.TransactionsViewModel
import cl.jpinodev.virtualwalletapidb.viewmodel.TransactionsViewModelFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomePage : Fragment() {
    private lateinit var binding: FragmentHomePageBinding
    private lateinit var accountsViewModel: AccountsViewModel

    private lateinit var transactionsViewModel: TransactionsViewModel
    private lateinit var transactionAdapter: TransactionAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomePageBinding.bind(view)


        //config accountsViewmodel
        val accountApiService: AccountApiService =
            RetrofitHelper.getRetrofit().create(AccountApiService::class.java)
        val database= AppDatabase.getDatabase(requireContext())

        val accountRepository = AccountsRepositoryImpl(accountApiService, database.AccountDao())
        val accountUseCase = AccountsUseCase(accountRepository)
        val factoryAccounts = AccountsViewModelFactory(accountUseCase)

        accountsViewModel = ViewModelProvider(this, factoryAccounts)[AccountsViewModel::class.java]

        //config transactionViewmodel
        val transactionService: TransactionApiService =
            RetrofitHelper.getRetrofit().create(TransactionApiService::class.java)
        val transactionsRepository =
            TransactionsRepositoryImpl(transactionService)
        val transactionsUseCase = TransactionsUseCase(transactionsRepository)
        val factoryTransactions = TransactionsViewModelFactory(transactionsUseCase)
        transactionsViewModel =
            ViewModelProvider(this, factoryTransactions)[TransactionsViewModel::class.java]

        //config recyclerview para las transaccioens
        binding.recyclerTransactionList.layoutManager = LinearLayoutManager(requireContext())
        transactionAdapter = TransactionAdapter(emptyList())
        binding.recyclerTransactionList.adapter = transactionAdapter

        // llamadas a SharedPreferences
        val user = SharedPreferencesHelper.getConnectedUser(requireContext())
        user?.let {
            val fullName = "${it.firstName} ${it.lastName}"
            binding.fullName.text = fullName
        }

        val token = SharedPreferencesHelper.getToken(requireContext())
        Log.i("HomePage", token.toString())
        token?.let {
            accountsViewModel.getOwnAccounts("Bearer $token")
            transactionsViewModel.getTransactions("Bearer $token")
        }

        // navegacion botones y enlaces
        val navController = Navigation.findNavController(view)
        binding.btnSend.setOnClickListener {
            navController.navigate(R.id.transactionSend)
        }
        binding.btnRequest.setOnClickListener {
            navController.navigate(R.id.transactionReceive)
        }
        binding.profileImage.setOnClickListener {
            navController.navigate(R.id.profile)
        }

        // BTN crear cuenta
        binding.btnCreateAccount.setOnClickListener {
            val token = SharedPreferencesHelper.getToken(requireContext())
            if (token != null) {
                val currentDate =
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
                val newAccount = AccountRequest(
                    creationDate = currentDate,
                    money = 6000,
                    isBlocked = false,
                    userId = 1
                )
                accountsViewModel.createAccount("Bearer $token", newAccount)

                // observer para observar cuenta creada
                accountsViewModel.accountLD.observe(viewLifecycleOwner, Observer { result ->
                    result.onSuccess { response ->
                        ToastUtils.showCustomToast(requireContext(), "Cuenta creada con éxito")
                    }
                    result.onFailure {
                        ToastUtils.showCustomToast(
                            requireContext(),
                            "Error al crear cuenta: ${it.message}"
                        )
                    }
                })
            } else {
                ToastUtils.showCustomToast(requireContext(), "No es posible crear una cuenta")
            }
        }

        /*****ViewModel Observers*****/
        // cambios en ownAccountsLD
        accountsViewModel.ownAccountsLD.observe(viewLifecycleOwner, Observer { result ->
            result?.onSuccess { response ->
                Log.i("HomePage", response.body().toString())

                val accounts = response.body()
                Log.i("HomePage", accounts.toString())
                if (!accounts.isNullOrEmpty()) {
                    // Mostrar la primera cuenta
                    val account = accounts[0]
                    SharedPreferencesHelper.saveAccount(requireContext(), account)
                    binding.accountResume.visibility = View.VISIBLE
                    binding.btnCreateAccount.visibility = View.GONE

                    binding.accountNumber.text = account.id.toString()
                    binding.balanceAmount.text = account.money.toString()

                } else {
                    binding.btnCreateAccount.visibility = View.VISIBLE
                    binding.accountResume.visibility = View.GONE
                }
            }
            result?.onFailure {
                ToastUtils.showCustomToast(
                    requireContext(),
                    "Error al obtener cuentas: ${it.message}"
                )
                Log.e("HomePage", "Error al obtener cuentas: ${it.message}")
            }
        })

        //cambios transactionsLD
        transactionsViewModel.transactionsLD.observe(viewLifecycleOwner, Observer { result ->
            result.onSuccess { response ->
                val transactions = response.body()?.data
                transactions?.let {
                    if (transactions.isEmpty()) {
                        binding.emptyTransactionList.visibility = View.VISIBLE
                        binding.recyclerTransactionList.visibility = View.GONE
                    } else {
                        transactionAdapter = TransactionAdapter(it)
                        binding.recyclerTransactionList.adapter = transactionAdapter
                        binding.emptyTransactionList.visibility = View.GONE
                        binding.recyclerTransactionList.visibility = View.VISIBLE
                    }
                }
            }
            result.onFailure {
                binding.emptyTransactionList.visibility = View.VISIBLE
                ToastUtils.showCustomToast(
                    requireContext(),
                    "Error al obtener transacciones: ${it.message}"
                )
            }
        })

        accountsViewModel.accountLD.observe(viewLifecycleOwner, Observer { result ->
            result.onSuccess { response ->
                val account = response.body()
                account?.let {
                    SharedPreferencesHelper.saveAccount(requireContext(), it)
                    binding.accountResume.visibility = View.VISIBLE
                    binding.btnCreateAccount.visibility = View.GONE

                    binding.accountNumber.text = it.id.toString()
                    binding.balanceAmount.text = it.money.toString()
                }
            }
            result.onFailure {
                ToastUtils.showCustomToast(
                    requireContext(),
                    "Error al crear cuenta: ${it.message}"
                )
            }
        })

    } // end of onViewCreated
}//end of class