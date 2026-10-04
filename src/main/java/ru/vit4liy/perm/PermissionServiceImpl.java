package ru.vit4liy.perm;

import ru.vit4liy.it72h.lib.config.Config;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

class PermissionServiceImpl implements PermissionService {
    private final Config config;
    private final Map<String, PermissionTreeUnit> groupTrees = new ConcurrentHashMap<>();

    public PermissionServiceImpl() {
        this(Paths.get(System.getProperty("user.dir")));
    }

    public PermissionServiceImpl(Path basePath) {
        Path configFilePath = basePath.resolve("permission/groups.yml");
        this.config = new Config(configFilePath, "permission/groups.yml");
        try{
            this.config.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        reload();
    }

    private void buildAllTrees() {
        Set<String> groups = config.getConfigurationSection("groups").getKeys();
        if (groups == null || groups.isEmpty()) {
            System.out.println("'groups' section in groups.yml is empty.");
            return;
        }

        for (String group : groups) {
            groupTrees.put(group, buildTreeForGroup(group, new HashSet<>()));
        }
    }

    private Set<String> getAllPermissionsRecursive(String groupKey, Set<String> visited) {
        if (visited.contains(groupKey)) return Collections.emptySet();
        visited.add(groupKey);

        Set<String> permissions = new HashSet<>(config.getStringList("groups." + groupKey + ".permissions"));
        List<String> inherits = config.getStringList("groups." + groupKey + ".inherits");
        for (String parentGroup : inherits) {
            permissions.addAll(getAllPermissionsRecursive(parentGroup, visited));
        }
        return permissions;
    }

    private PermissionTreeUnit buildTreeForGroup(String groupKey, Set<String> visited) {
        PermissionTreeUnit tree = new PermissionTreeUnit();
        Set<String> allPermissions = getAllPermissionsRecursive(groupKey, new HashSet<>(visited));
        for (String perm : allPermissions) tree.addPermission(perm);
        return tree;
    }

    @Override
    public String getRoleDisplayName(Permissible permissible) {
        String key = permissible.getGroupKey();
        if(key == null || key.isEmpty()) return "N/a";
        return config.getString("groups." + key + ".name", key);
    }

    @Override
    public List<String> getRolePermissions(Permissible permissible) {
        String key = permissible.getGroupKey();
        if(key == null || key.isEmpty()) return List.of();
        return config.getStringList("groups." + key + ".permissions");
    }

    public boolean hasPermission(Permissible permissible, String permission) {
        if (permission == null || permission.isEmpty()) return false;
        String key = permissible.getGroupKey();
        if (key == null || key.isEmpty()) return false;

        PermissionTreeUnit tree = groupTrees.get(key);
        if (tree == null) {
            tree = groupTrees.get("default");
            if (tree == null) {
                System.err.println("[Tickets] Group 'default' and '" + key + "' is unknown!");
                return false;
            }
        }
        return tree.hasPermission(permission);
    }

    public void checkPermissions(Permissible permissible, String ... permissions) throws MissingPermissionException{
        for(String permission : permissions){
            if(!hasPermission(permissible, permission)) throw new MissingPermissionException(permission);
        }
    }

    @Override
    public boolean isExists(String role) {
        return config.getConfigurationSection("groups." + role) != null;
    }

    public void reload() {
        groupTrees.clear();
        try {
            config.load();
            buildAllTrees();
        } catch (IOException e) {
            throw new RuntimeException("Ошибка перезагрузки конфигурации прав", e);
        }
    }

    @Override
    public Config getPermissionConfig() { return config; }
}