package gio.pinpointer;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class PinpointerCommand implements ICommand {
	
	private final List<String> aliases;
	protected String CMDNAME;
	
	private static final int OVERWORLD_DIMENSION_ID = 0;
	private static final int NETHER_DIMENSION_ID = -1;
	
	
	public PinpointerCommand() {
		CMDNAME = "pinpointer";
		aliases = new ArrayList<String>();
		
		aliases.add(CMDNAME);
		aliases.add("portals");
		aliases.add("portal");
		aliases.add("p");
		aliases.add("pinpoint");
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		World world = sender.getEntityWorld();
		BlockPos position = sender.getPosition();
		int dimension = sender.getCommandSenderEntity().dimension;
		if (world.isRemote) {
			BlockPos overworldCoordinates, netherCoordinates;
			if (dimension == OVERWORLD_DIMENSION_ID) {
				overworldCoordinates = position;
				netherCoordinates = new BlockPos(
					overworldCoordinates.getX()/8,
					overworldCoordinates.getY()/8,
					overworldCoordinates.getZ()/8
				);
			} else if (dimension == NETHER_DIMENSION_ID) {
				netherCoordinates = position;
				overworldCoordinates = new BlockPos(
					netherCoordinates.getX()*8 + 4,
					netherCoordinates.getY(), //although this doesn't matter
					netherCoordinates.getZ()*8 + 4
				);
			} else {
				sender.sendMessage(new TextComponentString("Dimension ID " + dimension + "Coordinates: " + stringifyVector(position)));
				return;
			}
			StringBuilder n = new StringBuilder();
			n.append("Overworld Coordinates: ");
			n.append(stringifyVector(overworldCoordinates));
			n.append("\nNether Coordinates: ");
			n.append(stringifyVector(netherCoordinates));
			sender.sendMessage(new TextComponentString(n.toString()));
		} else {
			System.out.println("Not processing client command on Server side");
		}		
	}

	private String stringifyVector(BlockPos position) {
		// TODO Auto-generated method stub
		return "X: " + position.getX() + ", " + "Z: " + position.getZ(); 
	}

	@Override
	public int compareTo(ICommand arg0) {
		return 0;
	}

	@Override
	public String getName() {
		return CMDNAME;
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/" + CMDNAME;
	}

	@Override
	public List<String> getAliases() {
		return this.aliases;
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return true;
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos targetPos) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		// TODO Auto-generated method stub
		return false;
	}
	

}
