package cl.jpinodev.virtualwalletapidb.view.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cl.jpinodev.virtualwalletapidb.R
import cl.jpinodev.virtualwalletapidb.databinding.TransactionItemBinding

class TransactionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val transactionDetail: TextView = view.findViewById(R.id.transactionDescription)
    val toAccountId: TextView = view.findViewById(R.id.numberAccount)
    val transactionDateTime: TextView = view.findViewById(R.id.transaction_datetime)
    val transactionAmount: TextView = view.findViewById(R.id.transaction_amount)
    val transactionOperationType: TextView = view.findViewById(R.id.transaction_operation_type)
    val transactionOperationIcon: ImageView = view.findViewById(R.id.operation_icon)
}

