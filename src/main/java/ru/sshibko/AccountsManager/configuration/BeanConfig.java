package ru.sshibko.AccountsManager.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

@Configuration
@RequiredArgsConstructor
public class BeanConfig {

    @Value("${account.security.aes}")
    private String AES;

    @Value("${account.security.secret}")
    private String SECRET;

    @Bean
    public Key key() {
        return  new SecretKeySpec(SECRET.getBytes(), AES);
    }

    @Bean
    public Cipher cipher() throws NoSuchAlgorithmException, NoSuchPaddingException {
        return Cipher.getInstance(AES);
    }
}
