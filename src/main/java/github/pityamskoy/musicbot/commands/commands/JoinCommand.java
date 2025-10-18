package github.pityamskoy.musicbot.commands.commands;

import github.pityamskoy.musicbot.commands.MusicBotCommand;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

@SuppressWarnings(value = {"DataFlowIssue"})
public final class JoinCommand implements MusicBotCommand {
    static boolean isMemberConnectedToVoiceChannel(@NotNull SlashCommandInteractionEvent event) {
        GuildVoiceState guildVoiceState = event.getMember().getVoiceState();
        return guildVoiceState.inAudioChannel();
    }

    static void connectToVoiceChannel(@NotNull SlashCommandInteractionEvent event) {
        AudioManager audioManager = event.getGuild().getAudioManager();
        GuildVoiceState guildVoiceState = event.getMember().getVoiceState();

        AudioChannel audioChannel = guildVoiceState.getChannel();
        audioManager.openAudioConnection(audioChannel);
    }

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        try {
            AudioManager audioManager = event.getGuild().getAudioManager();
            GuildVoiceState guildVoiceState = event.getMember().getVoiceState();

            if (isMemberConnectedToVoiceChannel(event)) {
                if (!audioManager.isConnected()) {
                    connectToVoiceChannel(event);

                    String voiceChannelName = event.getMember().getVoiceState().getChannel().getName();
                    event.reply(String.format("Connection to '%s' is successfully established", voiceChannelName)).queue();
                } else {
                    if (audioManager.getConnectedChannel() == guildVoiceState.getChannel()) {
                        event.reply("I'm already in the channel with you").setEphemeral(true).queue();
                    } else {
                        event.reply("I've already been connected to another voice channel").setEphemeral(true).queue();
                    }
                }
            } else {
                event.reply("You're not connected to a voice channel").setEphemeral(true).queue();
            }
        } catch (NullPointerException e) {
            event.reply("I'm sorry. I can't connect").queue();
        }
    }

    @NotNull
    @Override
    public String getName() {
        return "join";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "The bot connects to a voice channel " +
                "you have connected to";
    }

    @Nullable
    @Override
    public Collection<OptionData> getOptions() {
        return null;
    }
}
