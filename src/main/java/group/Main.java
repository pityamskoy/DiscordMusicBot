package group;

import group.sam.Sam;

import javax.security.auth.login.LoginException;


@SuppressWarnings("unused")
public final class Main {
    static void main(String[] arguments) {
        try {
            final Sam sam = new Sam();
        } catch (LoginException e) {
            System.out.println("ERROR: Provided bot token is invalid!");
        }
    }
}