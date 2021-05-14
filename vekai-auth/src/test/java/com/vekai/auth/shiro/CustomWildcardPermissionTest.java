package com.vekai.auth.shiro;


import static com.vekai.auth.AuthConsts.*;

import com.vekai.auth.authorization.PermissionResourceType;
import org.junit.Assert;
import org.junit.Test;

public class CustomWildcardPermissionTest {

    @Test(expected = IllegalArgumentException.class)
    public void testNonRW() {
        CustomWildcardPermission permission = new CustomWildcardPermission("l@aaa");
    }

    @Test
    public void testRW() {
        CustomWildcardPermission permission = new CustomWildcardPermission("@menu:/");


        permission = new CustomWildcardPermission("*@menu:/");
        Assert.assertEquals(ReadWriteType.All, permission.getReadWriteType());

        permission = new CustomWildcardPermission("r,w@menu:/");
        Assert.assertEquals(ReadWriteType.All, permission.getReadWriteType());

        permission = new CustomWildcardPermission("r@menu:/");
        Assert.assertEquals(ReadWriteType.Read, permission.getReadWriteType());

        permission = new CustomWildcardPermission(" r @menu:/");
        Assert.assertEquals(ReadWriteType.Read, permission.getReadWriteType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPermissionTypeWithoutSchemeToken() {
        CustomWildcardPermission permission = new CustomWildcardPermission("r@menu");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNonPermissionType() {
        CustomWildcardPermission permission = new CustomWildcardPermission("r@menuu:/");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPermissionTypeWithoutRootPart() {
        CustomWildcardPermission permission = new CustomWildcardPermission("r@menu:");
    }

    @Test
    public void testPermissionTypeWildcard() {
        CustomWildcardPermission permission = new CustomWildcardPermission("r@menu:*");
    }

    @Test
    public void testPermissionTypeSpecificPart() {
        CustomWildcardPermission permission = new CustomWildcardPermission("r@menu:a");
        Assert.assertEquals(1, permission.getPermissionResource().getParts().size());
        Assert.assertEquals("a", permission.getPermissionResource().getParts().get(0).iterator().next());
    }

    @Test
    public void testPermissionType() {
        CustomWildcardPermission permission = new CustomWildcardPermission("w@menu :/");
        Assert.assertEquals(PermissionResourceType.Menu, permission.getPermissionResource().getPermissionResourceType());

        permission = new CustomWildcardPermission("w@ menu :/");
        Assert.assertEquals(PermissionResourceType.Menu, permission.getPermissionResource().getPermissionResourceType());

    }

    @Test
    public void testParts() {
        CustomWildcardPermission permission = new CustomWildcardPermission("w@menu:/");
        Assert.assertEquals(1, permission.getPermissionResource().getParts().size());
        Assert.assertEquals(WILDCARD_TOKEN, permission.getPermissionResource().getParts().get(0).iterator().next());

        permission = new CustomWildcardPermission("w@menu:/a");
        Assert.assertEquals(1, permission.getPermissionResource().getParts().size());
        Assert.assertEquals("a", permission.getPermissionResource().getParts().get(0).iterator().next());

    }

    @Test
    public void testImply() {
        CustomWildcardPermission permission1 = new CustomWildcardPermission("w@menu:/a/b");
        CustomWildcardPermission permission2 = new CustomWildcardPermission("w@menu:/a/b/c");
        Assert.assertTrue(permission1.implies(permission2));

        permission2 = new CustomWildcardPermission("r@menu:/a/b/c");
        Assert.assertFalse(permission1.implies(permission2));

        permission2 = new CustomWildcardPermission("w@rest:/a/b/c");
        Assert.assertFalse(permission1.implies(permission2));

        permission2 = new CustomWildcardPermission("@menu:/a/b/c");
        Assert.assertFalse(permission1.implies(permission2));

        CustomWildcardPermission permission3 = new CustomWildcardPermission("@menu:a");
        Assert.assertTrue(permission3.implies(permission1));
    }



    @Test
    public void testToString() {

    }
}
