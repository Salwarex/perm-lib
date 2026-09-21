package ru.vit4liy.perm;

import ru.vit4liy.it72h.lib.config.Config;

import java.util.List;

public interface PermissionService {
    String getRoleDisplayName(Permissible permissible);
    List<String> getRolePermissions(Permissible permissible);
    boolean hasPermission(Permissible permissible, String permission);
    boolean isExists(String role);
    void reload();
    Config getPermissionConfig();
    void checkPermissions(Permissible permissible, String ... permissions) throws MissingPermissionException;
}
