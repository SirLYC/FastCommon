package me.lyc.fastcommon.log

import android.os.SystemClock
import me.lyc.fastcommon.FastCommonLib
import me.lyc.fastcommon.thread.waitFinishOnMain
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * Created by Liu Yuchuan on 2020/1/7.
 */
class LogUtils {
    companion object {
        @Volatile
        private var init = false

        private val loggerLock = ReentrantLock()

        private val timingMap by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { ConcurrentHashMap<String, Long>() }

        @JvmStatic
        fun config(
            logFileName: String = "FastCommon_LogFile",
            outputToConsole: Boolean = true,
            outputToFile: Boolean = true
        ) {
            Logger.globalInstance.apply {
                this.logFileName = logFileName
                this.outputToConsole = outputToConsole
                this.outputToFile = outputToFile
            }
        }

        @JvmStatic
        @JvmOverloads
        fun d(
            tag: String,
            msg: String? = null,
            ex: Throwable? = null,
            outputToConsole: Boolean = Logger.globalInstance.outputToConsole,
            outputToFile: Boolean = Logger.globalInstance.outputToFile
        ) {
            initLoggerIfNeeded()
            Logger.globalInstance.d(tag, msg, ex, outputToConsole, outputToFile)
        }

        @JvmStatic
        @JvmOverloads
        fun i(
            tag: String, msg: String? = null, ex: Throwable? = null,
            outputToConsole: Boolean = Logger.globalInstance.outputToConsole,
            outputToFile: Boolean = Logger.globalInstance.outputToFile
        ) {
            initLoggerIfNeeded()
            Logger.globalInstance.i(tag, msg, ex, outputToConsole, outputToFile)
        }

        @JvmStatic
        @JvmOverloads
        fun w(
            tag: String, msg: String? = null, ex: Throwable? = null,
            outputToConsole: Boolean = Logger.globalInstance.outputToConsole,
            outputToFile: Boolean = Logger.globalInstance.outputToFile
        ) {
            initLoggerIfNeeded()
            Logger.globalInstance.w(tag, msg, ex, outputToConsole, outputToFile)
        }

        @JvmStatic
        @JvmOverloads
        fun e(
            tag: String, msg: String? = null, ex: Throwable? = null,
            outputToConsole: Boolean = Logger.globalInstance.outputToConsole,
            outputToFile: Boolean = Logger.globalInstance.outputToFile
        ) {
            initLoggerIfNeeded()
            Logger.globalInstance.e(tag, msg, ex, outputToConsole, outputToFile)
        }

        fun waitForWriteFinish() {
            Logger.globalInstance.waitForWriteFinish()
        }

        fun startTiming(key: String) {
            timingMap[key] = SystemClock.elapsedRealtime()
        }

        fun debugLogTiming(
            tag: String,
            msg: String? = null,
            key: String,
            ex: Throwable? = null,
            outputToConsole: Boolean = Logger.globalInstance.outputToConsole,
            outputToFile: Boolean = Logger.globalInstance.outputToFile
        ) {
            logTiming(tag, msg, key, Level.DEBUG, ex, outputToConsole, outputToFile)
        }

        fun infoLogTiming(
            tag: String,
            msg: String? = null,
            key: String,
            ex: Throwable? = null,
            outputToConsole: Boolean = Logger.globalInstance.outputToConsole,
            outputToFile: Boolean = Logger.globalInstance.outputToFile
        ) {
            logTiming(tag, msg, key, Level.INFO, ex, outputToConsole, outputToFile)
        }

        fun warnLogTiming(
            tag: String,
            msg: String? = null,
            key: String,
            ex: Throwable? = null,
            outputToConsole: Boolean = Logger.globalInstance.outputToConsole,
            outputToFile: Boolean = Logger.globalInstance.outputToFile
        ) {
            logTiming(tag, msg, key, Level.WARN, ex, outputToConsole, outputToFile)
        }

        fun errorLogTiming(
            tag: String,
            msg: String? = null,
            key: String,
            ex: Throwable? = null,
            outputToConsole: Boolean = Logger.globalInstance.outputToConsole,
            outputToFile: Boolean = Logger.globalInstance.outputToFile
        ) {
            logTiming(tag, msg, key, Level.ERROR, ex, outputToConsole, outputToFile)
        }

        private fun initLoggerIfNeeded() {
            if (init) {
                return
            }

            loggerLock.withLock {
                if (init) {
                    return
                }
                waitFinishOnMain(Runnable {
                    initLogger()
                })

                init = true
            }
        }

        private fun initLogger() {
            Logger.globalInstance.apply {
                init(FastCommonLib.appContext)
            }
        }

        private fun logTiming(
            tag: String,
            msg: String? = null,
            key: String,
            level: Level,
            ex: Throwable? = null,
            outputToConsole: Boolean,
            outputToFile: Boolean
        ) {
            val current = SystemClock.elapsedRealtime()
            val lastTime = timingMap.remove(key) ?: current - 1
            val logMsg = "[${current - lastTime}ms] ${msg ?: ""}"
            when (level) {
                Level.INFO -> {
                    i(tag, logMsg, ex, outputToConsole, outputToFile)
                }

                Level.WARN -> {
                    w(tag, logMsg, ex, outputToConsole, outputToFile)
                }

                Level.ERROR -> {
                    e(tag, logMsg, ex, outputToConsole, outputToFile)
                }

                Level.DEBUG -> {
                    d(tag, logMsg, ex, outputToConsole, outputToFile)
                }
            }
        }
    }

    enum class Level {
        DEBUG, INFO, WARN, ERROR
    }
}
