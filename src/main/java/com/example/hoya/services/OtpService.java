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

    /**
     * Constructor dependency injector
     * @param otpGenerator - otpGenerator dependency
     * @param emailService - email service dependency
     * @param userService - user service dependency
     */



    /**
     * Method for generate OTP number
     *
     * @param key - provided key (username in this case)
     * @return boolean value (true|false)
     */
    public Boolean generateOtp(String key) throws MessagingException {
        // generate otp
        Integer otpValue = otpGenerator.generateOTP(key);
        System.out.println(otpValue);
        if (otpValue == -1)
        {
            return  false;
        }

        // fetch user e-mail from database
        User user = userService.findByEmail(key);
        // generate emailDTO object
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        helper.setText("OTP Password: " + otpValue, true);
        helper.setSubject("Spring Boot OTP Password.");
        helper.setTo(user.getEmail());
        mailSender.send(mimeMessage);

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
        System.out.println("key" + key);
        System.out.println("otpNumber" + otpNumber);
        Integer cacheOTP = otpGenerator.getOPTByKey(key);
        System.out.println("cacheotp" + cacheOTP);
        if (cacheOTP!=null && cacheOTP.equals(otpNumber))
        {
            otpGenerator.clearOTPFromCache(key);
            return true;
        }
        return false;
    }
}
