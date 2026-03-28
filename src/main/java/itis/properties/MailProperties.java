package itis.properties;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "mail")
@Validated
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Component
public class MailProperties {
    private String sender;
    private String from;
    private String subject;
    private String content;
    private String baseUrl;
}
