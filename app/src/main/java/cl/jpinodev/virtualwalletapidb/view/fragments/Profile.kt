package cl.jpinodev.virtualwalletapidb.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import cl.jpinodev.virtualwalletapidb.R
import cl.jpinodev.virtualwalletapidb.data.appdata.SharedPreferencesHelper
import cl.jpinodev.virtualwalletapidb.databinding.FragmentHomePageBinding
import cl.jpinodev.virtualwalletapidb.databinding.FragmentProfileBinding
import cl.jpinodev.virtualwalletapidb.viewmodel.AccountsViewModel

class Profile : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var accountsViewModel: AccountsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)

        val navController = Navigation.findNavController(view)

        binding.backIcon.setOnClickListener {
            navController.navigateUp()
        }
        val user = SharedPreferencesHelper.getConnectedUser(requireContext())
        user?.let {
            val fullName = "${it.firstName} ${it.lastName}"
            binding.userName.text = fullName
        }

    }

}