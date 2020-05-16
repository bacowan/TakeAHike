package com.example.takeahike.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.takeahike.R
import java.lang.ClassCastException
import java.lang.IllegalStateException

class NameRoute : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            try {
                val listener = targetFragment as Listener
                val view = inflater.inflate(R.layout.name_route, null)
                val textBox = view?.findViewById<EditText>(R.id.route_name_text)
                builder.setView(view)
                    .setPositiveButton(
                        R.string.confirm_button,
                        DialogInterface.OnClickListener { _, _ -> listener.onConfirm(textBox?.text?.toString() ?: "") })
                    .setNegativeButton(
                        R.string.cancel_button,
                        DialogInterface.OnClickListener { _, _ -> listener.onCancel() }
                    )
                builder.create()
            }
            catch (e: ClassCastException) {
                val y = e.message
                throw ClassCastException((targetFragment.toString() +
                        " must implement NameRoute.Listener"))
            }
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    interface Listener {
        fun onConfirm(name: String)
        fun onCancel()
    }
}