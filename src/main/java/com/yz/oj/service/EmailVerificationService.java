package com.yz.oj.service;

import com.yz.oj.util.TimeAndVerCode;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class EmailVerificationService {

    private static final long VERIFICATION_CODE_EXPIRY_TIME = 5 * 60 * 1000; // 5 minutes in milliseconds

    public boolean verifyEmail(String email, String code) {
        String storedCode = TimeAndVerCode.verCodeMap.get(email);
        Date sentTime = TimeAndVerCode.currentTimeMap.get(email);

        if (storedCode == null || sentTime == null) {
            return false; // No code sent or expired
        }

        long currentTime = System.currentTimeMillis();
        long sentTimeMillis = sentTime.getTime();

        if (currentTime - sentTimeMillis > VERIFICATION_CODE_EXPIRY_TIME) {
            return false; // Code expired
        }

        return storedCode.equals(code);
    }

}
