package it.OrderFlow.Model;

import Mock.Model.MockEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestEmployee {

    private final MockEntity me = new MockEntity();

    @Test
    void testAuthenticate() {
        Employee emp1 = new Employee("a@a.aa", "123");
        Employee emp2 = new Employee("a@a.aa", "123");

        assertTrue(emp1.authenticate(emp2));
    }

    @Test
    void testAuthenticateCheckWrongPassword() {
        Employee emp1 = new Employee("a@a.aa", "123");
        Employee emp2 = new Employee("a@a.aa", "wrongPass");

        assertFalse(emp1.authenticate(emp2));
    }

    @Test
    void testHasDefaultPassword() {
        Employee emp = new Employee("a@a.aa", "a@a.aa");

        assertTrue(emp.hasDefaultPassword());
    }

    @Test
    void testHasDefaultPasswordCheckDifferentPassword() {
        Employee emp = new Employee("a@a.aa", "123");

        assertFalse(emp.hasDefaultPassword());
    }

    @Test
    void testChangePassword() {
        Employee emp = new Employee("a@a.aa", "123");

        emp.changePassword("newPass");

        assertEquals("newPass", emp.getPasswordHash());
    }

    @Test
    void testChangeRole() {
        Employee emp = me.getMockRepresentative();

        emp.changeRole(UserRole.DELIVERY_WORKER);

        assertEquals(UserRole.DELIVERY_WORKER, emp.getUserRole());
    }
}