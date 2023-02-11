package com.sas.controller;

import com.sas.config.EmailTemplate;
import com.sas.service.EmailService;
import com.sas.service.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.Map;


@Controller
public class OTPController {

    private final String SUCCESS = "Entered Otp is valid";

    private final String FAIL = "Entered Otp is NOT valid. Please Retry!";

    @Autowired
    public OTPService otpService;

    @Autowired
    public EmailService emailService;

    @GetMapping("/generateOtp")
    public String generateOTP() throws MessagingException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        int otp = otpService.generateOTP(username);
        EmailTemplate template = new EmailTemplate("templates/SendOtp.html");
        Map<String, String> replacements = new HashMap<String, String>();
        replacements.put("user", username);
        replacements.put("otpnum", String.valueOf(otp));
        String message = template.getTemplate(replacements);
        emailService.sendOtpMessage(username, "OTP -SpringBoot", message);
        return "otppage";
    }

    @RequestMapping(value = "/validateOtp", method = RequestMethod.GET)
    public @ResponseBody
    String validateOtp(@RequestParam("otpnum") int otpnum) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        if (otpnum >= 0) {
            int serverOtp = otpService.getOtp(username);
            if (serverOtp > 0) {
                if (otpnum == serverOtp) {
                    otpService.clearOTP(username);
                }
                return SUCCESS;
            } else {
                return FAIL;
            }
        } else {
            return FAIL;
        }
    }
}
