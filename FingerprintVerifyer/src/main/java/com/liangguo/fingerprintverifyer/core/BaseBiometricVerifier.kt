package com.liangguo.fingerprintverifyer.core


/**
 * @author ldh
 * 时间: 2022/1/13 20:07
 * 邮箱: 2637614077@qq.com
 */
abstract class BaseBiometricVerifier {

    /**
     * 调起指纹验证
     * @param callback
     */
    abstract fun authenticate(callback: BiometricVerifierCallback?)

    /**
     * 取消掉
     */
    abstract fun cancel()
}