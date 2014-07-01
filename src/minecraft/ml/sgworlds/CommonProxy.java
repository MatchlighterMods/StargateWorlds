package ml.sgworlds;

import ml.core.gui.MLGuiHandler;
import ml.core.gui.core.TopParentGuiElement;
import ml.sgworlds.book.Book;
import ml.sgworlds.book.page.BlankPage;
import ml.sgworlds.book.page.TitlePage;
import ml.sgworlds.window.WindowBook;
import ml.sgworlds.window.WindowTablet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;

public class CommonProxy extends MLGuiHandler {
	
	public enum Windows {
		ANCIENT_TABLET,
		ANCIENT_LANGUAGE,
		PERSONAL_JOURNAL,
	}

	public void load() {

	}

	@Override
	public TopParentGuiElement getTopElement(int ID, EntityPlayer player, World world, int x, int y, int z, Side side) {
		switch (Windows.values()[ID]) {
		case ANCIENT_TABLET:
			return new WindowTablet(player, side, player.getHeldItem());
		case ANCIENT_LANGUAGE:
			return new WindowBook(player, side, new AncientBook());
		case PERSONAL_JOURNAL:
			
		}
		return null;
	}
	
	public static class AncientBook extends Book {
		
		@Override
		public void initContents(WindowBook window) {
			addPage(new BlankPage(this));
			addPage(new TitlePage(this, "The Ancients", EnumChatFormatting.ITALIC.toString() + "Dr. Daniel Jackson"));
			
			// History
			addChapter("History", "The Ancients were an ancient race of people that evolved long before humans did.", 0x3590FF);
			
			// Language
			addChapter("Language", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec a diam lectus. Sed sit amet ipsum mauris. Maecenas congue ligula ac quam viverra nec consectetur ante hendrerit. Donec et mollis dolor. Praesent et diam eget libero egestas mattis sit amet vitae augue. Nam tincidunt congue enim, ut porta lorem lacinia consectetur. Donec ut libero sed arcu vehicula ultricies a non tortor. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean ut gravida lorem. Ut turpis felis, pulvinar a semper sed, adipiscing id dolor. Pellentesque auctor nisi id magna consequat sagittis. Curabitur dapibus enim sit amet elit pharetra tincidunt feugiat nisl imperdiet. Ut convallis libero in urna ultrices accumsan. Donec sed odio eros. Donec viverra mi quis quam pulvinar at malesuada arcu rhoncus. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. In rutrum accumsan ultricies. Mauris vitae nisi at sem facilisis semper ac in est.", 0x35FF90);
			
		}
	};
}
