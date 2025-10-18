package github.pityamskoy.musicbot.events;

import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import static github.pityamskoy.musicbot.Utility.preferablySendMessageToBotSpamChannel;

public final class QuantityListener extends ListenerAdapter {
    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        String message = event.getUser().getAsMention() + " Hello! Let's go to a voice chat";

        preferablySendMessageToBotSpamChannel(event.getGuild(), message);
    }

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        String message = "Unfortunately, " + event.getUser().getAsMention() + " left from the server";

        preferablySendMessageToBotSpamChannel(event.getGuild(), message);
    }
}