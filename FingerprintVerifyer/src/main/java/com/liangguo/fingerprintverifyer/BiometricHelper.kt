package com.liangguo.fingerprintverifyer

import android.os.Build
import androidx.fragment.app.FragmentActivity
import com.liangguo.fingerprintverifyer.core.BiometricVerifier
import com.liangguo.fingerprintverifyer.core.BiometricVerifierCallback
import com.liangguo.fingerprintverifyer.core.FingerPrintVerifier


/**
 * @author ldh
 * 时间: 2022/1/13 18:57
 * 邮箱: 2637614077@qq.com
 *
 * 指纹识别的工具类。
 * 封装了安卓6以上和安卓10以上两种指纹识别方式，不支持安卓6以下。
 *
 * @param enableSystemDialog 是否弹出系统对话框进行生物识别,默认为不弹出系统对话框
 */
class BiometricHelper(activity: FragmentActivity, private val enableSystemDialog: Boolean = false) {

    private var mCallback: BiometricVerifierCallback? = null

    private val mFingerPrintVerifier =
        if (enableSystemDialog && Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // >28  Android 10以上有系统自带生物识别弹窗,可以识别指纹和面部
            BiometricVerifier(activity)
        } else {
            // 对指纹进行验证
            // 在 Android 6.0 (API 23) 之前的设备上使用
            FingerPrintVerifier(activity)
        }

    fun setCallback(callback: BiometricVerifierCallback?) = (this).apply {
        this.mCallback = callback
    }

    /**
     * 执行指纹或面部识别
     */
    fun authenticate() = (this).apply {
        mFingerPrintVerifier.authenticate(mCallback)
    }

    /**
     * 取消
     */
    fun cancel() = (this).apply {
        mFingerPrintVerifier.cancel()
    }
}