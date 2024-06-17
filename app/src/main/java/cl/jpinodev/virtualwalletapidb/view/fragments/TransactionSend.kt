package cl.jpinodev.virtualwalletapidb.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.Navigation
import cl.jpinodev.virtualwalletapidb.R
import cl.jpinodev.virtualwalletapidb.databinding.FragmentHomePageBinding
import cl.jpinodev.virtualwalletapidb.databinding.FragmentTransactionSendBinding

class TransactionSend : Fragment() {
    private lateinit var binding: FragmentTransactionSendBinding
        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transaction_send, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentTransactionSendBinding.bind(view)
        val navController = Navigation.findNavController(view)
        binding.materialToolbar.setNavigationOnClickListener {
            navController.navigateUp()
        }
    }
}