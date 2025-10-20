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

import static github.pityamskoy.musicbot.Utility.isMemberConnectedToVoiceChannel;

@SuppressWarnings(value = {"DataFlowIssue"})
public final class JoinCommand implements MusicBotCommand {
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

            if (!isMemberConnectedToVoiceChannel(event)) {
                event.reply("You're not connected to a voice channel").setEphemeral(true).queue();
                return;
            }

            if (!audioManager.isConnected()) {
                connectToVoiceChannel(event);
                // fix join when nobody in the channel
                String voiceChannelName = event.getMember().getVoiceState().getChannel().getName();
                event.reply(String.format("Connection to '%s' is successfully established", voiceChannelName)).queue();
                return;
            }

            if (audioManager.getConnectedChannel() == guildVoiceState.getChannel()) {
                event.reply("I'm already in the channel with you").setEphemeral(true).queue();
            } else {
                event.reply("I've already been connected to another voice channel").setEphemeral(true).queue();
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

    @NotNull
    @Override
    public Optional<Collection<OptionData>> getOptions() {
        return Optional.empty();
    }
}
