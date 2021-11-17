package net.hat.gt;


import com.jab125.util.datagen.DataGeneraton;
import com.jab125.util.tradehelper.TradeManager;
import com.jab125.util.tradehelper.type.BasicTrade;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.hat.gt.config.GoblinTradersConfig;
import net.hat.gt.init.*;
import net.hat.gt.spawning.SpawnHandler;
import net.minecraft.data.DataGenerator;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Objects;

public class GobT implements ModInitializer {

    public static final String MODID = "goblintraders";
    // Mental Note: don't remove this
    private static final boolean doDataGen = false;
    public static GoblinTradersConfig config;
    public static final Logger LOGGER = LogManager.getLogger("goblintraders");

    @Override
    public void onInitialize() {
        AutoConfig.register(GoblinTradersConfig.class, JanksonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(GoblinTradersConfig.class).getConfig();

        TradeManager manager = TradeManager.instance();
        manager.registerTrader(ModEntities.GOBLIN_TRADER);
        manager.registerTrader(ModEntities.VEIN_GOBLIN_TRADER);
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(manager);

        ServerWorldEvents.LOAD.register(new SpawnHandler.WorldLoad());
        ServerLifecycleEvents.SERVER_STOPPED.register(new SpawnHandler.ServerStopped());
        ServerTickEvents.END_WORLD_TICK.register(new SpawnHandler.OnWorldTick());
        manager.registerTypeSerializer(BasicTrade.SERIALIZER);
        ModGameRules.registerGameRules();
        ModSounds.registerSounds();
        ModEntities.registerEntities();
        ModItems.registerItems();
        ModStats.registerStats();
        ModPotions.registerPotions();
        ModPotions.registerPotionRecipes();
        //ModSpawns.init();
        // Mental Note: don't remove this
        if (doDataGen) {
            DataGenerator dataGenerator = new DataGenerator(new File("../src/main/generated/resources").toPath(), null);
            DataGeneraton.registerCommonProviders(dataGenerator);
        }
        boolean a;
            a = ResourceManagerHelper.registerBuiltinResourcePack(id("gobtvanillaish"), Objects.requireNonNull(FabricLoader.getInstance().getModContainer(MODID)).get(), ResourcePackActivationType.NORMAL);
    }
    public static Identifier id(String path) {
        return new Identifier(MODID, path);
    }
}
