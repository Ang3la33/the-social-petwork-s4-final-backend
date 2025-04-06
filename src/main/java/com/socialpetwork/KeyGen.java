package com.socialpetwork;

import java.security.SecureRandom;
import java.util.Base64;

public class KeyGen {
    public static void main(String[] args) {
        byte[] secret = new byte[64]; // 512-bit
        new SecureRandom().nextBytes(secret);
        System.out.println(Base64.getEncoder().encodeToString(secret));
    }
}
