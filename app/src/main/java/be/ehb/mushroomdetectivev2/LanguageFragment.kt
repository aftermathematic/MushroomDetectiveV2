package be.ehb.mushroomdetectivev2

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.fragment.app.Fragment
import java.util.Locale

class LanguageFragment : Fragment(R.layout.fragment_language) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val switch: Switch = view.findViewById(R.id.switch_language)

        switch.isChecked = Locale.getDefault().language == "nl"

        // Change the locale when the switch is checked
        switch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Set the locale to NL
                val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags("nl")
                AppCompatDelegate.setApplicationLocales(appLocale)

                // refresh the current fragment
                val ft = parentFragmentManager.beginTransaction()
                ft.detach(this).attach(this).commit()

                // The switch is enabled/checked
                Log.d(ContentValues.TAG, "Locale changed to NL")
            } else {
                // Set the locale to EN
                val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags("en")
                AppCompatDelegate.setApplicationLocales(appLocale)

                // The switch is enabled/checked
                Log.d(ContentValues.TAG, "Locale changed to EN")
            }
        }

    }


}
