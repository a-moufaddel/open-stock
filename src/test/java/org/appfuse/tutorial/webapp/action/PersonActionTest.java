package org.appfuse.tutorial.webapp.action;

import com.opensymphony.xwork2.ActionSupport;

import org.apache.struts2.ServletActionContext;
import org.appfuse.tutorial.model.Person;
import org.appfuse.tutorial.service.GenericManager;
import org.appfuse.tutorial.webapp.action.BaseActionTestCase;
import org.appfuse.tutorial.webapp.action.PersonAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PersonActionTest extends BaseActionTestCase {
    private PersonAction action;

    @Autowired
    private GenericManager personManager;

    @Before
    public void onSetUp() {
        super.onSetUp();

        action = new PersonAction();
        action.setPersonManager(personManager);

        // add a test person to the database
        Person person = new Person();

        // enter all required fields

        personManager.save(person);
    }

    @Test
    public void testGetAllPersons() throws Exception {
        assertEquals(action.list(), ActionSupport.SUCCESS);
        assertTrue(action.getPersons().size() >= 1);
    }

    @Test
    public void testEdit() throws Exception {
        log.debug("testing edit...");
        action.setId(1L);
        assertNull(action.getPerson());
        assertEquals("success", action.edit());
        assertNotNull(action.getPerson());
        assertFalse(action.hasActionErrors());
    }

    @Test
    public void testSave() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        ServletActionContext.setRequest(request);
        action.setId(1L);
        assertEquals("success", action.edit());
        assertNotNull(action.getPerson());

        // update last name and save
        action.getPerson().setLastName("Updated Last Name");
        assertEquals("input", action.save());
        assertEquals("Updated Last Name", action.getPerson().getLastName());
        assertFalse(action.hasActionErrors());
        assertFalse(action.hasFieldErrors());
        assertNotNull(request.getSession().getAttribute("messages"));
    }

    @Test
    public void testRemove() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        ServletActionContext.setRequest(request);
        action.setDelete("");
        Person person = new Person();
        person.setId(2L);
        action.setPerson(person);
        assertEquals("success", action.delete());
        assertNotNull(request.getSession().getAttribute("messages"));
    }
}
