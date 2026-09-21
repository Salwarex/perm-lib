package ru.vit4liy.perm;

import ru.vit4liy.modular.Module;
import ru.vit4liy.modular.ModuleLoader;
import ru.vit4liy.modular.exception.ModuleInitializeException;
import ru.vit4liy.modular.exception.ModuleShutdownException;

import java.util.Set;

public class PermissionModule extends Module {

    private PermissionService permissionService;

    public PermissionModule(ModuleLoader loader) {
        super(loader);
    }

    @Override
    protected Set<Class<? extends Module>> dependencies() {
        return Set.of();
    }

    @Override
    public void initialize() throws ModuleInitializeException {
        permissionService = new PermissionServiceImpl();
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
