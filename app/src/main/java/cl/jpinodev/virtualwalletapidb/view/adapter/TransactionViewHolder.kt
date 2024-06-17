package cl.jpinodev.virtualwalletapidb.view.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cl.jpinodev.virtualwalletapidb.R
import cl.jpinodev.virtualwalletapidb.databinding.TransactionItemBinding

class TransactionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val accountId: TextView = view.findViewById(R.id.transaction_user_name)
    val toAccountId: TextView = view.findViewById(R.id.transaction_user_lastname)
    val transactionDateTime: TextView = view.findViewById(R.id.transaction_datetime)
    val transactionAmount: TextView = view.findViewById(R.id.transaction_amount)
    val transactionOperationType: TextView = view.findViewById(R.id.transaction_operation_type)
    val transactionOperationIcon: ImageView = view.findViewById(R.id.operation_icon)
}

