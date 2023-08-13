package com.movatica.ledtagprogrammer

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.preference.PreferenceManager
import com.google.android.material.snackbar.Snackbar
import com.movatica.ledtagprogrammer.databinding.ActivityMainBinding
import com.movatica.ledtagprogrammer.extensions.copy
import com.movatica.ledtagprogrammer.lib.AnimatedGifEncoder
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity() {
    private lateinit var B: ActivityMainBinding

    private var previewFile: ByteArray? = null
    private var previewFileType: String = ""

    private var animationConfig = AnimationConfig()
/*
    // get device handle and permission on plugging in
    private lateinit var usbManager: UsbManager
    private var usbDevice: UsbDevice? = null

    private val usbActionReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == UsbManager.ACTION_USB_DEVICE_DETACHED)
            {
                handleIntent(intent)
            }
        }
    }
*/
    // TODO: handle app launch when no device is attached
    // -> disable program button
    // -> enable, when device is detected later
    // -> manage this in a separate thread
    // or maybe just use program button for searching the device?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        B = ActivityMainBinding.inflate(layoutInflater)
        setContentView(B.root)

        // initialize preferences
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)

        B.spMode.adapter = ArrayAdapter.createFromResource(
            this,
            R.array.mode_entries,
            android.R.layout.simple_spinner_item
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        // load config values
        B.etContent.setText(animationConfig.text)
        B.spMode.setSelection(animationConfig.mode.index)
        B.sbSpeed.progress = animationConfig.speed
        B.swFlash.isChecked = animationConfig.flash
        B.swBorder.isChecked = animationConfig.border

        // add user input listeners
        B.etContent.addTextChangedListener {
            animationConfig.text = it.toString()
            updatePreview()
        }

        B.spMode.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                animationConfig.mode = Mode.valueOfIndex(position)
                updatePreview()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        B.sbSpeed.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                animationConfig.speed = progress
                updatePreview()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        B.swFlash.setOnCheckedChangeListener { _, isChecked -> animationConfig.flash = isChecked }

        B.swBorder.setOnCheckedChangeListener { _, isChecked -> animationConfig.border = isChecked }

        B.btProgram.setOnClickListener {
            updatePreview()
            programDevice()
        }

        // setup USB device handling
 /*       usbManager = getSystemService(USB_SERVICE) as UsbManager
        val usbActionFilter = IntentFilter().apply { addAction(UsbManager.ACTION_USB_DEVICE_DETACHED) }
        registerReceiver(usbActionReceiver, usbActionFilter)
*/
        handleIntent(intent)

        updatePreview()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
/*        unregisterReceiver(usbActionReceiver)
*/
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.settings, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.siShareGIF -> {
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.MIME_TYPE, previewFileType)
                    put(MediaStore.MediaColumns.DATE_ADDED, System.currentTimeMillis())
                    put(MediaStore.MediaColumns.DISPLAY_NAME, "animation")
                }

                val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                    ?: return false

                contentResolver.openOutputStream(uri)?.apply {
                    write(previewFile)
                    close()
                }

                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = previewFileType
                    putExtra(Intent.EXTRA_STREAM, uri)
                }

                startActivity(Intent.createChooser(intent, "Share Preview"))
                true
            }
            R.id.siConfigureTag -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showErrorMessage(message: String)
    {
        Snackbar.make(B.root, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun handleIntent(intent: Intent) {
        // handle usb device attach/detach intents
        /*
        @Suppress("DEPRECATION")
        usbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE)
         */
    }

    private fun programDevice() {
        // program device

        // compile bytecode
        // TODO: extract this into a class
        // TODO: allow the actual 8 different messages
/*
        val payload = compileBytecode(
            B.etContent.text.toString(),
            B.sbAnimationSpeed.progress,
            0,
            B.swBlinkEffect.isChecked,
            B.swBorderEffect.isChecked,
            B.sbBrightness.progress
        )

        // trying to program more than 8kb will damage the device
        if (payload.size > 8192)
        {
            showErrorMessage("Payload too large")
            return
        }

        if (usbDevice == null) {
            showErrorMessage("USB Device not available")
            return
        }

        usbDevice?.getInterface(0)?.also { intf ->
            for (e in 0 until intf.endpointCount) {
                val endpoint = intf.getEndpoint(e) ?: continue

                if (endpoint.direction == UsbConstants.USB_DIR_OUT) {
                    usbManager.openDevice(usbDevice)?.apply {
                        claimInterface(intf, true)
                        bulkTransfer(endpoint, payload, payload.size, 0) //do in another thread
                    }
                    break
                }
            }
        }
*/
        //val result = programDevice(payload)

        //if (!result) {
        //    showErrorMessage("Programming not successful")
        //    return
        //}

        // TODO: check if device is available
        // TODO: do USB device detection and handling somewhere else
        // TODO: program device
    }

    private fun updatePreview() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val tagConfig = TagConfig(preferences)

        val frames = renderAnimation(tagConfig,4, animationConfig)

        B.ivPreview.setOnClickListener(null)

        if (frames.size == 1) {
            B.ivPreview.setImageBitmap(frames.first())
        }
        else if (frames.size > 1) {
            val animation = AnimationDrawable()
            B.ivPreview.setImageDrawable(animation)

            for (frame in frames) {
                animation.addFrame(BitmapDrawable(resources, frame), 200)
            }

            animation.start()

            B.ivPreview.setOnClickListener { if (animation.isRunning) animation.stop() else animation.start() }
        }

        //makeImageFile(frames)
    }

    private fun makeImageFile(frames: List<Bitmap>) {
        val outputStream = ByteArrayOutputStream()

        if (frames.size == 1) {
            previewFileType = "image/jpeg"

            frames.first().compress(Bitmap.CompressFormat.PNG, 80, outputStream)
        }
        else {
            previewFileType = "image/gif"

            val encoder = AnimatedGifEncoder()

            encoder.start(outputStream)

            for (frame in frames) {
                encoder.addFrame(frame.copy())
            }

            encoder.finish()
        }

        previewFile = outputStream.toByteArray()
    }
}