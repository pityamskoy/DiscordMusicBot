package group.sam.interaction.listeners;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static group.sam.Utility.preferablySendMessageToBotSpamChannel;

/**
 * <p>
 *     This listener is dedicated to all interaction between admins and the bot itself.
 * </p>
 */
public final class AdminListener extends ListenerAdapter {
    private static final Logger log = LoggerFactory.getLogger(AdminListener.class);

    /**
     * <p>
     *     This method sends provided message preferably for bot-spam channels in all guilds,
     *     where the bot has been established.
     * </p>
     * @param jda current the bot's JDA.
     * @param message any text message to send.
     */
    private void sendMessageToAllGuilds(@NotNull JDA jda, @NotNull String message) {
        jda.getGuilds().forEach(guild -> preferablySendMessageToBotSpamChannel(guild, message));
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        if (!message.startsWith("!announce ")) {
            return;
        }

        if (!event.getAuthor().getId().equals(System.getenv("DISCORD_ADMIN_ID"))) {
            return;
        }

        log.info("Announce message: {}", message.substring("!announce ".length()));

        sendMessageToAllGuilds(event.getJDA(), message.substring("!announce ".length()));
    }
}
