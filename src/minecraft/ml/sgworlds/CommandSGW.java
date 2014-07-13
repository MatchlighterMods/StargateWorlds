package ml.sgworlds;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import ml.sgworlds.world.SGWorldData;
import ml.sgworlds.world.SGWorldManager;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.ChatMessageComponent;

public class CommandSGW extends CommandBase {

	public static final List<String> cmdAliases = new ArrayList<String>();
	static {
		cmdAliases.add("sgw");
	}
	
	@Override
	public String getCommandName() {
		return "sgworlds";
	}

	@Override
	public List getCommandAliases() {
		return cmdAliases;
	}
	
	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}
	
	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "command.sgw.usage";
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring) {
		List<String> args = new ArrayList<String>(Arrays.asList(astring));
		if (args.size() < 1) throw new WrongUsageException(getCommandUsage(icommandsender));
		
		String cmd = args.remove(0);
		if ("discover".equals(cmd)) {
			if (args.size() < 1) throw new WrongUsageException("command.sgw.discover.usage");
			SGWPlayerData pld = SGWPlayerData.getPlayerData(getCommandSenderAsPlayer(icommandsender).username);
			
			if (isInteger(args.get(0))) {
				if (args.size() != 1) throw new WrongUsageException("command.sgw.discover.usage");
				int wd = parseInt(icommandsender, args.remove(0));
				List<SGWorldData> mixedWorlds = new ArrayList<SGWorldData>(SGWorldManager.instance.worlds);
				Collections.shuffle(mixedWorlds);
				
				int cnt = 0;
				for (SGWorldData sgwd : mixedWorlds) {
					if (cnt >= wd) break;
					if (!pld.discoveredWorlds.contains(sgwd.getDesignation())) {
						pld.discoveredWorlds.add(sgwd.getDesignation());
						cnt++;
					}
				}
				icommandsender.sendChatToPlayer(ChatMessageComponent.createFromTranslationWithSubstitutions("command.sgw.discover.success.count", cnt));
				
			} else if ("all".equals(args.get(0))) {
				for (SGWorldData sgwd : SGWorldManager.instance.worlds) {
					if (!pld.discoveredWorlds.contains(sgwd.getDesignation())) {
						pld.discoveredWorlds.add(sgwd.getDesignation());
					}
				}
				icommandsender.sendChatToPlayer(ChatMessageComponent.createFromTranslationWithSubstitutions("command.sgw.discover.success.all"));
				
			} else if (args.size() > 0) {
				Iterator<String> i = args.iterator();
				while (i.hasNext()) {
					String sgw = i.next();
					if (!pld.discoveredWorlds.contains(sgw) && SGWorldManager.instance.getWorldData(sgw) != null) {
						pld.discoveredWorlds.add(sgw);
					} else i.remove();
				}
				icommandsender.sendChatToPlayer(ChatMessageComponent.createFromTranslationWithSubstitutions("command.sgw.discover.success.list", joinNiceString(args.toArray())));
				
			} else throw new WrongUsageException("command.sgw.discover.usage");
			pld.markDirty();
			
		} else if ("generate".equals(cmd)) {
			if (args.size() == 1 && isInteger(args.get(0))) {
				int cnt = parseInt(icommandsender, args.remove(0));
				if (cnt > 50 || cnt < 1) throw new WrongUsageException("command.sgw.generate.error.minmax");
					
				for (int i=0; i<cnt; i++) {
					SGWorldManager.instance.registerSGWorld(SGWorldData.generateRandom());
				}
				icommandsender.sendChatToPlayer(ChatMessageComponent.createFromTranslationWithSubstitutions("command.sgw.generate.success", cnt));
				
			} else throw new WrongUsageException("command.sgw.generate.usage");
			
//		} else if ("help".equals(cmd)) {
//			if (args.size() == 1 && StatCollector.func_94522_b("command.sgw."+args.get(0)+".info")) {
//				String topic = args.get(0);
//				icommandsender.sendChatToPlayer(ChatMessageComponent.createFromTranslationWithSubstitutions("command.sgw."+topic+".info"));
//				
//			} else throw new WrongUsageException("command.sgw.help.usage");
			
		} else throw new WrongUsageException(getCommandUsage(icommandsender));
	}

	@Override
	public List addTabCompletionOptions(ICommandSender par1iCommandSender, String[] par2ArrayOfStr) {
		// TODO Auto-generated method stub
		return super.addTabCompletionOptions(par1iCommandSender, par2ArrayOfStr);
	}
	
	public boolean isInteger(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (NumberFormatException numberformatexception) {
			return false;
		}
	}
}
