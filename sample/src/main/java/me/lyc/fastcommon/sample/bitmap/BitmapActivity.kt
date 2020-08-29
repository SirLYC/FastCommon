package me.lyc.fastcommon.sample.bitmap

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_bitmap.*
import me.lyc.fastcommon.bitmap.decodeBitmap
import me.lyc.fastcommon.log.LogUtils
import me.lyc.fastcommon.sample.R
import me.lyc.fastcommon.thread.ExecutorFactory

/**
 * Created by Liu Yuchuan on 2020/8/29.
 */
class BitmapActivity : AppCompatActivity() {
    @Volatile
    private var destroy = false

    private companion object {
        const val TAG = "BitmapActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bitmap)
        val uri = intent?.data ?: return
        ExecutorFactory.IO_EXECUTOR.execute {
            if (destroy) {
                return@execute
            }

            try {
                val orgBitmap = decodeBitmap(uri)

                if (destroy) {
                    return@execute
                }

                if (orgBitmap != null) {
                    ExecutorFactory.MAIN_EXECUTOR.execute {
                        org_pic.setImageBitmap(orgBitmap)
                    }
                }

                val bitmap1000x1000 = decodeBitmap(uri, 1000, 1000)
                if (destroy) {
                    return@execute
                }

                if (bitmap1000x1000 != null) {
                    ExecutorFactory.MAIN_EXECUTOR.execute {
                        pic_1000x1000.setImageBitmap(bitmap1000x1000)
                    }
                }

                val bitmap900x1200 = decodeBitmap(uri, 900, 1200)
                if (destroy) {
                    return@execute
                }

                if (bitmap900x1200 != null) {
                    ExecutorFactory.MAIN_EXECUTOR.execute {
                        pic_900x1200.setImageBitmap(bitmap900x1200)
                    }
                }

                val bitmap1200x900 = decodeBitmap(uri, 1200, 900)
                if (destroy) {
                    return@execute
                }

                if (bitmap1200x900 != null) {
                    ExecutorFactory.MAIN_EXECUTOR.execute {
                        pic_1200x900.setImageBitmap(bitmap1200x900)
                    }
                }
            } catch (t: Throwable) {
                LogUtils.e(TAG, ex = t)
            }
        }
    }

    override fun onDestroy() {
        destroy = true
        super.onDestroy()
    }
}
