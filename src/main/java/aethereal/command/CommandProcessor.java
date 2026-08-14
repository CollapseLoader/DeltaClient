package aethereal.command;


import aethereal.config.BaseProcessor;
import aethereal.lib.log4j.LoggerFactory;
import aethereal.lib.log4j.Logger_2;
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
import java.util.Objects;

public class CommandProcessor extends BaseProcessor {

    private static final Logger_2 b;

    static {
        b = LoggerFactory.a(CommandProcessor.class);
    }

    private final List<BaseCommand> d = new ArrayList<>();
    private final WayCommand e = new WayCommand();
    private final GPSCommand f = new GPSCommand();
    private final LayoutCommand g = new LayoutCommand();
    private final RCTCommand h = new RCTCommand();
    private final BlockESPCommand i = new BlockESPCommand();
    private final String k = ".";
    private final CommandDispatcher<CommandSource> c = new CommandDispatcher<>(new CaseInsensitiveLiteral.a());
    private final ClientCommandSource j = new ClientCommandSource(null, MinecraftClient.getInstance());

    public static <T> RequiredArgumentBuilder<CommandSource, T> a(String name, ArgumentType<T> type) {
        return RequiredArgumentBuilder.argument(name, type);
    }

    @Override
    
    public void setup() {
        a(this.e, this.f, this.g, this.h, this.i, new AHCommand(), new MacrosCommand(), new FriendCommand(), new StaffCommand(), new WardenCommand(), new ConfigCommand(), new BindCommand(), new VClipCommand(), new HClipCommand(), new CCCommand());
    }

    public CommandDispatcher<CommandSource> a() {
        return this.c;
    }

    public List<BaseCommand> b() {
        return this.d;
    }

    public WayCommand c() {
        return this.e;
    }

    public GPSCommand d() {
        return this.f;
    }

    public LayoutCommand e() {
        return this.g;
    }

    public RCTCommand f() {
        return this.h;
    }

    public BlockESPCommand g() {
        return this.i;
    }

    public ClientCommandSource h() {
        return this.j;
    }

    public String i() {
        return ".";
    }

    @Override
    public void unSetup() {
    }

    public void a(BaseCommand... commands) {
        for (BaseCommand command : commands) {
            this.d.add(command);
            command.a(this.c);
        }
    }

    public void a(String message, CallbackInfo ci) {
        if (message == null || message.isEmpty() || !message.startsWith(i())) {
            return;
        }
        String command = message.substring(i().length()).trim();
        if (!command.isEmpty()) {
            try {
                ParseResults<CommandSource> results = this.c.parse(command, this.j);
                for (ParsedCommandNode<CommandSource> parsed : results.getContext().getNodes()) {
                    if (parsed.getNode() instanceof LiteralCommandNode) {
                        LiteralCommandNode<CommandSource> literal = (LiteralCommandNode<CommandSource>) parsed.getNode();
                        int typedLength = parsed.getRange().getLength();
                        if (typedLength != literal.getLiteral().length()) {
                            return;
                        }
                    }
                }
                this.c.execute(results);
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
