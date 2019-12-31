package io.github.t3r1jj.pbmap.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.github.t3r1jj.pbmap.R

/**
 * Preferences screen
 */
open class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings_container, SettingsFragment())
                .commit()
    }
}

