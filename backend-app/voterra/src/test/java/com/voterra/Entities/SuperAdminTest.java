package com.voterra.Entities;

import com.voterra.entities.SuperAdmin;
import com.voterra.entities.User;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SuperAdminTest {

    @Test
    void testMakeAdmin() {
        // Arrange
        List<String> admins = new ArrayList<>();
        SuperAdmin superAdmin = new SuperAdmin("admin@example.com", "password", "admin", "Admin",
                "User", new Date(), new ArrayList<>(), new ArrayList<>(), User.userType.SUPERADMIN, User.gender.NOT_SPECIFIED, new ArrayList<>());
        superAdmin.setListOfAdmins(admins);

        // Act
        superAdmin.makeAdmin("newAdmin");

        // Assert
        assertTrue(superAdmin.getListOfAdmins().contains("newAdmin"));
    }

    @Test
    void testRemoveAdmin() {
        // Arrange
        List<String> admins = new ArrayList<>();
        admins.add("adminToRemove");
        SuperAdmin superAdmin = new SuperAdmin("admin@example.com", "password", "admin", "Admin",
                "User", new Date(), new ArrayList<>(), new ArrayList<>(), User.userType.SUPERADMIN, User.gender.NOT_SPECIFIED, new ArrayList<>() );
        superAdmin.setListOfAdmins(admins);

        // Act
        superAdmin.removeAdmin("adminToRemove");

        // Assert
        assertEquals(0, superAdmin.getListOfAdmins().size());
    }
}