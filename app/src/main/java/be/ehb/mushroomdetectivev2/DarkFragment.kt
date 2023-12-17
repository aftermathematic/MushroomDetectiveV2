package be.ehb.mushroomdetectivev2

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment

class DarkFragment:Fragment(R.layout.fragment_dark) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val switch: Switch = view.findViewById(R.id.switch1)

        switch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                // The switch is enabled/checked
                Log.d(TAG, "isChecked")

                // Set the theme to dark
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

            } else {
                Log.d(TAG, "isNotChecked")
                // The switch is disabled

                // Set the theme to light
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

}
