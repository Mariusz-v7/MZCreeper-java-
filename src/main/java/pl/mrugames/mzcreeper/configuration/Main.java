package pl.mrugames.mzcreeper.configuration;

import pl.mrugames.commons.MruGamesLauncher;

import java.io.IOException;

public class Main {
    public static void main(String... args) throws IOException {
        MruGamesLauncher launcher = new MruGamesLauncher("internal_config.properties", "config/config.properties", MainConfiguration.class);
        launcher.launch();
    }
}
