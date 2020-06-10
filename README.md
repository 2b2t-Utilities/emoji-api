# emoji-api
An API used to add emojis to minecraft

![Showcase](https://cdn.discordapp.com/attachments/661446370359640074/707343317263843378/2020-05-05_16.45.20.png)

## Emojis
Emojis in this mod are downloaded from https://github.com/2b2t-Utilities/emojis and can be updated without having to reinstall the mod

## Usage
First, download the emoji-api-1.0.jar from [here](https://github.com/2b2t-Utilities/emoji-api/releases/tag/v1.0).
Next, move the jar to a libraries folder in your projects dir. For example: _project_dir_/libs/emoji-api-1.0.jar.
Then, add this to your Gradle:
```
dependencies {
    runtime files("libs/emoji-api-1.0.jar")
}
```
Last, add the emoji-api to your mixin configs:
```
public class MixinLoader implements IFMLLoadingPlugin {
	public MixinLoader() {
		MixinBootstrap.init();
		// mixins.ares.json is the config of the mod implementing the emoji api
		Mixins.addConfigurations("mixins.ares.json", "mixins.emojiapi.json");
		MixinEnvironment.getDefaultEnvironment().setObfuscationContext("searge");
	}

	@Override
	public String[] getASMTransformerClass() {
		return new String[0];
	}

	@Override
	public String getModContainerClass() {
		return null;
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}
}
```
