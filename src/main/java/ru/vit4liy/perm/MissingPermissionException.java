package ru.vit4liy.perm;

public class MissingPermissionException extends Exception {
    public MissingPermissionException(String perm) {
        super("This action requires the following permission: " + perm);
    }
}
