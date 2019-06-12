package com.emmanueldavies.mensapluse1.presentation.ui.MensaView

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.emmanueldavies.mensapluse1.R

class CustomSpinnerAdapter : ArrayAdapter<String> {
    private var customSpinnerItems: List<String>

    constructor(context: Context, resource: Int, pickerItems: List<String>) : super(
        context,
        resource,
        pickerItems
    ) {
        this.customSpinnerItems = pickerItems
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return CustomSpinnerView(position, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return CustomSpinnerView(position, parent)
    }

    private fun CustomSpinnerView(position: Int, parent: ViewGroup): View {
        //Getting the Layout Inflater Service from the system
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        //Inflating out custom spinner viewz
        val customView = layoutInflater.inflate(R.layout.spinner_item, parent, false)
        //Declaring and initializing the widgets in custom layout
        val textView = customView?.findViewById(R.id.spinner_canteen_name) as? TextView
        textView?.text = customSpinnerItems[position]
        return customView
    }


}