package name.amori.permadeathrevive;

import name.amori.permadeathrevive.network.ClientPacketHandler;
import name.amori.permadeathrevive.network.ModNetwork;
import net.fabricmc.api.ClientModInitializer;

public class PermadeathReviveModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientPacketHandler.register();
    }
}
