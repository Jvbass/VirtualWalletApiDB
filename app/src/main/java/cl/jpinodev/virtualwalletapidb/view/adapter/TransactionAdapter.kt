package cl.jpinodev.virtualwalletapidb.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cl.jpinodev.virtualwalletapidb.R
import cl.jpinodev.virtualwalletapidb.data.model.entities.Transactions
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TransactionAdapter(private val transactions: List<Transactions>) :
    RecyclerView.Adapter<TransactionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.transaction_item, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]
        holder.accountId.text = transaction.accountId.toString()
        holder.toAccountId.text = transaction.toAccountId.toString()
        holder.transactionDateTime.text = transaction.date.dateFormatterString()
        holder.transactionAmount.text = transaction.amount
        holder.transactionOperationType.text = if (transaction.type == "payment") "-" else "+"
        holder.transactionOperationIcon.setImageResource(if (transaction.type == "payment")
            R.drawable.send_icon_yellow else R.drawable.request_icon_blue)
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