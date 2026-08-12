/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package org.telegram.messenger.support.fingerprint;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Handler;

import org.telegram.messenger.FileLog;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.security.Signature;

import javax.crypto.Cipher;
import javax.crypto.Mac;

@TargetApi(23)
public final class FingerprintManagerCompatApi23 {

    private static Object getFingerprintManager(Context ctx) {
        try {
            return ctx.getSystemService("fingerprint");
        } catch (Throwable e) {
            return null;
        }
    }

    public static boolean hasEnrolledFingerprints(Context context) {
        try {
            Object fingerprintManager = getFingerprintManager(context);
            if (fingerprintManager == null) {
                return false;
            }
            Method method = fingerprintManager.getClass().getMethod("hasEnrolledFingerprints");
            return (Boolean) method.invoke(fingerprintManager);
        } catch (Throwable e) {
            FileLog.e(e);
        }
        return false;
    }

    public static boolean isHardwareDetected(Context context) {
        try {
            Object fingerprintManager = getFingerprintManager(context);
            if (fingerprintManager == null) {
                return false;
            }
            Method method = fingerprintManager.getClass().getMethod("isHardwareDetected");
            return (Boolean) method.invoke(fingerprintManager);
        } catch (Throwable e) {
            FileLog.e(e);
        }
        return false;
    }

    public static void authenticate(Context context, CryptoObject crypto, int flags, Object cancel,
                                    AuthenticationCallback callback, Handler handler) {
        try {
            Object fingerprintManager = getFingerprintManager(context);
            if (fingerprintManager == null) {
                return;
            }
            Object cryptoObject = wrapCryptoObject(crypto);
            Object callbackObject = wrapCallback(callback);

            Method authenticateMethod = fingerprintManager.getClass().getMethod("authenticate",
                    Class.forName("android.hardware.fingerprint.FingerprintManager$CryptoObject"),
                    android.os.CancellationSignal.class,
                    int.class,
                    Class.forName("android.hardware.fingerprint.FingerprintManager$AuthenticationCallback"),
                    Handler.class);

            authenticateMethod.invoke(fingerprintManager, cryptoObject, (android.os.CancellationSignal) cancel, flags, callbackObject, handler);
        } catch (Throwable e) {
            FileLog.e(e);
        }
    }

    private static Object wrapCryptoObject(CryptoObject cryptoObject) {
        if (cryptoObject == null) {
            return null;
        }
        try {
            Class<?> cls = Class.forName("android.hardware.fingerprint.FingerprintManager$CryptoObject");
            if (cryptoObject.getCipher() != null) {
                Constructor<?> constructor = cls.getConstructor(Cipher.class);
                return constructor.newInstance(cryptoObject.getCipher());
            } else if (cryptoObject.getSignature() != null) {
                Constructor<?> constructor = cls.getConstructor(Signature.class);
                return constructor.newInstance(cryptoObject.getSignature());
            } else if (cryptoObject.getMac() != null) {
                Constructor<?> constructor = cls.getConstructor(Mac.class);
                return constructor.newInstance(cryptoObject.getMac());
            }
        } catch (Throwable e) {
            FileLog.e(e);
        }
        return null;
    }

    @SuppressWarnings("unused")
    private static CryptoObject unwrapCryptoObject(Object cryptoObject) {
        if (cryptoObject == null) {
            return null;
        }
        try {
            Method getCipher = cryptoObject.getClass().getMethod("getCipher");
            Cipher cipher = (Cipher) getCipher.invoke(cryptoObject);
            if (cipher != null) return new CryptoObject(cipher);

            Method getSignature = cryptoObject.getClass().getMethod("getSignature");
            Signature signature = (Signature) getSignature.invoke(cryptoObject);
            if (signature != null) return new CryptoObject(signature);

            Method getMac = cryptoObject.getClass().getMethod("getMac");
            Mac mac = (Mac) getMac.invoke(cryptoObject);
            if (mac != null) return new CryptoObject(mac);
        } catch (Throwable e) {
            FileLog.e(e);
        }
        return null;
    }

    private static Object wrapCallback(final AuthenticationCallback callback) {
        // FingerprintManager.AuthenticationCallback is an abstract class.
        // Implementing it via reflection is not possible without a stub or bytecode generation.
        // Since we are compiling against SDK 37 where it's removed, we cannot reference it.
        return null;
    }

    public static class CryptoObject {

        private final Signature mSignature;
        private final Cipher mCipher;
        private final Mac mMac;

        public CryptoObject(Signature signature) {
            mSignature = signature;
            mCipher = null;
            mMac = null;
        }

        public CryptoObject(Cipher cipher) {
            mCipher = cipher;
            mSignature = null;
            mMac = null;
        }

        public CryptoObject(Mac mac) {
            mMac = mac;
            mCipher = null;
            mSignature = null;
        }

        public Signature getSignature() {
            return mSignature;
        }

        public Cipher getCipher() {
            return mCipher;
        }

        public Mac getMac() {
            return mMac;
        }
    }

    public static final class AuthenticationResultInternal {
        private CryptoObject mCryptoObject;

        public AuthenticationResultInternal(CryptoObject crypto) {
            mCryptoObject = crypto;
        }

        public CryptoObject getCryptoObject() {
            return mCryptoObject;
        }
    }

    public static abstract class AuthenticationCallback {

        public void onAuthenticationError(int errMsgId, CharSequence errString) {
        }

        public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        }

        public void onAuthenticationSucceeded(AuthenticationResultInternal result) {
        }

        public void onAuthenticationFailed() {
        }
    }
}
