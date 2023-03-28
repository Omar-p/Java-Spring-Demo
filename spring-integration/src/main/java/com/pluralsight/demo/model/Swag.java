package com.pluralsight.demo.model;

public class Swag {

  private String type;

  public Swag(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @Override
  public String toString() {
    return "Swag{" +
        "type='" + type + '\'' +
        '}';
  }
}
