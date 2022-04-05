package com.bguilherme.financialmanager.view.bill.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bguilherme.financialmanager.databinding.ItemBillBinding
import com.bguilherme.financialmanager.model.bill.entity.Bill
import com.bguilherme.financialmanager.view.bill.adapter.ItemBillClickListener

class BillViewHolder private constructor(val binding: ItemBillBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Bill, clickListener: ItemBillClickListener) {
        binding.txtBillDescription.text = item.description
        binding.txtBillValue.text = item.value.toString()
        binding.bill = item
        binding.clickListener = clickListener
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): BillViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemBillBinding.inflate(layoutInflater, parent, false)
            return BillViewHolder(binding)
        }
    }
}

// Without DataBinding
/*
class BillViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val txtBillDescription = itemView.findViewById<TextView>(R.id.txt_bill_description)
    val txtBillValue = itemView.findViewById<TextView>(R.id.txt_bill_value)

    fun bind(item: Bill) {
        txtBillDescription.text = item.description
        txtBillValue.text = item.value.toString()
    }

    companion object {
        fun from(parent: ViewGroup): BillViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_bill, parent, false)
            return BillViewHolder(view)
        }
    }
}
*/
