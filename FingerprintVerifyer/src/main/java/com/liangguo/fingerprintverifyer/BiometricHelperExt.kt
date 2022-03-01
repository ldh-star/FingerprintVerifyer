package com.liangguo.fingerprintverifyer

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.liangguo.fingerprintverifyer.core.BiometricVerifierCallback
import com.liangguo.fingerprintverifyer.core.FingerPrintVerifier


/**
 * @author ldh
 * 时间: 2022/3/1 14:50
 * 邮箱: 2637614077@qq.com
 */

/**
 * 生物特征验证。封装了[BiometricHelper]的使用。
 * 实现了生命周期的适配。
 * 简化了回调事件的接口。
 */
fun FragmentActivity.verifierBiometricPrompt(callbackScope: SimpleBiometricPromptCallback.() -> Unit) =
    run {
        val biometricHelper = BiometricHelper(this)
        lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                when (event) {
                    Lifecycle.Event.ON_STOP -> biometricHelper.cancel()
                    Lifecycle.Event.ON_DESTROY -> biometricHelper.cancel()
                    Lifecycle.Event.ON_RESUME -> biometricHelper.authenticate()
                    else -> {
                    }
                }
            }
        })
        val simpleBiometricPromptCallback = SimpleBiometricPromptCallback()
        biometricHelper.setCallback(object : BiometricVerifierCallback() {
            override fun onSuccess() {
                simpleBiometricPromptCallback.onSuccessAction?.invoke()
            }

            override fun onCancel() {
                simpleBiometricPromptCallback.onFailedAction?.invoke()
            }

            override fun onError(errMsgId: Int, errString: CharSequence?) {
                simpleBiometricPromptCallback.onFailedAction?.invoke()
            }

            override fun onFailed() {
                simpleBiometricPromptCallback.onFailedAction?.invoke()
            }
        })
        callbackScope(simpleBiometricPromptCallback)
        biometricHelper
    }

/**
 * 在[BiometricVerifierCallback]的基础上进一步简化了回调，只提供成功和失败两个接口。
 */
class SimpleBiometricPromptCallback {

    internal var onSuccessAction: (() -> Unit)? = null

    internal var onFailedAction: (() -> Unit)? = null


    fun onSuccess(action: () -> Unit) {
        onSuccessAction = action
    }

    fun onFailed(action: () -> Unit) {
        onFailedAction = action
    }

}

/**
 * 检查当前设备指纹识别是否可用
 * @return false:支持指纹但是没有录入指纹   true:有可用指纹   null:手机不支持指纹
 */
val Context.isFingerprintAvailable: Boolean?
    get() = FingerPrintVerifier(this).checkFingerprintAvailable()