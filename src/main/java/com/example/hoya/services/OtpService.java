package com.example.hoya.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class OtpService {

//    private OtpGenerator otpGenerator;
//    private EmailService emailService;
//    private UserService userService;
//
//    /**
//     * Method for generate OTP number
//     *
//     * @param key - provided key (username in this case)
//     * @return boolean value (true|false)
//     */
//    public Boolean generateOtp(String key)
//    {
//        // generate otp
//        Integer otpValue = otpGenerator.generateOTP(key);
//        if (otpValue == -1)
//        {
//            LOGGER.error("OTP generator is not working...");
//            return  false;
//        }
//
//        LOGGER.info("Generated OTP: {}", otpValue);
//
//        // fetch user e-mail from database
//        String userEmail = userService.findEmailByUsername(key);
//        List<String> recipients = new ArrayList<>();
//        recipients.add(userEmail);
//
//        // generate emailDTO object
//        EmailDTO emailDTO = new EmailDTO();
//        emailDTO.setSubject("Spring Boot OTP Password.");
//        emailDTO.setBody("OTP Password: " + otpValue);
//        emailDTO.setRecipients(recipients);
//
//        // send generated e-mail
//        return emailService.sendSimpleMessage(emailDTO);
//    }
//
//    /**
//     * Method for validating provided OTP
//     *
//     * @param key - provided key
//     * @param otpNumber - provided OTP number
//     * @return boolean value (true|false)
//     */
//    public Boolean validateOTP(String key, Integer otpNumber)
//    {
//        // get OTP from cache
//        Integer cacheOTP = otpGenerator.getOPTByKey(key);
//        if (cacheOTP!=null && cacheOTP.equals(otpNumber))
//        {
//            otpGenerator.clearOTPFromCache(key);
//            return true;
//        }
//        return false;
//    }
}
