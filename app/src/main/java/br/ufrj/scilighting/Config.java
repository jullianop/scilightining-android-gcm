package br.ufrj.scilighting;

public class Config {

	static final String SERVER_URL = "http://scilightning.dyndns-ip.com:8080/SciLightining-server";
	public static final String PREFS_NAME = "MyPrefsFile";

    @SuppressWarnings("unchecked")
    public static String makeLogTag(Class cls) {
        String tag = "SciLightning_" + cls.getSimpleName();
        return (tag.length() > 23) ? tag.substring(0, 23) : tag;
    }
	
}
