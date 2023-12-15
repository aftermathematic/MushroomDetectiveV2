package be.ehb.mushroomdetectivev2

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import java.util.*

class LanguageFragment : Fragment(R.layout.fragment_language) {

    private fun setLocale(language: String) {
        // Initialize resources
        val resources: Resources = requireContext().resources



        // Reset the activity to apply changes
        //val refresh = requireActivity().intent
        //requireActivity().finish()
        //startActivity(refresh)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_english).setOnClickListener {
            setLocale("")
        }

        view.findViewById<Button>(R.id.button_dutch).setOnClickListener {
            setLocale("nl-rBE")
        }
    }
}
