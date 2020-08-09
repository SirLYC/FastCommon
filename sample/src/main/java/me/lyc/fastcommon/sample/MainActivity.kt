package me.lyc.fastcommon.sample

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import me.lyc.fastcommon.FastCommonLib
import me.lyc.fastcommon.log.LogUtils
import me.lyc.fastcommon.permission.checkAndRequestPermissions
import me.lyc.fastcommon.toast.showLongToast

/**
 * Created by Liu Yuchuan on 2020/8/2.
 */
class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        FastCommonLib.init(this)

        bt_permission.setOnClickListener {
            checkAndRequestPermissions(
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO
                ),
                {
                    showLongToast("Granted!")
                    LogUtils.d(TAG, "Granted!")
                },
                { _, rejected ->
                    showLongToast("Rejected: ${rejected.contentToString()}")
                    LogUtils.d(TAG, "Rejected: ${rejected.contentToString()}")
                }
            )
        }
    }
}
