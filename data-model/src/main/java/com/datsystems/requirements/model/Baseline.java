package com.datsystems.requirements.model;

import java.util.List;
import java.util.Properties;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;

@Entity
public class Baseline {
  String guid;
  String name;
  String description;
  List<Requirement> requirements;
  Properties options;
  
  @Id
  @Column(length=36)
  public String getGuid() {
    return guid;
  }
  public void setGuid(String guid) {
    this.guid = guid;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  
  @OneToMany(cascade=CascadeType.MERGE)
  public List<Requirement> getRequirements() {
    return requirements;
  }
  public void setRequirements(List<Requirement> requirements) {
    this.requirements = requirements;
  }
  
  @ElementCollection
  @CollectionTable(
        name="Baseline_Options",
        joinColumns=  {
          @JoinColumn(name="bl-guid", referencedColumnName="guid"), 
        }
  )
  public Properties getOptions() {
    return options;
  }
  public void setOptions(Properties options) {
    this.options = options;
  }
  @PrePersist
  public void prePersist() {
    setGuid(UUID.randomUUID().toString());
  }
}
