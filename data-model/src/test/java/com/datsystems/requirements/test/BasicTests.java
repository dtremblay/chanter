package com.datsystems.requirements.test;

import java.util.HashMap;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datsystems.requirements.model.Requirement;

public class BasicTests {
  final Logger logger = LoggerFactory.getLogger(BasicTests.class);
  private static String URL = "jdbc:postgresql://boot2docker/requirements";
  
  private static String DRIVER = "org.postgresql.Driver";
  private static String USER = "reqpro";
  private static String PASSWORD = "password";
 
  private EntityManager em;
  
  @Before
  public void setup() {
    HashMap<String, String> props = new HashMap<String, String>();
    props.put("openjpa.ConnectionDriverName", DRIVER);
    props.put("openjpa.ConnectionURL", URL);
    props.put("openjpa.ConnectionUserName", USER);
    props.put("openjpa.ConnectionPassword", PASSWORD);
    
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("requirements", props);
    em = emf.createEntityManager();
  }
  static Requirement createRequirement() {
    Requirement req = new Requirement();
    req.setId("my-project-id-1");
    req.setType("Header");
    req.setText("Section 1.0");
    Properties options = new Properties();
    options.setProperty("category", "header");
    req.setOptions(options);
    return req;
  }
  @Test
  public void testPersistSingle() {
    em.getTransaction().begin();
    em.persist(createRequirement());
    em.getTransaction().commit();
  }
  
  @Test
  public void testPersistMultipleOptions() {
    Requirement req = createRequirement();
    req.getOptions().setProperty("context", "none");
    em.getTransaction().begin();
    em.persist(req);
    em.getTransaction().commit();
  }
  
  @Test
  public void testPersistMultipleVersion() {
    Requirement req = createRequirement();
    req.getOptions().setProperty("context", "first");
    em.getTransaction().begin();
    em.persist(req);
    em.getTransaction().commit();
    
    Requirement copy = req.copy();
    copy.getOptions().setProperty("context", "second");
    copy.setText("Section 1.0 Update");
    em.getTransaction().begin();
    em.persist(copy);
    em.getTransaction().commit();
  }
}
