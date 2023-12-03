package be.ehb.mushroomdetectivev2

import android.graphics.ImageDecoder
import android.graphics.drawable.AnimatedImageDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the content view to the SplashFragment's layout
        setContentView(R.layout.fragment_splash)

        // Load the animated loading image in a separate thread
        Thread {
            val source = ImageDecoder.createSource(
                resources, R.drawable.loading
            )
            val drawable = ImageDecoder.decodeDrawable(source)
            val imageview = findViewById<ImageView>(R.id.loading_view)
            imageview.post {
                imageview.setImageDrawable(drawable)
                (drawable as? AnimatedImageDrawable)?.start()
            }
        }.start()

        // Handler to switch to the main layout and show HomeFragment after 2 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            // Set the content view to your activity_main layout
            setContentView(R.layout.activity_main)

            // Initialize fragments
            val homeFragment = HomeFragment()
            val archiveFragment = ArchiveFragment()
            val darkFragment = DarkFragment()
            val languageFragment = LanguageFragment()

            // Set the HomeFragment to the FrameLayout
            setCurrentFragment(homeFragment)

            val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

            bottomNavigationView.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.menu_home -> setCurrentFragment(homeFragment)
                    R.id.menu_archive -> setCurrentFragment(archiveFragment)
                    R.id.menu_dark -> setCurrentFragment(darkFragment)
                    R.id.menu_language -> setCurrentFragment(languageFragment)
                }
                true
            }
        }, 5000)  // Show splash image for 5 seconds in full screen
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
        }
}
