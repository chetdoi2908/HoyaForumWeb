package com.example.hoya.services;

import com.example.hoya.entities.User;
import com.example.hoya.entities.UserPrincipal;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class OtpService {
    private OtpGenerator otpGenerator;
    private EmailService emailService;
    private UserService userService;
    private JavaMailSender mailSender;

    public Boolean generateOtp(String email) throws MessagingException {
        // generate otp
        Integer otpValue = otpGenerator.generateOTP(email);
        System.out.println(otpValue);
        if (otpValue == -1)
        {
            return  false;
        }

        // fetch user e-mail from database
        // generate emailDTO object
        emailService.send(email, "Đây là mã OTP: " + otpValue, "Lấy lại mật khẩu");
        // send generated e-mail
        return true;
    }
        // no ko nhan dc email kia
    /**
     * Method for validating provided OTP
     *
     * @param key - provided key
     * @param otpNumber - provided OTP number
     * @return boolean value (true|false)
     */
    public Boolean validateOTP(String key, Integer otpNumber)
    {
        // get OTP from cache
        Integer cacheOTP = otpGenerator.getOPTByKey(key);
        if (cacheOTP!=null && cacheOTP.equals(otpNumber))
        {
            otpGenerator.clearOTPFromCache(key);
            return true;
        }
        return false;
    }
}
