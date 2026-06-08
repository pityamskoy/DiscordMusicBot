package pityamskoy.sam.interaction.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static pityamskoy.sam.interaction.listeners.GuildListener.getIsOnUserUpdateOnlineStatusWork;
import static pityamskoy.sam.interaction.listeners.GuildListener.setIsOnUserUpdateOnlineStatusWork;

@SuppressWarnings(value = {"DataFlowIssue"})
public final class ToggleCommand implements MusicBotCommand {
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if (event.getOption("function").getAsString().equals("online")) {
            if (!event.getMember().hasPermission(Permission.MANAGE_SERVER)) {
                event.reply("You don't have permission to use this command!").setEphemeral(true).queue();
                return;
            }

            setIsOnUserUpdateOnlineStatusWork(!getIsOnUserUpdateOnlineStatusWork());

            if (getIsOnUserUpdateOnlineStatusWork()) {
                event.reply("Notifications of member's becoming online is set to on").queue();
            } else {
                event.reply("Notifications of member's becoming online is set to off").queue();
            }
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
