package pityamskoy;

import pityamskoy.discordmusicbot.DiscordMusicBot;

import javax.security.auth.login.LoginException;


@SuppressWarnings("unused")
public final class Main {
    static void main(String[] arguments) {
        try {
            final DiscordMusicBot discordMusicBot = new DiscordMusicBot();
        } catch (LoginException e) {
            System.out.println("ERROR: Provided bot token is invalid!");
        }
    }
}