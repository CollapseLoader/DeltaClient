package platform;


import aethereal.api.Compile;
import aethereal.core.Delta;
import net.fabricmc.api.ClientModInitializer;

public class Initializer implements ClientModInitializer {

    @Compile
    public void onInitializeClient() {
        new Delta();
    }
}
