package github.pityamskoy.musicbot.commands.commands;

import com.sedmelluq.discord.lavaplayer.player.*;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import github.pityamskoy.musicbot.commands.MusicBotCommand;
import github.pityamskoy.musicbot.commands.lavaplayer.AudioLoadResultHandlerImpl;
import github.pityamskoy.musicbot.commands.lavaplayer.AudioPlayerSendHandler;
import github.pityamskoy.musicbot.commands.lavaplayer.TrackScheduler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;

import static github.pityamskoy.musicbot.commands.commands.JoinCommand.connectToVoiceChannel;
import static github.pityamskoy.musicbot.commands.commands.JoinCommand.isMemberConnectedToVoiceChannel;

@SuppressWarnings(value = {"ArraysAsListWithZeroOrOneArgument", "DataFlowIssue"})
public final class PlayCommand implements MusicBotCommand {
    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        try {
            Guild guild = event.getGuild();
            AudioManager audioManager = guild.getAudioManager();
            GuildVoiceState guildVoiceState = event.getMember().getVoiceState();

            if (isMemberConnectedToVoiceChannel(event)) {
                if (audioManager.isConnected()) {
                    if (audioManager.getConnectedChannel() != guildVoiceState.getChannel()) {
                        event.reply("You should be in the same channel with me to call this command").setEphemeral(true).queue();
                        return;
                    }
                } else {
                    connectToVoiceChannel(event);
                }
                Message.Attachment file = event.getOption("file").getAsAttachment();

                AudioPlayerManager audioPlayerManager = new DefaultAudioPlayerManager();
                AudioSourceManagers.registerRemoteSources(audioPlayerManager);

                AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
                AudioPlayer audioPlayer = playerManager.createPlayer();
                AudioPlayerSendHandler audioPlayerSendHandler = new AudioPlayerSendHandler(audioPlayer);
                guild.getAudioManager().setSendingHandler(audioPlayerSendHandler);

                TrackScheduler trackScheduler = new TrackScheduler(audioPlayer);
                audioPlayer.addListener(trackScheduler);

                AudioLoadResultHandlerImpl audioLoadResultHandlerImpl = new AudioLoadResultHandlerImpl(audioPlayer);
                audioPlayerManager.loadItemOrdered(guild, file.getUrl(), audioLoadResultHandlerImpl);

                event.reply(MessageFormat.format("Playing {0}", file.getFileName())).setEphemeral(true).queue();
            } else {
                event.reply("You should be connected to a voice channel").setEphemeral(true).queue();
            }
        } catch (NullPointerException e) {
            event.reply("I'm sorry. A error has been occurred").setEphemeral(true).queue();
        }
    }

    @NotNull
    @Override
    public String getName() {
        return "play";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Plays music files";
    }

    @NotNull
    @Override
    public Collection<OptionData> getOptions() {
        OptionData file = new OptionData(OptionType.ATTACHMENT, "file",
                "Support only .mp3 / .wav files", true);
        return Arrays.asList(file);
    }
}