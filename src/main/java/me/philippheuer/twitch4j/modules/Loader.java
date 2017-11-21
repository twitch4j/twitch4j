package me.philippheuer.twitch4j.modules;

import com.jcabi.log.Logger;
import lombok.Getter;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.events.event.system.modules.ModuleDisabledEvent;
import me.philippheuer.twitch4j.events.event.system.modules.ModuleEnabledEvent;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.stream.Collectors;

public class Loader {

	@Getter
	protected static final List<Class<? extends IModule>> modules = new CopyOnWriteArrayList<>();
	public static final String MODULE_DIR = "modules";

	@Getter
	private List<IModule> loadedModules = new CopyOnWriteArrayList<>();
	private TwitchClient client;

	static {
		if (Config.LOAD_EXTERNAL_MODULES) {
			File modulesDir = new File(MODULE_DIR);
			if (modulesDir.exists()) {
				if (!modulesDir.isDirectory()) {
					throw new RuntimeException(MODULE_DIR + " isn't a directory!");
				}
			} else {
				if (!modulesDir.mkdir()) {
					throw new RuntimeException("Error creating " + MODULE_DIR + " directory");
				}
			}

			File[] files = modulesDir.listFiles((FilenameFilter) FileFilterUtils.suffixFileFilter("jar"));
			if (files != null && files.length > 0) {
				Logger.info(Loader.class, "Attempting to load %d external module(s)...", files.length);
				loadExternalModules(Arrays.asList(files));
			}
		}
	}

	public Loader(TwitchClient client) {
		this.client = client;

		for (Class<? extends IModule> clazz : modules) {
			try {
				IModule module = clazz.newInstance();
				Logger.info(this, "Loading module %s v%s by %s", module.getName(), module.getVersion(), module.getAuthor());
				loadedModules.add(module);
			} catch (InstantiationException | IllegalAccessException e) {
				Logger.error(this, "Unable to load module %s!", clazz.getName(), ExceptionUtils.getStackTrace(e));
			}
		}

		if (Config.AUTOMATICALLY_ENABLE_MODULES) { // Handles module load order and loads the modules
			List<IModule> toLoad = new CopyOnWriteArrayList<>(loadedModules);
			while (toLoad.size() > 0) {
				for (IModule module : toLoad) {
					if (loadModule(module))
						toLoad.remove(module);
				}
			}
		}
	}

	public boolean loadModule(IModule module) {
		if (loadedModules.contains(module)) {
			return false;
		}
		Class<? extends IModule> clazz = module.getClass();
		if (clazz.isAnnotationPresent(Require.class)) {
			Require annotation = clazz.getAnnotation(Require.class);
			if (!hasDependency(loadedModules, annotation.value())) {
				return false;
			}
		}
		boolean enabled = module.enable(client);
		if (enabled) {
			client.getDispatcher().registerListener(module);
			if (!loadedModules.contains(module))
				loadedModules.add(module);

			client.getDispatcher().dispatch(new ModuleEnabledEvent(module));
		}

		return true;
	}

	public void unloadModule(IModule module) {
		loadedModules.remove(module);
		module.disable();
		client.getDispatcher().unregisterListener(module);

		loadedModules.removeIf(mod -> {
			Class<? extends IModule> clazz = module.getClass();
			if (clazz.isAnnotationPresent(Require.class)) {
				Require annotation = clazz.getAnnotation(Require.class);
				if (annotation.value().equals(module.getClass().getName())) {
					unloadModule(mod);
					return true;
				}
			}
			return false;
		});

		client.getDispatcher().dispatch(new ModuleDisabledEvent(module));
	}

	private boolean hasDependency(List<IModule> modules, String className) {
		for (IModule module : modules)
			if (module.getClass().getName().equals(className))
				return true;
		return false;
	}

