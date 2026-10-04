package ru.vit4liy.perm;

import ru.vit4liy.modular.Module;
import ru.vit4liy.modular.ModuleLoader;
import ru.vit4liy.modular.exception.ModuleInitializeException;
import ru.vit4liy.modular.exception.ModuleShutdownException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

public class PermissionModule extends Module {

    private PermissionService permissionService;
    private final Path basePath;

    public PermissionModule(ModuleLoader loader) {
        this(loader, Paths.get(System.getProperty("user.dir")));
    }

    public PermissionModule(ModuleLoader loader, Path basePath) {
        super(loader);
        this.basePath = basePath;
    }

    @Override
    protected Set<Class<? extends Module>> dependencies() {
        return Set.of();
    }

    @Override
    public void initialize() throws ModuleInitializeException {
        // Передаем путь в сервис
        permissionService = new PermissionServiceImpl(basePath);
    }

    @Override
    public void shutdown() throws ModuleShutdownException {}

    public synchronized PermissionService getPermissionService() {
        return permissionService;
    }

    @Override
    public String moduleName() {
        return "Permission";
    }
}