package ru.vit4liy.perm;

import java.util.HashMap;
import java.util.Map;

class PermissionTreeUnit {
    private final Map<String, PermissionTreeUnit> children = new HashMap<>();
    private boolean hasPermission = false;
    private boolean hasWildcard = false;

    public void addPermission(String permission) {
        if (permission == null || permission.isEmpty()) return;

        if (permission.equals("*")) {
            this.hasWildcard = true;
            return;
        }

        String[] parts = permission.split("\\.");
        PermissionTreeUnit current = this;

        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];

            if (part.equals("*")) {
                current.hasWildcard = true;
                return;
            }

            current = current.children.computeIfAbsent(part, k -> new PermissionTreeUnit());
        }

        current.hasPermission = true;
    }

    public boolean hasPermission(String permission) {
        if (permission == null || permission.isEmpty()) return false;

        String[] parts = permission.split("\\.");
        PermissionTreeUnit current = this;

        for (String part : parts) {
            if (current.hasWildcard) {
                return true;
            }

            if (!current.children.containsKey(part)) {
                return false;
            }

            current = current.children.get(part);
        }

        return current.hasPermission || current.hasWildcard;
    }
}