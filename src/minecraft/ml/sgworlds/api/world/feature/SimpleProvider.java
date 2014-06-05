package ml.sgworlds.api.world.feature;


public @interface SimpleProvider {
	String identifier();
	FeatureType type();
	int weight() default 100;
	boolean independent() default false;
}
