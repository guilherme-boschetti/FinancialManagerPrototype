package com.bguilherme.financialmanager.view.bill.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bguilherme.financialmanager.R
import com.bguilherme.financialmanager.model.bill.entity.Bill
import com.bguilherme.financialmanager.view.bill.activity.IDeleteOrMoveBill
import com.bguilherme.financialmanager.view.bill.viewholder.BillViewHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Collections.*

private const val ITEM_VIEW_TYPE_HEADER = 0;
private const val ITEM_VIEW_TYPE_ITEM = 1;
private const val ITEM_VIEW_TYPE_FOOTER = 2;

/*class BillAdapter(
    private val iDeleteOrMoveBill: IDeleteOrMoveBill,
    private val clickListener: ItemBillClickListener
) : ListAdapter<DataItem, RecyclerView.ViewHolder>(BillDiffCallBack()) {*/

class BillAdapter(
    private val iDeleteOrMoveBill: IDeleteOrMoveBill,
    private val clickListener: ItemBillClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var bills: ArrayList<DataItem>
    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> HeaderFooterViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> BillViewHolder.from(parent)
            ITEM_VIEW_TYPE_FOOTER -> HeaderFooterViewHolder.from(parent)
            else -> BillViewHolder.from(parent)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (bills.get(position)) { //getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.BillItem -> ITEM_VIEW_TYPE_ITEM
            is DataItem.Footer -> ITEM_VIEW_TYPE_FOOTER
            //else -> ITEM_VIEW_TYPE_ITEM // 'when' is exhaustive so 'else' is redundant here
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BillViewHolder -> {
                val item = bills.get(position) as DataItem.BillItem
                holder.bind(item.bill, clickListener)
            }
            is HeaderFooterViewHolder -> {
                if (position == 0)
                    holder.bind("Header")
                else
                    holder.bind("Footer")
            }
        }
    }

    fun removeItemAt(position: Int) {

        val billItem = bills.get(position)

        notifyItemRemoved(position)

        iDeleteOrMoveBill.deleteBill(billItem.bill!!, position)
        bills.removeAt(position)
    }

    fun onMoved(fromPosition: Int, toPosition: Int) {

        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                swap(bills, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                swap(bills, i, i - 1)
            }
        }

        iDeleteOrMoveBill.billMoved()
    }

    fun getBills(): List<DataItem>? {
        return bills
    }

    fun clearBills() {
        bills = ArrayList()
    }

    fun reAddBill(bill: Bill, position: Int) {
        bills.add(position, DataItem.BillItem(bill))
        notifyItemInserted(position)
    }

    fun addHeaderAndFooterAndSubmitList(list: List<Bill>?) {
        if ((!this::bills.isInitialized || bills.isEmpty()) && list != null && !list.isEmpty()) {
            adapterScope.launch {
                val items = when (list) {
                    null -> listOf(DataItem.Header) + listOf(DataItem.Footer)
                    else -> listOf(DataItem.Header) + list.map { DataItem.BillItem(it) } + listOf(
                        DataItem.Footer
                    )
                }
                withContext(Dispatchers.Main) {
                    bills = items as ArrayList<DataItem>
                    notifyDataSetChanged()
                }
            }
        } else if (list != null && !list.isEmpty()) {
            bills.add(1, DataItem.BillItem(list.get(0)))
            notifyItemInserted(1)
        }
    }

    // ==

    class HeaderFooterViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(textHeaderFooter: String) {
            text?.setText(textHeaderFooter)
        }

        companion object {
            var text: TextView? = null
            fun from(parent: ViewGroup): HeaderFooterViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.header, parent, false)
                text = view.findViewById<TextView>(R.id.txt_header_footer)
                return HeaderFooterViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int {
        if (!this::bills.isInitialized)
            return 0
        return bills.count()
    }
}

class ItemBillClickListener(val clickListener: (bill: Bill) -> Unit) {
    fun onClick(bill: Bill) = clickListener(bill)
}

sealed class DataItem {
    data class BillItem(val billItem: Bill) : DataItem() {
        override val id: Long
            get() = billItem.id!!
        override val bill: Bill
            get() = billItem
    }

    object Header : DataItem() {
        override val id: Long
            get() = Long.MIN_VALUE
        override val bill: Bill?
            get() = null
    }

    object Footer : DataItem() {
        override val id: Long
            get() = Long.MAX_VALUE
        override val bill: Bill?
            get() = null
    }

    abstract val id: Long
    abstract val bill: Bill?
}

/*class BillDiffCallBack : DiffUtil.ItemCallback<DataItem>() {

    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }

}*/