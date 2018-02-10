package com.datsystems.requirements.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datsystems.requirements.model.Baseline;
import com.datsystems.requirements.model.Requirement;

public class BaselineTests {
  final Logger logger = LoggerFactory.getLogger(BaselineTests.class);
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
  static Requirement createRequirement(String id, String text) {
    Requirement req = new Requirement();
    req.setId(id);
    req.setType("Header");
    req.setText(text);
    Properties options = new Properties();
    options.setProperty("category", "header");
    req.setOptions(options);
    return req;
  }
  
  static Baseline createBaseline() {
    Baseline baseline = new Baseline();
    baseline.setName("First Baseline");
    Properties options = new Properties();
    options.setProperty("version", "pdr");
    baseline.setOptions(options);
    return baseline;
  }
  @Test
  public void testPersistSingle() {
    em.getTransaction().begin();
    
    List<Requirement> reqs = new ArrayList<Requirement>();
    for (int i=0;i<10;i++) {
      Requirement req = createRequirement("req-" + i, "text: " + i);
      em.persist(req);
      reqs.add(req);
    }
    Baseline bl = createBaseline();
    bl.setRequirements(reqs);
    em.persist(bl);
    em.getTransaction().commit();
  }
}
