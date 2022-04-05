package com.bguilherme.financialmanager.view.bill.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bguilherme.financialmanager.FinancialManagerApplication
import com.bguilherme.financialmanager.R
import com.bguilherme.financialmanager.model.bill.entity.Bill
import com.bguilherme.financialmanager.model.database.FinancialManagerDB
import com.bguilherme.financialmanager.view.AbstractActivity
import com.bguilherme.financialmanager.view.about.activity.AboutActivity
import com.bguilherme.financialmanager.view.bill.adapter.BillAdapter
import com.bguilherme.financialmanager.view.bill.adapter.ItemBillClickListener
import com.bguilherme.financialmanager.view.bill.adapter.RecyclerViewSwipeCallBack
import com.bguilherme.financialmanager.view.bill.dialog.IAddBillDialog
import com.bguilherme.financialmanager.view.bill.dialog.AddItemDialog
import com.bguilherme.financialmanager.view.settings.activity.SettingsActivity
import com.bguilherme.financialmanager.view.test.activity.TestActivity
import com.bguilherme.financialmanager.view.util.ThemeUtil
import com.bguilherme.financialmanager.viewmodel.bill.viewmodel.BillViewModel
import com.bguilherme.financialmanager.viewmodel.bill.viewmodel.BillViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class BillsActivity : AbstractActivity(), IAddBillDialog, IDeleteOrMoveBill {

    private lateinit var billViewModel: BillViewModel
    private lateinit var adapter: BillAdapter
    private var moved: Boolean = false
    private var billUpdated: Boolean = false
    private var recentlyDeletedBill: Bill? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bills)
        ThemeUtil.setViewBackgroundColorPrimaryTransp(this, findViewById(R.id.bkg_parent))

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        val fabAdd = findViewById<FloatingActionButton>(R.id.fab_add)

        fabAdd.setOnClickListener { openAddBillDialog(null) }

        val application = requireNotNull(this@BillsActivity).application
        val dataSource = FinancialManagerDB.getInstance(application).billDao

        billViewModel = ViewModelProvider(this@BillsActivity, BillViewModelFactory(dataSource))
            .get(BillViewModel::class.java)

        // RecyclerView Adapter
        adapter = BillAdapter(this, ItemBillClickListener { bill ->
            openAddBillDialog(bill)
        })
        recyclerView.adapter = adapter
        // RecyclerView ItemTouchHelper SwipeCallBack
        val itemTouchHelper = ItemTouchHelper(RecyclerViewSwipeCallBack(adapter))
        itemTouchHelper.attachToRecyclerView(recyclerView)

        billViewModel.billsUpdated.observe(this@BillsActivity, Observer {
            if (billUpdated) {
                adapter.clearBills()
                billUpdated = false
            }
            adapter.addHeaderAndFooterAndSubmitList(billViewModel.bills) // list updated
        })

        billViewModel.getBills(FinancialManagerApplication.getInstance()?.currentUser!!.id)
    }

    override fun onStop() {
        super.onStop()
        if (moved) {
            billViewModel.updateBillsOrder(adapter.getBills())
        }
    }

    private fun openAddBillDialog(bill: Bill?) {
        val additemDialog = AddItemDialog(this, this, bill)
        additemDialog.openDialog()
    }

    override fun addBill(bill: Bill, reAdd: Boolean) {
        billViewModel.addBill(bill, reAdd)
    }

    override fun updateBill(bill: Bill) {
        billUpdated = true
        billViewModel.updateBill(bill)
    }

    override fun deleteBill(bill: Bill, position: Int) {
        recentlyDeletedBill = bill
        billViewModel.deleteBill(bill)
        showUndoSnackbar(position)
    }

    private fun showUndoSnackbar(position: Int) {
        val coordinatorLayout: View = findViewById(R.id.coordinator_layout)
        val snackbar: Snackbar =
            Snackbar.make(coordinatorLayout, R.string.undo_snack_bar_text, Snackbar.LENGTH_LONG)
        snackbar.setAction(R.string.undo_snack_bar_button_text) { undoDelete(position) }
        snackbar.show()
    }

    private fun undoDelete(position: Int) {
        if (recentlyDeletedBill != null) {
            addBill(recentlyDeletedBill!!, true)
            adapter.reAddBill(recentlyDeletedBill!!, position)
        }
        recentlyDeletedBill = null
    }

    override fun billMoved() {
        moved = true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId
        if (id == R.id.settings) {
            val it = Intent(this, SettingsActivity::class.java)
            startActivity(it)
        } else if (id == R.id.about) {
            val it = Intent(this, AboutActivity::class.java)
            startActivity(it)
        } else if (id == R.id.test) {
            val it = Intent(this, TestActivity::class.java)
            startActivity(it)
        }
        return super.onOptionsItemSelected(item)
    }
}

interface IDeleteOrMoveBill {
    fun deleteBill(bill: Bill, position: Int)
    fun billMoved()
}
