package be.ehb.mushroomdetectivev2

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.fragment.app.Fragment
import java.util.*

class LanguageFragment : Fragment(R.layout.fragment_language) {

    private fun setLocale(language: String) {
        val locale = Locale(language)
        val resources: Resources = requireContext().resources
        val config: Configuration = resources.configuration
        config.setLocale(locale)
        Locale.setDefault(locale)
        resources.configuration.updateFrom(config)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_dutch).setOnClickListener {
            val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags("nl")
            AppCompatDelegate.setApplicationLocales(appLocale)
        }

        view.findViewById<Button>(R.id.button_english).setOnClickListener {
            val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags("")
            AppCompatDelegate.setApplicationLocales(appLocale)
        }

    }
}
