package com.energy.tajo.global.encode;

import static com.energy.tajo.global.exception.ErrorCode.PASSWORD_ENCRYPTION_ERROR;

import com.energy.tajo.global.exception.EnergyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PasswordEncoderSHA256 {

    private static final String ALGORITHM = "SHA-256";

    private PasswordEncoderSHA256() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static String encode(CharSequence rawPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
            byte[] byteData = digest.digest(rawPassword.toString().getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : byteData) {
                hexString.append(String.format("%02x", b));
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            log.error("Don't support algorithm: {}", ALGORITHM);
            throw new EnergyException(PASSWORD_ENCRYPTION_ERROR);
        }
    }

    public static boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encode(rawPassword).equals(encodedPassword);
    }
}

