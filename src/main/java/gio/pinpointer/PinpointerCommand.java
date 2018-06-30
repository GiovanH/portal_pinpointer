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
		int dimension = sender.getCommandSenderEntity().dimension;
		if (world.isRemote) { //We should only worry about client side
			BlockPos overworldCoordinates, netherCoordinates;
			if (dimension == OVERWORLD_DIMENSION_ID) {
				overworldCoordinates = parseCommandInput(sender, args);
				if (overworldCoordinates == null) return;
				netherCoordinates = new BlockPos(
					overworldCoordinates.getX()/8,
					overworldCoordinates.getY()/8,
					overworldCoordinates.getZ()/8
				);
			} else if (dimension == NETHER_DIMENSION_ID) {
				netherCoordinates = parseCommandInput(sender, args);
				if (netherCoordinates == null) return;
				overworldCoordinates = new BlockPos(
					netherCoordinates.getX()*8 + 4,
					netherCoordinates.getY(), //although this doesn't matter
					netherCoordinates.getZ()*8 + 4
				);
			} else {
				sender.sendMessage(new TextComponentString("Dimension ID " + dimension + "Coordinates: " + stringifyVector(sender.getPosition())));
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
	
	private BlockPos parseCommandInput(ICommandSender sender, String[] args) {
		BlockPos position = sender.getPosition();
		BlockPos coords;
		switch (args.length) {
			case 0: // Usage: /portal
				coords = position;
				break;
			case 1: //  Usage: /portal help
				sender.sendMessage(new TextComponentString(getUsage(sender)));
				return null;
			case 2: //  Usage: /portal X Z
				coords = new BlockPos(
					Integer.parseInt(args[0]),
					0,
					Integer.parseInt(args[1])
				);
				break;
			case 3: //  Usage: /portal X Y Z
				coords = new BlockPos(
					Integer.parseInt(args[0]),
					0,
					Integer.parseInt(args[2])
				);
				break;
			default:
				System.err.println("Unknown arg length " + args.length);
				return null;
		}
		return coords;
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
		StringBuilder n = new StringBuilder();
		n.append("Pinpointer");
		n.append("\nAliases: ");
		for (String alias : aliases) {
			n.append(alias + ", ");
		}
		n.append("\nUsage:");
		n.append("\n/" + aliases.get(0) + "");
		n.append("\n/" + aliases.get(0) + " help");
		n.append("\n/" + aliases.get(0) + " [X] [Z]");
		n.append("\n/" + aliases.get(0) + " [X] [Y] [Z]");
		return n.toString();
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
