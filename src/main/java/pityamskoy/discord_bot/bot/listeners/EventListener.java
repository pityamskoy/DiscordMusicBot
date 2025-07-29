package pityamskoy.discord_bot.bot.listeners;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


public class EventListener extends ListenerAdapter {

    @NotNull
    public Optional<TextChannel> getBotSpamTextChannel(@NotNull Guild guild) {
        List<TextChannel> textChannelList = guild.getTextChannels();
        if (textChannelList.isEmpty()) {
            return Optional.empty();
        }
        for (TextChannel textChannel : textChannelList) {
            if (textChannel.getName().equals("bot-spam")) {
                return Optional.of(textChannel);
            }
        }
        return Optional.of(Objects.requireNonNull(guild.getDefaultChannel()).asTextChannel());
    }

    public void sendMessage(@NotNull Optional<TextChannel> optionalTextChannel, @NotNull String message) {
        //the variable optionalTextChannel should be the result of calling the getBotSpamTextChannel()
        optionalTextChannel.ifPresent(textchannel -> textchannel.sendMessage(message).queue());
    }

    @Override
    public void onGuildMemberJoin (@NotNull GuildMemberJoinEvent event){
        User user = event.getUser();
        String message = user.getAsMention() + " дары! Лара в войс к пацанам";
        sendMessage(getBotSpamTextChannel(event.getGuild()), message);
    }
}
