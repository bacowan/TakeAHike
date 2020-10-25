package com.example.takeahike.ui.edit.editor

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
import androidx.navigation.NavHost
import androidx.navigation.fragment.NavHostFragment
import com.example.takeahike.R
import java.lang.ClassCastException
import java.lang.IllegalStateException

class NameRoute : DialogFragment() {
    private lateinit var listener: Listener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = parentFragment as Listener
        }
        catch (e: ClassCastException) {
            throw ClassCastException((parentFragment.toString() +
                    " must implement NameRoute.Listener"))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            val inflater = requireActivity().layoutInflater

            val textBox = view?.findViewById<EditText>(R.id.route_name_text)
            builder.setView(inflater.inflate(R.layout.name_route, null))
                .setPositiveButton(R.string.confirm_button)
                    { dialog, _ -> listener.onConfirm((dialog as? Dialog)?.findViewById<EditText>(R.id.route_name_text)?.text?.toString() ?: "") }
                .setNegativeButton(R.string.cancel_button)
                    { _, _ -> listener.onCancel() }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    interface Listener {
        fun onConfirm(name: String)
        fun onCancel()
    }
}