	@SuppressWarnings("unchecked")
	public static synchronized void loadExternalModules(File file) { // A bit hacky, but oracle be dumb and encapsulates URLClassLoader#addUrl()
		if (file.isFile() && file.getName().endsWith(".jar")) { // Can't be a directory and must be a jar
			try (JarFile jar = new JarFile(file)) {
				Manifest man = jar.getManifest();
				String moduleAttrib = man.getMainAttributes().getValue("Twitch4J-ModuleClass");
				String[] moduleClasses = new String[0];
				if (moduleAttrib != null) {
					moduleClasses = moduleAttrib.split(";");
				}
				// Executes would should be URLCLassLoader.addUrl(file.toURI().toURL());
				URLClassLoader loader = (URLClassLoader) ClassLoader.getSystemClassLoader();
				URL url = file.toURI().toURL();
				for (URL it : Arrays.asList(loader.getURLs())) { // Ensures duplicate libraries aren't loaded
					if (it.equals(url)) {
						return;
					}
				}
				Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
				method.setAccessible(true);
				method.invoke(loader, url);
				if (moduleClasses.length == 0) { // If the Module Developer has not specified the Implementing Class, revert to recursive search
					// Scans the jar file for classes which have IModule as a super class
					List<String> classes = new ArrayList<>();
					jar.stream().filter(jarEntry -> !jarEntry.isDirectory() && jarEntry.getName().endsWith(".class")).map(path -> path.getName().replace('/', '.').substring(0, path.getName().length() - ".class".length())).forEach(classes::add);
					for (String clazz : classes) {
						try {
							Class classInstance = loadClass(clazz);
							if (IModule.class.isAssignableFrom(classInstance) && !classInstance.equals(IModule.class)) {
								addModuleClass(classInstance);
							}
						} catch (NoClassDefFoundError ignored) { /* This can happen. Looking recursively looking through the classpath is hackish... */ }
					}
				} else {
					for (String moduleClass : moduleClasses) {
						Logger.info(Loader.class, "Loading Class from Manifest Attribute: %s", moduleClass);
						Class classInstance = loadClass(moduleClass);
						if (IModule.class.isAssignableFrom(classInstance))
							addModuleClass(classInstance);
					}
				}
			} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | IOException | ClassNotFoundException e) {
				Logger.error(Loader.class, "Unable to load module %s!", file.getName(), ExceptionUtils.getStackTrace(e));
			}
		}
	}

	private static Class loadClass(String clazz) throws ClassNotFoundException {
		if (clazz.contains("$") && clazz.substring(0, clazz.lastIndexOf("$")).length() > 0) {
			try {
				loadClass(clazz.substring(0, clazz.lastIndexOf("$")));
			} catch (ClassNotFoundException ignored) {
			} // If the parent class doesn't exist then it is safe to instantiate the child
		}
		return Class.forName(clazz);
	}

	public static void loadExternalModules(List<File> files) {
		List<File> independents = new ArrayList<>();
		List<File> dependents = new ArrayList<>();

		files.forEach((file) -> {
			try {
				if (getModuleRequires(file).length > 0) {
					dependents.add(file);
				} else {
					independents.add(file);
				}
			} catch (IOException e) {
				Logger.error(Loader.class, "Twitch4J Internal Exception");
			}
		});

		independents.forEach(Loader::loadExternalModules);

		List<File> noLongerDependents = dependents.stream().filter(jarFile -> { // loads all dependents whose requirements have been met already
			try {
				String[] moduleRequires = getModuleRequires(jarFile);
				List<Class> classes = new ArrayList<>();
				for (String clazz : moduleRequires) {
					classes.add(Class.forName(clazz));
				}
				return classes.size() == moduleRequires.length;
			} catch (Exception e) {
				return false;
			}
		}).collect(Collectors.toList());
		dependents.removeAll(noLongerDependents);
		noLongerDependents.forEach(Loader::loadExternalModules);

		final int retryAttempts = dependents.size();
		for (int i = 0; i < retryAttempts; i++) {
			dependents.removeIf((file -> { // Filters out all usable files
				boolean loaded = false;
				try {
					String[] required = getModuleRequires(file);
					for (String clazz : required) {
						try {
							Class.forName(clazz);
							loaded = true;
						} catch (ClassNotFoundException ignored) {}

						if (!loaded)
							loaded = findFileForClass(files, clazz) != null;

						if (!loaded)
							break;
					}
				} catch (IOException ignored) {}

				if (loaded)
					loadExternalModules(file);

				return loaded;
			}));

			if (dependents.isEmpty())
				break;
		}

		if (dependents.size() > 0)
			Logger.warn(Loader.class, "Unable to load %d modules!", dependents.size());
	}

	private static String[] getModuleRequires(File file) throws IOException {
		JarFile jarFile = new JarFile(file);
		Manifest manifest = jarFile.getManifest();
		Attributes.Name moduleRequiresLower = new Attributes.Name("module-requires"); //TODO remove
		Attributes.Name moduleRequiresUpper = new Attributes.Name("Module-Requires");
		if (manifest != null && manifest.getMainAttributes() != null //TODO remove
				&& manifest.getMainAttributes().containsKey(moduleRequiresLower)) {
			String value = manifest.getMainAttributes().getValue(moduleRequiresLower);
			Logger.warn(Loader.class, "File %s uses the 'module-requires' attribute instead of 'Module-Requires', please rename the attribute!", file.getName());
			return value.contains(";") ? value.split(";") : new String[]{value};
		} else if (manifest != null && manifest.getMainAttributes() != null
				&& manifest.getMainAttributes().containsKey(moduleRequiresUpper)) {
			String value = manifest.getMainAttributes().getValue(moduleRequiresUpper);
			return value.contains(";") ? value.split(";") : new String[]{value};
		} else {
			return new String[0];
		}
	}

	private static File findFileForClass(List<File> files, String clazz) {
		return files.stream().filter((file) -> {
			try {
				JarFile jarFile = new JarFile(file);
				return jarFile.getJarEntry(clazz.replaceAll("\\.", File.pathSeparator) + ".class") != null;
			} catch (IOException e) {
				return false;
			}
		}).findFirst().orElse(null);
	}

	public static void addModuleClass(Class<? extends IModule> clazz) {
		if (!Modifier.isAbstract(clazz.getModifiers())
				&& !Modifier.isInterface(clazz.getModifiers())
				&& !modules.contains(clazz)) {
			modules.add(clazz);
		}
	}
}
