package pl.mrugames.mzcreeper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Console;
import java.util.Base64;
import java.util.Scanner;
import java.util.prefs.Preferences;

@Component
public class AuthenticationManager {
    private final Preferences preferences;
    private final ApplicationContext context;

    private static final String YOUR_LOGIN_KEY = "login_key";
    private static final String YOUR_PASSWORD_KEY = "password_key";

    @Autowired
    public AuthenticationManager(ApplicationContext ac) {
        preferences = Preferences.userNodeForPackage(AuthenticationManager.class);
        this.context = ac;
    }

    @PostConstruct
    private void postConstruct() {
        if (preferences.get(YOUR_LOGIN_KEY, null) == null) {
            save(YOUR_LOGIN_KEY, read("Enter your MZ login: "));
        }

        if (preferences.get(YOUR_PASSWORD_KEY, null) == null) {
            save(YOUR_PASSWORD_KEY, read("Enter your MZ password: "));
        }
    }

    public String getMZLogin() {
        return new String(Base64.getDecoder().decode(preferences.get(YOUR_LOGIN_KEY, "")));
    }

    public String getMzPassword() {
        return new String(Base64.getDecoder().decode(preferences.get(YOUR_PASSWORD_KEY, "")));
    }

    public void reset() {
        preferences.remove(YOUR_LOGIN_KEY);
        preferences.remove(YOUR_PASSWORD_KEY);
    }

    private void save(String key, String rawInput) {
        String encoded = Base64.getEncoder().encodeToString(rawInput.getBytes());
        preferences.put(key, encoded);
    }

    private String read(String question) {
        Console console = System.console();

        if (console != null) {
            char[] value = System.console().readPassword(question);
            return new String(value);

        }

        Scanner scanner = (Scanner) context.getBean("SystemInScanner");
        System.out.print(question);
        return scanner.nextLine();
    }
}
