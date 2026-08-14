package aethereal.command;


import aethereal.config.BaseProcessor;
import aethereal.lib.log4j.Logger;
import aethereal.lib.log4j.LoggerFactory;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.ParsedCommandNode;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.command.CommandSource;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

public class CommandProcessor extends BaseProcessor {

    @SuppressWarnings("unused")
    private static final Logger logger;

    static {
        logger = LoggerFactory.a(CommandProcessor.class);
    }

    private final List<BaseCommand> commands = new ArrayList<>();
    private final WayCommand wayCommand = new WayCommand();
    private final GPSCommand gpsCommand = new GPSCommand();
    private final LayoutCommand layoutCommand = new LayoutCommand();
    private final RCTCommand rctCommand = new RCTCommand();
    private final BlockESPCommand blockESPCommand = new BlockESPCommand();
    @SuppressWarnings("unused")
    private final String prefix = ".";
    private final CommandDispatcher<CommandSource> dispatcher = new CommandDispatcher<>(new CaseInsensitiveLiteral.a());
    private final ClientCommandSource commandSource = new ClientCommandSource(null, MinecraftClient.getInstance());

    public static <T> RequiredArgumentBuilder<CommandSource, T> a(String name, ArgumentType<T> type) {
        return RequiredArgumentBuilder.argument(name, type);
    }

    @Override

    public void setup() {
        a(this.wayCommand, this.gpsCommand, this.layoutCommand, this.rctCommand, this.blockESPCommand, new AHCommand(), new MacrosCommand(), new FriendCommand(), new StaffCommand(), new WardenCommand(), new ConfigCommand(), new BindCommand(), new VClipCommand(), new HClipCommand(), new CCCommand());
    }

    public CommandDispatcher<CommandSource> a() {
        return this.dispatcher;
    }

    public List<BaseCommand> b() {
        return this.commands;
    }

    public WayCommand c() {
        return this.wayCommand;
    }

    public GPSCommand d() {
        return this.gpsCommand;
    }

    public LayoutCommand e() {
        return this.layoutCommand;
    }

    public RCTCommand f() {
        return this.rctCommand;
    }

    public BlockESPCommand g() {
        return this.blockESPCommand;
    }

    public ClientCommandSource h() {
        return this.commandSource;
    }

    public String i() {
        return ".";
    }

    @Override
    public void unSetup() {
    }

    public void a(BaseCommand... commands) {
        for (BaseCommand command : commands) {
            this.commands.add(command);
            command.a(this.dispatcher);
        }
    }

    public void a(String message, CallbackInfo ci) {
        if (message == null || message.isEmpty() || !message.startsWith(i())) {
            return;
        }
        String command = message.substring(i().length()).trim();
        if (!command.isEmpty()) {
            try {
                ParseResults<CommandSource> results = this.dispatcher.parse(command, this.commandSource);
                for (ParsedCommandNode<CommandSource> parsed : results.getContext().getNodes()) {
                    if (parsed.getNode() instanceof LiteralCommandNode<CommandSource> literal) {
                        int typedLength = parsed.getRange().getLength();
                        if (typedLength != literal.getLiteral().length()) {
                            return;
                        }
                    }
                }
                this.dispatcher.execute(results);
                ci.cancel();
            } catch (CommandSyntaxException e) {
                System.out.println("Failure command: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Command error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
