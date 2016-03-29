package com.groman.openam.yubikey;

import com.yubico.client.v2.VerificationResponse;
import com.yubico.client.v2.YubicoClient;
import com.yubico.client.v2.exceptions.YubicoValidationFailure;
import com.yubico.client.v2.exceptions.YubicoVerificationException;

/**
 * Hello world!
 *
 */
public class App 
{
    private static final int CLIENT_ID = 27893;
    private static final String API_KEY = "iO5AYOogxAzrBJeEhvCnH/PLyfc=";
    
    private static final YubicoClient client = YubicoClient.getClient(CLIENT_ID, API_KEY);
    
    public static void main( String[] args ) throws YubicoVerificationException, YubicoValidationFailure
    {
        String otp = "ccccccegjjrvnjrglrggugflcegnttnkvivufkniunir";
        if (!YubicoClient.isValidOTPFormat(otp)) {
            System.err.println("Invalid OTP format");
            System.exit(-1);
        }
        
        VerificationResponse response = client.verify(otp);
        if (response.isOk()) {
            String yubikeyId = YubicoClient.getPublicId(otp);
            System.out.println("Valid OTP for Id: " + yubikeyId);
        } else {
            System.err.println("Invalid OTP");
            System.exit(-1);
        }
    }
}
