package com.everyhelpcounts.web.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Role {

  @JsonIgnore
  private Long id;

  private ERole name;

  public Role() {

  }
}