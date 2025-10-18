package github.pityamskoy.musicbot.events;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.user.update.UserUpdateOnlineStatusEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;

import static github.pityamskoy.musicbot.Utility.*;

public final class StatusListener extends ListenerAdapter {
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
