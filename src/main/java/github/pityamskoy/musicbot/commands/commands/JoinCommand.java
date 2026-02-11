package github.pityamskoy.musicbot.commands.commands;

import github.pityamskoy.musicbot.commands.MusicBotCommand;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;

import static github.pityamskoy.musicbot.Utility.isPossibleToExecuteCommandAndReplyIfFalse;

@SuppressWarnings(value = {"DataFlowIssue"})
public final class JoinCommand implements MusicBotCommand {
    static void connectToVoiceChannel(SlashCommandInteractionEvent event) {
        AudioManager audioManager = event.getGuild().getAudioManager();
        GuildVoiceState guildVoiceState = event.getMember().getVoiceState();

        AudioChannel audioChannel = guildVoiceState.getChannel();
        audioManager.openAudioConnection(audioChannel);
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        try {
            AudioManager audioManager = event.getGuild().getAudioManager();
            GuildVoiceState memberVoiceState = event.getMember().getVoiceState();

            if (isPossibleToExecuteCommandAndReplyIfFalse(event)) {
                if (audioManager.getConnectedChannel() == memberVoiceState.getChannel()) {
                    event.reply("I'm already in the channel with you").setEphemeral(true).queue();
                    return;
                }

                if (!audioManager.isConnected()) {
                    connectToVoiceChannel(event);
                    // fix join when nobody in the channel
                    String voiceChannelName = event.getMember().getVoiceState().getChannel().getName();
                    event.reply(String.format("Connection to '%s' is successfully established", voiceChannelName)).queue();
                }
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
        return "The bot connects to a voice channel you have connected to";
    }

    @NotNull
    @Override
    public Optional<Collection<OptionData>> getOptions() {
        return Optional.empty();
    }
}
