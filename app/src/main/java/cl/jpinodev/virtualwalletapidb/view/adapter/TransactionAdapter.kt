package cl.jpinodev.virtualwalletapidb.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cl.jpinodev.virtualwalletapidb.R
import cl.jpinodev.virtualwalletapidb.data.model.apientities.TransactionsResponse
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TransactionAdapter(private val transactions: List<TransactionsResponse>) :
    RecyclerView.Adapter<TransactionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.transaction_item, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]
        if (transaction.type == "payment") {
            holder.transactionDetail.text = "Transf. a la cuenta "
            holder.toAccountId.text = transaction.toAccountId.toString()
            holder.transactionOperationType.text = "-"
            holder.transactionOperationIcon.setImageResource(R.drawable.send_icon_yellow)
        } else {
            holder.transactionDetail.text = "Deposito desde la cuenta "
            holder.toAccountId.text = transaction.accountId.toString()
            holder.transactionOperationType.text = "+"
            holder.transactionOperationIcon.setImageResource(R.drawable.request_icon_blue)
        }
        holder.transactionDateTime.text = transaction.date.dateFormatterString()
        holder.transactionAmount.text = transaction.amount
    }

    override fun getItemCount(): Int {
        return transactions.size
    }
}

fun String.dateFormatterString(): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd-MM-yyyy, HH:mm", Locale.getDefault())

    return try {
        val date: Date? = inputFormat.parse(this)
        date?.let {
            outputFormat.format(it)
        } ?: ""
    } catch (e: Exception) {
        ""
    }
}