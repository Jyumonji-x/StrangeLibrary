package fudan.se.lab2.enums;

/**
 * @author Wanghaiwei
 */
public enum UserPermissionEnum {

    normalUser (0) {
    },
    normalAdministrator (1) {
    },
    superAdministrator (2) {
    };


    UserPermissionEnum (int permission) {
        this.permission = permission;
    }

    private final int permission;


    public int getPermission() {
        return permission;
    }

}