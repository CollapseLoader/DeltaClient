package platform;



import aethereal.core.Delta;
import net.fabricmc.api.ClientModInitializer;

public class Initializer implements ClientModInitializer {

    
    public void onInitializeClient() {
        new Delta();
    }
}
