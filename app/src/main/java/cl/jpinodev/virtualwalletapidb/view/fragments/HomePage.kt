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

class HomePage : Fragment() {
    private lateinit var binding: FragmentHomePageBinding
    private lateinit var accountsViewModel: AccountsViewModel

    //feature transaction
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
        val accountService: AccountApiService =
            RetrofitHelper.getRetrofit().create(AccountApiService::class.java)
        val accountRepository: AccountsRepositoryImpl = AccountsRepositoryImpl(accountService)
        val accountUseCase: AccountsUseCase = AccountsUseCase(accountRepository)
        val factoryAccounts = AccountsViewModelFactory(accountUseCase)

        accountsViewModel = ViewModelProvider(this, factoryAccounts)[AccountsViewModel::class.java]

        //config transactionViewmodel
        // Configurar TransactionsViewModel
        val transactionService: TransactionApiService =
            RetrofitHelper.getRetrofit().create(TransactionApiService::class.java)
        val transactionsRepository: TransactionsRepositoryImpl =
            TransactionsRepositoryImpl(transactionService)
        val transactionsUseCase: TransactionsUseCase = TransactionsUseCase(transactionsRepository)
        val factoryTransactions = TransactionsViewModelFactory(transactionsUseCase)
        transactionsViewModel =
            ViewModelProvider(this, factoryTransactions)[TransactionsViewModel::class.java]

        //config recyclerview para las transaccioens
        binding.recyclerTransactionList.layoutManager = LinearLayoutManager(requireContext())
        transactionAdapter = TransactionAdapter(emptyList())
        binding.recyclerTransactionList.adapter = transactionAdapter


        val navController = Navigation.findNavController(view)
        binding.btnSend.setOnClickListener {
            navController.navigate(R.id.transactionSend)
        }
        binding.btnRequest.setOnClickListener {
            navController.navigate(R.id.transactionReceive)
        }

        val user = SharedPreferencesHelper.getConnectedUser(requireContext())
        user?.let {
            val fullName = "${it.firstName} ${it.lastName}"
            binding.greetingName.text = fullName
        }

        val token = SharedPreferencesHelper.getToken(requireContext())
        Log.i("HomePage", token.toString())
        token?.let {
            accountsViewModel.getOwnAccounts("Bearer $token")
            transactionsViewModel.getTransactions("Bearer $token")
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
            result.onFailure {
                ToastUtils.showCustomToast(
                    requireContext(),
                    "Error al obtener cuentas: ${it.message}"
                )
                Log.e("HomePage", "Error al obtener cuentas: ${it.message}")
            }
        })

        //observar los cambios en transactionsLD
        transactionsViewModel.transactionsLD.observe(viewLifecycleOwner, Observer { result ->
            result.onSuccess { response ->
                val transactions = response.body()?.data
                transactions?.let {
                    transactionAdapter = TransactionAdapter(it)
                    binding.recyclerTransactionList.adapter = transactionAdapter
                }
            }
            result.onFailure {
                ToastUtils.showCustomToast(
                    requireContext(),
                    "Error al obtener transacciones: ${it.message}"
                )
            }
        })
    }
}