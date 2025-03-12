package com.example.loginycardview.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.loginycardview.R
import com.example.loginycardview.domain.Event

class EventDialogFragment(
    private val event: Event? = null,
    private val onEventSaved: (Event) -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val view = requireActivity().layoutInflater.inflate(R.layout.dialog_event, null)

        val titleEditText = view.findViewById<EditText>(R.id.editTextTitle)
        val descriptionEditText = view.findViewById<EditText>(R.id.editTextDescription)
        val dateEditText = view.findViewById<EditText>(R.id.editTextDate)
        val locationEditText = view.findViewById<EditText>(R.id.editTextLocation)
        val saveButton = view.findViewById<Button>(R.id.buttonSaveEvent)

        event?.let {
            titleEditText.setText(it.title)
            descriptionEditText.setText(it.description)
            dateEditText.setText(it.date)
            locationEditText.setText(it.location)
        }

        saveButton.setOnClickListener {
            val newEvent = Event(
                id = event?.id ?: "",
                title = titleEditText.text.toString(),
                description = descriptionEditText.text.toString(),
                date = dateEditText.text.toString(),
                location = locationEditText.text.toString()
            )
            onEventSaved(newEvent)
            dismiss()
        }

        builder.setView(view)
        return builder.create()
    }
}
