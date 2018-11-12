package com.sirlordpouya.recorder.dialog

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.pouya.voicerecorder.dialog.RecordDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var recordDialog: RecordDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        show.setOnClickListener {
            showRecorderDialog()
        }
    }

    private fun showRecorderDialog() {
        recordDialog = RecordDialog.newInstance()
        recordDialog.setMessage("Press for record")
        recordDialog.show(supportFragmentManager, "TAG")
        recordDialog.setPositiveButton("Save") { Toast.makeText(this@MainActivity, "Save audio: $it", Toast.LENGTH_LONG).show() }

    }
}
