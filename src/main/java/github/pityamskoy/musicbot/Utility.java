package github.pityamskoy.musicbot;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
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
}