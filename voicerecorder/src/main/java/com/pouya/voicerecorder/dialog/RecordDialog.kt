package com.pouya.voicerecorder.dialog

import android.animation.Animator
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.media.AudioFormat
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView

import com.google.android.material.floatingactionbutton.FloatingActionButton

import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Timer
import java.util.TimerTask

import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import omrecorder.AudioChunk
import omrecorder.AudioRecordConfig
import omrecorder.OmRecorder
import omrecorder.PullTransport
import omrecorder.PullableSource
import omrecorder.Recorder

class RecordDialog : DialogFragment() {
    private var _strMessage: String? = null
    private var _strPositiveButtonText: String? = null
    private var _recordButton: FloatingActionButton? = null
    private var STATE_BUTTON = "INIT"
    var audioPath: String? = null
        private set
    private var _timerView: TextView? = null
    private var _timer: Timer? = null
    private var recorderSecondsElapsed: Int = 0
    private var playerSecondsElapsed: Int = 0

    private var _clickListener: ((String?) -> Unit)? = null
    private var recorder: Recorder? = null
    private var mediaPlayer: MediaPlayer? = null
    private var mPlayer: MediaPlayer? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        setupRecorder()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        // Getting the layout inflater to inflate the view in an alert dialog.
        val inflater = LayoutInflater.from(activity)
        val rootView = inflater.inflate(R.layout.record_dialog, null)
        val strMessage = if (_strMessage == null) "Presiona para grabar" else _strMessage
        _timerView = rootView.findViewById(R.id.txtTimer)
        _timerView!!.text = strMessage
        _recordButton = rootView.findViewById(R.id.btnRecord)
        _recordButton!!.setOnClickListener {
            scaleAnimation()
            when (STATE_BUTTON) {
                "INIT" -> {
                    _recordButton!!.setImageResource(R.drawable.ic_stop)
                    STATE_BUTTON = "RECORD"
                    try {
                        mPlayer = MediaPlayer.create(context, R.raw.hangouts_message)
                        mPlayer!!.start()
                        mPlayer!!.setOnCompletionListener {
                            recorder!!.startRecording()
                            startTimer()
                        }
                    } catch (e: IllegalStateException) {
                        e.printStackTrace()
                    }

                }
                "RECORD" -> {
                    try {
                        recorder!!.stopRecording()
                        mPlayer = MediaPlayer.create(context, R.raw.pop)
                        mPlayer!!.start()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    _recordButton!!.setImageResource(R.drawable.ic_play)
                    STATE_BUTTON = "STOP"
                    _timerView!!.text = "00:00:00"
                    recorderSecondsElapsed = 0
                }
                "STOP" -> startMediaPlayer()
                "PLAY" -> pauseMediaPlayer()
                "PAUSE" -> resumeMediaPlayer()
            }
        }

        val alertDialog = AlertDialog.Builder(activity!!)
        alertDialog.setView(rootView)

        val strPositiveButton = if (_strPositiveButtonText == null) "CLOSE" else _strPositiveButtonText
        alertDialog.setPositiveButton(strPositiveButton) { _, _ ->
            if (STATE_BUTTON == "RECORD") {
                try {
                    recorder!!.stopRecording()
                    stopTimer()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
            _clickListener?.invoke(audioPath)
        }

        recorderSecondsElapsed = 0
        playerSecondsElapsed = 0

        val dialog = alertDialog.create()
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams
                .SOFT_INPUT_STATE_HIDDEN)

        return dialog
    }

    // Change End


    fun setMessage(strMessage: String) {
        _strMessage = strMessage
    }

    fun setPositiveButton(strPositiveButtonText: String, onClickListener: (path: String?) -> Unit) {
        _strPositiveButtonText = strPositiveButtonText
        _clickListener = onClickListener
    }

    private fun setupRecorder() {
        recorder = OmRecorder.wav(
                PullTransport.Default(mic(), PullTransport.OnAudioChunkPulledListener { }), file())
    }

    private fun mic(): PullableSource {
        return PullableSource.Default(
                AudioRecordConfig.Default(
                        MediaRecorder.AudioSource.MIC, AudioFormat.ENCODING_PCM_16BIT,
                        AudioFormat.CHANNEL_IN_MONO, 44100
                )
        )
    }

    @NonNull
    private fun file(): File {
        @SuppressLint("SimpleDateFormat") val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val file = File(Environment.getExternalStorageDirectory().absolutePath, "$timeStamp.wav")
        audioPath = file.path
        return file
    }

    private fun startMediaPlayer() {
        mediaPlayer = MediaPlayer()
        try {
            mediaPlayer!!.setDataSource(audioPath)
            mediaPlayer!!.prepare()
            mediaPlayer!!.setOnCompletionListener { stopMediaPlayer() }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        _recordButton!!.setImageResource(R.drawable.ic_pause)
        STATE_BUTTON = "PLAY"
        playerSecondsElapsed = 0
        startTimer()
        mediaPlayer!!.start()
    }

    private fun resumeMediaPlayer() {
        _recordButton!!.setImageResource(R.drawable.ic_pause)
        STATE_BUTTON = "PLAY"
        mediaPlayer!!.start()
    }

    private fun pauseMediaPlayer() {
        _recordButton!!.setImageResource(R.drawable.ic_play)
        STATE_BUTTON = "PAUSE"
        mediaPlayer!!.pause()
    }

    private fun stopMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer!!.stop()
            mediaPlayer!!.reset()
            mediaPlayer!!.release()
            mediaPlayer = null
            _recordButton!!.setImageResource(R.drawable.ic_play)
            STATE_BUTTON = "STOP"
            _timerView!!.text = "00:00:00"
            stopTimer()
        }
    }

    private fun startTimer() {
        stopTimer()
        _timer = Timer()
        _timer!!.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                updateTimer()
            }
        }, 0, 1000)
    }

    private fun stopTimer() {
        if (_timer != null) {
            _timer!!.cancel()
            _timer!!.purge()
            _timer = null
        }
    }

    private fun updateTimer() {
        // here you check the value of getActivity() and break up if needed
        if (activity == null)
            return

        activity?.runOnUiThread {
            if (STATE_BUTTON == "RECORD") {
                recorderSecondsElapsed++
                _timerView!!.text = Util.formatSeconds(recorderSecondsElapsed)
            } else if (STATE_BUTTON == "PLAY") {
                playerSecondsElapsed++
                _timerView!!.text = Util.formatSeconds(playerSecondsElapsed)
            }
        }
    }

    private fun scaleAnimation() {
        _recordButton!!.animate()
                .scaleX(1.1f)
                .scaleY(1.1f)
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {

                    }

                    override fun onAnimationEnd(animation: Animator) {
                        _recordButton!!.animate().scaleX(1f).scaleY(1f).start()
                    }

                    override fun onAnimationCancel(animation: Animator) {

                    }

                    override fun onAnimationRepeat(animation: Animator) {

                    }
                })
    }

    override fun onPause() {
        super.onPause()
        dismiss()
    }

    companion object {
        /**
         * new Insstance method
         * @return
         */
        fun newInstance(): RecordDialog {
            return RecordDialog()
        }
    }
}
