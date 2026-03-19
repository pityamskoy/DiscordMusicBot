package github.pityamskoy;

import github.pityamskoy.musicbot.MusicBot;

import javax.security.auth.login.LoginException;


@SuppressWarnings("unused")
public final class Main {
    static void main(String[] arguments) {
        try {
            final MusicBot musicBot = new MusicBot();
        } catch (LoginException e) {
            System.out.println("ERROR: Provided bot token is invalid!");
        }
    }
}