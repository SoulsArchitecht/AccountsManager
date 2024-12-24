package ru.sshibko.AccountsManager.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

@Configuration
public class BeanConfig {

    //TODO remove to env or properties
    private static final String AES = "AES";
    private static final String SECRET = "secret-key-12345";

    @Bean
    public Key key() {
        return  new SecretKeySpec(SECRET.getBytes(), AES);
    }

    @Bean
    public Cipher cipher() throws NoSuchAlgorithmException, NoSuchPaddingException {
        return Cipher.getInstance(AES);
    }
}
