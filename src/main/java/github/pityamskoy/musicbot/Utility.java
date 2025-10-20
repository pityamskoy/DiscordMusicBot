package github.pityamskoy.musicbot;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class Utility {
    @SuppressWarnings(value = {"DataFlowIssue"})
    public static void preferablySendMessageToBotSpamChannel(@NotNull Guild guild, @NotNull String message) {
        List<TextChannel> textChannelList = guild.getTextChannels();

        if (!textChannelList.isEmpty()) {
            for (TextChannel textChannel : textChannelList) {
                if (textChannel.getName().equals("bot-spam")) {
                    textChannel.sendMessage(message).queue();
                    return;
                }
            }
            guild.getDefaultChannel().asTextChannel().sendMessage(message).queue();
        }
    }

    public static int getNumberOfOnlineMembersInGuild(@NotNull Guild guild) {
        int numberOfOnlineMembers = 0;

        for (Member member : guild.getMembers()) {
            if (member.getOnlineStatus().equals(OnlineStatus.ONLINE)) {
                numberOfOnlineMembers++;
            }
        }

        return numberOfOnlineMembers;
    }

    /*
    * Use defineDeclensionOfWordPeople only for Russian language.
    * */
    @Deprecated(forRemoval = false)
    @NotNull
    public static String defineDeclensionOfWordPeople(int numberOfPeople) {
        String declensionOfWordPeople;

        if (numberOfPeople >= 2 && numberOfPeople <= 4) {
            declensionOfWordPeople = "человека";
        } else {
            declensionOfWordPeople = "человек";
        }

        return declensionOfWordPeople;
    }

    @SuppressWarnings(value = {"DataFlowIssue"})
    public static boolean isMemberConnectedToVoiceChannel(@NotNull SlashCommandInteractionEvent event) {
        GuildVoiceState guildVoiceState = event.getMember().getVoiceState();
        return guildVoiceState.inAudioChannel();
    }

    @SuppressWarnings(value = {"DataFlowIssue"})
    public static boolean handleIfImpossibleToExecuteMusicCommand(@NotNull SlashCommandInteractionEvent event) {
        try {
            Guild guild = event.getGuild();
            AudioManager audioManager = guild.getAudioManager();
            GuildVoiceState guildVoiceState = event.getMember().getVoiceState();

            if (!isMemberConnectedToVoiceChannel(event)) {
                event.reply("You are not connected to any voice channel").setEphemeral(true).queue();
                return false;
            }

            if (!audioManager.isConnected()) {
                event.reply("I'm connected to any voice channel").setEphemeral(true).queue();
                return false;
            }

            if (audioManager.getConnectedChannel() != guildVoiceState.getChannel()) {
                event.reply("We are in the different voice channels").setEphemeral(true).queue();
                return false;
            }

            return true;
        } catch (NullPointerException e) {
            event.reply("I'm sorry, a error has been occurred").setEphemeral(true).queue();
            return false;
        }
    }
}