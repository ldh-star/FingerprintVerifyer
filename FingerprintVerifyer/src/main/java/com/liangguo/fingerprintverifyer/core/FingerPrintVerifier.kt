package com.liangguo.fingerprintverifyer.core

import android.content.Context
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import androidx.core.os.CancellationSignal


/**
 * @author ldh
 * 时间: 2022/1/13 20:14
 * 邮箱: 2637614077@qq.com
 *
 * 对指纹进行验证
 * 在 Android 6.0 (API 23) 之前的设备上使用
 */
class FingerPrintVerifier(context: Context) : BaseBiometricVerifier() {

    private var mCancellationSignal: CancellationSignal? = null

    /**
     * 面向调用方的指纹识别回调
     */
    private var mFingerprintCallback: BiometricVerifierCallback? = null

    private var mFingerprintManager: FingerprintManagerCompat =
        FingerprintManagerCompat.from(context)

    /**
     * 执行指纹识别
     * @param callback 可选项
     */
    override fun authenticate(callback: BiometricVerifierCallback?) {
        this.mFingerprintCallback = callback
        authenticate()
    }

    /**
     * 检查指纹识别是否可用
     * @return false:支持指纹但是没有录入指纹   true:有可用指纹   null:手机不支持指纹
     */
    fun checkFingerprintAvailable(): Boolean? {
        if (!mFingerprintManager.isHardwareDetected) {
            return null
        } else if (!mFingerprintManager.hasEnrolledFingerprints()) {
            return false
        }
        return true
    }

    private fun authenticate() {
        mCancellationSignal ?: let { mCancellationSignal = CancellationSignal() }
        mFingerprintManager.authenticate(
            null,
            0,
            mCancellationSignal,
            mAuthenticationCallback,
            null
        )
    }

    /**
     * 取消指纹识别
     */
    override fun cancel() {
        mCancellationSignal?.let {
            if (!it.isCanceled) {
                it.cancel()
            }
        }
        mCancellationSignal = null
        mFingerprintCallback = null
    }

    private val mAuthenticationCallback =
        object : FingerprintManagerCompat.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: FingerprintManagerCompat.AuthenticationResult?) {
                super.onAuthenticationSucceeded(result)
                mFingerprintCallback?.onSucceededFingerPrint(result)
                mFingerprintCallback?.onSuccess()
            }

            override fun onAuthenticationError(errMsgId: Int, errString: CharSequence?) {
                super.onAuthenticationError(errMsgId, errString)
                mFingerprintCallback?.onError(errMsgId, errString)
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                mFingerprintCallback?.onFailed()
            }

        }


}