package com.sirlordpouya.recorder.dialog

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.pouya.voicerecorder.dialog.RecordDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        show.setOnClickListener {
            showRecorderDialog()
        }
    }

    private fun showRecorderDialog() {
        val recordDialog = RecordDialog.newInstance()
        recordDialog.setMessage("Press to record")
        recordDialog.show(supportFragmentManager, "TAG")
        recordDialog.setPositiveButton("Save") { Toast.makeText(this, "Audio path is: $it", Toast.LENGTH_LONG).show() }

    }
}
