# emoji-api
An API used to add emojis to minecraft

![Showcase](https://cdn.discordapp.com/attachments/661446370359640074/707343317263843378/2020-05-05_16.45.20.png)

## Emojis
Emojis in this mod are downloaded from https://github.com/2b2t-Utilities/emojis and can be updated without having to reinstall the mod

## Usage
- Note: Gradle snippets ommit some basic setup
---
First, add the tigr.dev maven repository to gradle:
```
repositories {
    maven {
        name = "tigr.dev"
        url = "https://maven.tigr.dev"
    }
}
```
Next, add the emoji api to your dependencies:
```
dependencies {
    compile "dev.tigr:emojiapi:1.0"
}
```
Then, add the emoji api to your shadow task:
```
shadowJar {
    dependencies {
        include(dependency("dev.tigr:emojiapi"))
    }
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
