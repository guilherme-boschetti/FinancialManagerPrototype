package com.bguilherme.financialmanager.view.bill.dialog

import android.app.Activity
import android.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.EditText
import com.bguilherme.financialmanager.FinancialManagerApplication
import com.bguilherme.financialmanager.R
import com.bguilherme.financialmanager.model.bill.entity.Bill
import java.math.BigDecimal
import java.util.*

class AddItemDialog(activity: Activity?, classCallback: IAddBillDialog?, bill: Bill?) {

    private var activity: Activity? = null
    private var dialog: AlertDialog? = null
    private var btnSalvar: Button? = null
    private var btnCancelar: Button? = null
    private var description: EditText? = null
    private  var value:EditText? = null
    private  var expirationDate:EditText? = null
    private var billPaid: CheckBox? = null
    private  var billFixed:CheckBox? = null
    private var classCallback: IAddBillDialog? = null
    private var bill: Bill? = null

    init {
        this.activity = activity
        this.classCallback = classCallback
        this.bill = bill
    }

    fun openDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        val li = activity!!.layoutInflater
        val view = li.inflate(R.layout.dialog_add_item, null)
        findViewsByIds(view) // Busca as views
        addListeners() // Adiciona os listeners
        setLabels() // Seta os labels
        builder.setView(view)
        dialog = builder.create()
        dialog!!.show()
    }

    /**
     * Busca as views
     */
    private fun findViewsByIds(view: View) {
        btnSalvar = view.findViewById(R.id.btnSalvar)
        btnCancelar = view.findViewById(R.id.btnCancelar)
        description = view.findViewById(R.id.description)
        value = view.findViewById(R.id.value)
        expirationDate = view.findViewById(R.id.expirationDate)
        billPaid = view.findViewById(R.id.billPaid)
        billFixed = view.findViewById(R.id.billFixed)
    }

    /**
     * Adiciona os listeners
     */
    private fun addListeners() {
        value!!.addTextChangedListener(object : TextWatcher {
            private var isUpdating = false
            override fun beforeTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
                //Nothing to do
            }

            override fun onTextChanged(s: CharSequence, i: Int, i1: Int, i2: Int) {
                if (isUpdating) {
                    isUpdating = false
                    return
                }
                isUpdating = true
                value!!.setText(if (!s.toString().isEmpty()) { convertToNumber(s.toString(), 2) } else { "" })
                value!!.setSelection(value!!.getText().length)
            }

            override fun afterTextChanged(editable: Editable) {
                //Nothing to do
            }
        })
        billFixed!!.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { _ , checked ->
            if (checked) {
                expirationDate!!.setVisibility(View.VISIBLE)
            } else {
                expirationDate!!.setVisibility(View.INVISIBLE)
                expirationDate!!.setText(null)
            }
        })
        btnSalvar!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                if (bill == null) {
                    bill = Bill()
                    bill?.userId = FinancialManagerApplication.getInstance()?.currentUser!!.id
                    bill?.description = description!!.text.toString()
                    bill?.value = if(!value!!.text.toString().isEmpty()) { value!!.text.toString().toDouble() } else { null }
                    bill?.paid = billPaid!!.isChecked()
                    bill?.fixed = billFixed!!.isChecked()
                    bill?.expirationDate = expirationDate!!.getText().toString()
                    bill?.inclusionDate = Date().toString()
                    bill?.listOrder = 0
                    classCallback!!.addBill(bill!!, false)
                } else {
                    bill?.description = description!!.text.toString()
                    bill?.value = if (!value!!.text.toString().isEmpty()) { value!!.text.toString().toDouble() } else { 0.0 }
                    bill?.paid = billPaid!!.isChecked()
                    bill?.fixed = billFixed!!.isChecked()
                    bill?.expirationDate = expirationDate!!.getText().toString()
                    bill?.inclusionDate = Date().toString()
                    classCallback!!.updateBill(bill!!)
                }
                dialog!!.dismiss()
            }
        })
        btnCancelar!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                dialog!!.dismiss()
            }
        })
    }

    /**
     * Seta os labels
     */
    private fun setLabels() {
        btnSalvar!!.setText("salvar")
        btnCancelar!!.setText("cancelar")
        if (bill != null) {
            description!!.setText(bill?.description)
            value!!.setText(if (bill?.value != null) { bill?.value.toString() } else { "" })
            expirationDate!!.setText(bill?.expirationDate)
            billFixed!!.setChecked(bill?.fixed!!)
            billPaid!!.setChecked(bill?.paid!!)
        }
    }

    /**
     * Converte um valor para numero conforme usuario vai digitando na tela
     *
     * @param value
     * @param qtCasas
     * @return
     */
    fun convertToNumber(value: String?, qtCasas: Int): String? {
        if (value == null || value.isEmpty()) {
            return "0"
        }

        // deixa apenas numeros no que foi digitado
        val auxValue = value.replace("[^\\d]".toRegex(), "")


        //Converte pra BigDecimal
        var aux = BigDecimal(auxValue)

        //Dvide pela quantidade de casas
        aux = aux.divide(
            BigDecimal(Math.pow(10.0, qtCasas.toDouble())),
            qtCasas,
            BigDecimal.ROUND_HALF_EVEN
        )

        //Converte para String e retorna
        return aux.toString()//ConverterUtil.toString(aux)
    }
}

interface IAddBillDialog {
    fun addBill(bill: Bill, reAdd: Boolean)
    fun updateBill(bill: Bill)
}