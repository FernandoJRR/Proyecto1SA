package com.sa.establishment_service.shared.domain;

import java.util.UUID;

import com.sa.domain.Auditor;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Establishment extends Auditor {
  private String name;
  private String address;
  private boolean active;

  public Establishment(UUID id, String name, String address) {
    super(id);
    this.name = name;
    this.address = address;
    this.active = true;
  }
}
