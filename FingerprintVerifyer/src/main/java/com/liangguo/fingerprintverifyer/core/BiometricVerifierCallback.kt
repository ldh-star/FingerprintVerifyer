package com.liangguo.fingerprintverifyer.core

import androidx.biometric.BiometricPrompt
import androidx.core.hardware.fingerprint.FingerprintManagerCompat

/**
 * 验证结果回调，供使用者调用
 */
abstract class BiometricVerifierCallback {

    /**
     * 验证成功
     */
    open fun onSucceededBiometric(result: BiometricPrompt.AuthenticationResult) {}

    /**
     * 验证成功
     */
    open fun onSucceededFingerPrint(result: FingerprintManagerCompat.AuthenticationResult?) {}

    /**
     * 验证失败
     */
    open fun onFailed() {}

    /**
     * 验证失败
     */
    open fun onError(errMsgId: Int, errString: CharSequence?) {}

    /**
     * 取消验证
     */
    open fun onCancel() {}

    /**
     * 成功
     */
    open fun onSuccess() {}

}