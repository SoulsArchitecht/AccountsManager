package ru.sshibko.AccountsManager.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
@Getter
@Setter
public class AppInfoProperties {

    private Contact contact;
    private Version version;

    @Getter
    @Setter
    public static class Contact{
        private String email;
        private String phone;
        private String telegram;
    }

    @Getter
    @Setter
    public static class Version{
        private String number;
        private String status;
    }
}
