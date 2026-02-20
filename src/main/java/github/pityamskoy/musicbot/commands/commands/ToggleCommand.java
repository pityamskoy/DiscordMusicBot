package github.pityamskoy.musicbot.commands.commands;

import github.pityamskoy.musicbot.commands.MusicBotCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static github.pityamskoy.musicbot.events.GuildListener.getIsOnUserUpdateOnlineStatusWork;
import static github.pityamskoy.musicbot.events.GuildListener.setIsOnUserUpdateOnlineStatusWork;

@SuppressWarnings(value = {"DataFlowIssue"})
public final class ToggleCommand implements MusicBotCommand {
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if (event.getOption("function").getAsString().equals("online")) {
            if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                event.reply("You don't have permission to use this command!").setEphemeral(true).queue();
                return;
            }

            setIsOnUserUpdateOnlineStatusWork(getIsOnUserUpdateOnlineStatusWork());
        }
    }

    @NotNull
    @Override
    public String getName() {
        return "toggle";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "toggles different permissions of the bot";
    }

    @Override
    public Optional<Collection<OptionData>> getOptions() {
        OptionData loopType = new OptionData(OptionType.STRING, "function",
                "toggles a function of the bot", true)
                .addChoice("online", "online");

        return Optional.of(List.of(loopType));
    }
}
