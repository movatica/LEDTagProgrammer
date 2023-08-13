package com.movatica.ledtagprogrammer

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.movatica.ledtagprogrammer.databinding.SettingsActivityBinding

class SettingsActivity : AppCompatActivity(),
    SharedPreferences.OnSharedPreferenceChangeListener {
    private lateinit var B: SettingsActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        B = SettingsActivityBinding.inflate(layoutInflater)
        setContentView(B.root)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)



        val defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        defaultSharedPreferences.registerOnSharedPreferenceChangeListener(this)

        onSharedPreferenceChanged(defaultSharedPreferences, null)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.preferences, rootKey)
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        val frames = renderAnimation(
            TagConfig(sharedPreferences),
            brightness = 4,
            AnimationConfig()
        )

        B.ivTagPreview.setImageBitmap(frames.first())
    }
}