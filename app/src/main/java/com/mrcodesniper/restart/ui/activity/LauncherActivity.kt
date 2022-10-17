package com.mrcodesniper.restart.ui.activity

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.mrcodesniper.restart.view.CanvasView

class LauncherActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(CanvasView(this))
    }


}