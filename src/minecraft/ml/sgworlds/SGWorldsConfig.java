package ml.sgworlds;

import net.minecraftforge.common.Configuration;
import ml.core.data.Config;

public class SGWorldsConfig extends Config {

	public SGWorldsConfig(Configuration cfg) {
		super(cfg);
	}

	public @Prop int worldProviderId = 151398428;
	
	public @Prop int numberWorldsToGenerate = 100;
	public @Prop int numberWorldsToGenerateRandom = 50;
	
	public @Prop(comment="Set to true to recover worlds after removing an addon mod.") boolean ignoreMissingFeature = false;
}
