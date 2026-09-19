package ru.vit4liy.perm;

import ru.vit4liy.modular.Module;
import ru.vit4liy.modular.exception.ModuleInitializeException;
import ru.vit4liy.modular.exception.ModuleShutdownException;

public class PermissionModule implements Module {

    private PermissionService permissionService;

    @Override
    public void initialize() throws ModuleInitializeException {
        permissionService = new PermissionServiceImpl();
    }

    @Override
    public void shutdown() throws ModuleShutdownException {}

    public PermissionService getPermissionService() {
        return permissionService;
    }

    @Override
    public String moduleName() {
        return "Permission";
    }
}
