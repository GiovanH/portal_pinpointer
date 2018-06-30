package gio.pinpointer;

import net.minecraft.command.ICommand;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import org.apache.logging.log4j.Logger;

@Mod(modid = PinpointerMod.MODID, name = PinpointerMod.NAME, version = PinpointerMod.VERSION)
public class PinpointerMod
{
    public static final String MODID = "pinpointer";
    public static final String NAME = "Portal Pinpointer Mod";
    public static final String VERSION = "1.0";

    private static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	ICommand pinpointerCommand = new PinpointerCommand();
		net.minecraftforge.client.ClientCommandHandler.instance.registerCommand(pinpointerCommand);
    }
}
