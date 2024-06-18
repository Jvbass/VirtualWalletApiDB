package cl.jpinodev.virtualwalletapidb.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import cl.jpinodev.virtualwalletapidb.R
import cl.jpinodev.virtualwalletapidb.data.appdata.SharedPreferencesHelper
import cl.jpinodev.virtualwalletapidb.data.model.apientities.OperationRequest
import cl.jpinodev.virtualwalletapidb.data.network.api.AccountApiService
import cl.jpinodev.virtualwalletapidb.data.network.retrofit.RetrofitHelper
import cl.jpinodev.virtualwalletapidb.data.repository.AccountsRepositoryImpl
import cl.jpinodev.virtualwalletapidb.databinding.FragmentTransactionReceiveBinding
import cl.jpinodev.virtualwalletapidb.domain.AccountsUseCase
import cl.jpinodev.virtualwalletapidb.view.utils.ToastUtils
import cl.jpinodev.virtualwalletapidb.viewmodel.AccountsViewModel
import cl.jpinodev.virtualwalletapidb.viewmodel.AccountsViewModelFactory

class TransactionReceive : Fragment() {

    private lateinit var binding: FragmentTransactionReceiveBinding
    private lateinit var accountsViewModel: AccountsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transaction_receive, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentTransactionReceiveBinding.bind(view)
        val navController = Navigation.findNavController(view)

        binding.materialToolbar.setNavigationOnClickListener {
            navController.navigateUp()
        }

        // Configurar AccountsViewModel
        val accountApiService: AccountApiService =
            RetrofitHelper.getRetrofit().create(AccountApiService::class.java)
        val accountsRepository: AccountsRepositoryImpl = AccountsRepositoryImpl(accountApiService)
        val accountsUseCase: AccountsUseCase = AccountsUseCase(accountsRepository)
        val factory = AccountsViewModelFactory(accountsUseCase)
        accountsViewModel = ViewModelProvider(this, factory)[AccountsViewModel::class.java]

        // cuenta y usuario conectado
        val connectedUser = SharedPreferencesHelper.getConnectedUser(requireContext())
        val connectedAccount = SharedPreferencesHelper.getAccount(requireContext())
        val accountId = connectedAccount?.id

        binding.nombreUsuario.text = connectedUser?.firstName
        binding.apellidoUsuario.text = connectedUser?.lastName

        binding.buttonReceive.setOnClickListener {
            val amountText = binding.txtAmountSend.text.toString()
            val message = binding.txtDepositComment.text.toString()

            if (amountText.isNotEmpty() && accountId != null) {
                val amount = amountText.toIntOrNull()
                if (amount != null) {
                    val token = SharedPreferencesHelper.getToken(requireContext())
                    val request = OperationRequest(type = "topup", concept = message, amount = amount)
                    token?.let {
                        accountsViewModel.transactionOperation(accountId, "Bearer $token", request)
                    }
                } else {
                    ToastUtils.showCustomToast(requireContext(), "Por favor ingrese una cantidad válida.")
                }
            } else {
                ToastUtils.showCustomToast(requireContext(), "Por favor complete todos los campos.")
            }
        }

        accountsViewModel.operationLD.observe(viewLifecycleOwner, Observer { result ->
            result.onSuccess { response ->
                val message = response.body()?.message
                ToastUtils.showCustomToast(requireContext(), message ?: "Depósito exitoso")
                navController.navigateUp()
            }
            result.onFailure { throwable ->
                ToastUtils.showCustomToast(requireContext(), "Error: ${throwable.message}")
            }
        })

    }
}