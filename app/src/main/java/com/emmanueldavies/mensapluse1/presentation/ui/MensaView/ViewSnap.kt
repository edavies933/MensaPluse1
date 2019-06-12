package com.emmanueldavies.mensapluse1.presentation.ui.MensaView

import android.R
import android.app.Activity
import android.support.design.widget.BaseTransientBottomBar
import android.support.design.widget.Snackbar
import android.view.View
import javax.inject.Inject

class ViewSnap @Inject constructor() {
    lateinit var snackBar: Snackbar

    fun showSnackBar(
        activity: Activity,
        snackStrId: Int,
        actionStrId: Int = 0,
        listener: View.OnClickListener? = null
    ) {


        snackBar = Snackbar.make(
            activity.findViewById(R.id.content), activity.getString(snackStrId),
            BaseTransientBottomBar.LENGTH_INDEFINITE
        )
        if (actionStrId != 0 && listener != null) {
            snackBar.setAction(activity.getString(actionStrId), listener)
        } else {

            snackBar.setAction(activity.getString(com.emmanueldavies.mensapluse1.R.string.snackbar_ok_button)) { snackBar.dismiss() }
        }
        snackBar.show()
    }

}