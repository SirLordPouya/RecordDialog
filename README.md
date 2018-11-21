# Voice Recorder Dialog 


[![](https://jitpack.io/v/SirLordPouya/RecordDialog.svg)](https://jitpack.io/#SirLordPouya/RecordDialog)
[![API](https://img.shields.io/badge/API-16%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=16)
[![Build Status](https://travis-ci.org/SirLordPouya/VoiceRecorderDialog.svg?branch=master)](https://travis-ci.org/SirLordPouya/VoiceRecorderDialog)


A Simple Wav audio recorder dialog based on [RecordDialog](https://github.com/IvanSotelo/RecordDialog)

<img src="https://github.com/SirLordPouya/VoiceRecorderDialog/blob/master/voice_recorder_dialog.gif" width="400">

## Releases:

#### Current release: [![](https://jitpack.io/v/SirLordPouya/RecordDialog.svg)](https://jitpack.io/#SirLordPouya/RecordDialog)

## Usage:

#### Permissions
Add these permissions into your `AndroidManifest.xml` and [request for them in Android 6.0+](https://developer.android.com/training/permissions/requesting.html) Min API: 18
```xml
<uses-permission android:name="android.permission.RECORD_AUDIO"/>
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
```


### Creating the dialog

```
    private fun showRecorderDialog() {
        val recordDialog = RecordDialog.newInstance()
        recordDialog.setMessage("Press for record")
        recordDialog.show(supportFragmentManager, "TAG")
        recordDialog.setPositiveButton("Save") { Toast.makeText(this@MainActivity, "Save audio: $it", Toast.LENGTH_LONG).show() }

    }
```

## Download

#### Adding the depencency

Add this to your root *build.gradle* file:

```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Now add the dependency to your app build.gradle file:

```groovy
implementation 'com.github.SirLordPouya:RecordDialog:2.0.2'
```

## License

```
The MIT License (MIT)

Copyright (c) 2018 Pouya Heydari

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
