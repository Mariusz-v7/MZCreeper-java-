package pl.mrugames.mzcreeper;

import org.springframework.stereotype.Component;

import java.io.Console;
import java.util.Base64;
import java.util.Scanner;
import java.util.prefs.Preferences;

@Component
public class AuthenticationManager {
    private Preferences preferences;

    private static final String YOUR_LOGIN_KEY = "login_key";
    private static final String YOUR_PASSWORD_KEY = "password_key";

    public AuthenticationManager() {
        preferences = Preferences.userNodeForPackage(AuthenticationManager.class);

        Console console = System.console();
        Scanner scanner = null;

        if (console == null) {
            scanner = new Scanner(System.in);
        }

        if (preferences.get(YOUR_LOGIN_KEY, null) == null) {
            char[] login;
            if (console != null) {
                login = System.console().readPassword("Enter your MZ login: ");
            } else {
                System.out.print("Enter your MZ login: ");
                login = scanner.nextLine().toCharArray();
            }

            save(YOUR_LOGIN_KEY, login);
        }

        if (preferences.get(YOUR_PASSWORD_KEY, null) == null) {
            char[] pass;
            if (console != null) {
                pass = System.console().readPassword("Enter your MZ password: ");
            } else {
                System.out.print("Enter your MZ password: ");
                pass = scanner.nextLine().toCharArray();
            }

            save(YOUR_PASSWORD_KEY, pass);
        }

        if (scanner != null) {
            scanner.close();
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

    private void save(String key, char[] rawInput) {
        String encoded = Base64.getEncoder().encodeToString(new String(rawInput).getBytes());
        preferences.put(key, encoded);
    }
}
