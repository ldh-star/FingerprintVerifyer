package com.liangguo.fingerprintverifyer.core

import android.os.Handler
import android.os.Looper
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import com.liangguo.fingerprintverifyer.R
import java.lang.ref.WeakReference
import java.util.concurrent.Executor


/**
 * @author ldh
 * 时间: 2022/1/13 20:10
 * 邮箱: 2637614077@qq.com
 *
 * 系统对话框验证指纹或者面部识别等生物特征
 * 运行在 Android 9.0 (API 28) 及更高版本的设备上
 */
class BiometricVerifier(fragmentActivity: FragmentActivity) : BaseBiometricVerifier() {

    private val mExecutor =
        Executor { command -> Handler(Looper.getMainLooper()).post(command) }


    private val mActivityWeakReference = WeakReference(fragmentActivity)

    /**
     * 这么做是为了执行取消
     */
    private var mBiometricPrompt: BiometricPrompt? = null

    override fun authenticate(callback: BiometricVerifierCallback?) {
        val activity = mActivityWeakReference.get()
        activity ?: return
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(activity.getText(R.string.please_verify_finger_point))
            .setNegativeButtonText(activity.getText(R.string.cancel))
            .build()

        //需要提供的参数callback
        val biometricPrompt = BiometricPrompt(
            activity,
            mExecutor, object : BiometricPrompt.AuthenticationCallback() {
                //各种异常的回调
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    if (callback != null) {
                        if (errorCode == 13) {
                            callback.onCancel()
                        } else {
                            callback.onError(errorCode, errString)
                        }
                    }
                }

                //认证成功的回调
                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    callback?.onSucceededBiometric(result)
                    callback?.onSuccess()
                }

                //认证失败的回调
                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    callback?.onFailed()
                }
            })
        // 显示认证对话框
        biometricPrompt.authenticate(promptInfo)
        mBiometricPrompt = biometricPrompt

    }

    override fun cancel() {
        mBiometricPrompt?.cancelAuthentication()
        mBiometricPrompt = null
    }

}