package enumsTest;

import fudan.se.lab2.enums.UserPermissionEnum;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserPreminssionEnumTest {

    @Test
    public void simpleTest(){
        System.out.println(UserPermissionEnum.normalUser.getPermission());
        assertEquals(UserPermissionEnum.normalUser.getPermission(),0);
        System.out.println(UserPermissionEnum.normalAdministrator.getPermission());
        assertEquals(UserPermissionEnum.normalAdministrator.getPermission(),1);
        System.out.println(UserPermissionEnum.superAdministrator.getPermission());
        assertEquals(UserPermissionEnum.superAdministrator.getPermission(),2);
    }
}
