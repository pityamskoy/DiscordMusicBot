package github.pityamskoy.musicbot.events;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateOnlineStatusEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;

import static github.pityamskoy.musicbot.Utility.getNumberOfOnlineMembersInGuild;
import static github.pityamskoy.musicbot.Utility.preferablySendMessageToBotSpamChannel;

public final class GuildListener extends ListenerAdapter {
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

    @Override
    public void onUserUpdateOnlineStatus(@NotNull UserUpdateOnlineStatusEvent event) {
        if (event.getNewOnlineStatus().equals(OnlineStatus.ONLINE)) {
            Guild guild = event.getGuild();
            int numberOfOnlineMembers = getNumberOfOnlineMembersInGuild(guild);

            String message = MessageFormat.format("{0} has just joined to discord. " +
                            "Totally {1} people are online",
                    event.getUser().getName(), numberOfOnlineMembers);

            preferablySendMessageToBotSpamChannel(guild, message);
        }
    }
}