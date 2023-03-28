package com.example.productapiannotation.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Product {

  @Id
  private String id;

  private String name;

  private Double price;

  public Product(String id, String name, Double price) {
    this.id = id;
    this.name = name;
    this.price = price;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Product product)) return false;

    if (getId() != null ? !getId().equals(product.getId()) : product.getId() != null) return false;
    if (getName() != null ? !getName().equals(product.getName()) : product.getName() != null) return false;
    return getPrice() != null ? getPrice().equals(product.getPrice()) : product.getPrice() == null;
  }



  @Override
  public int hashCode() {
    int result = getId() != null ? getId().hashCode() : 0;
    result = 31 * result + (getName() != null ? getName().hashCode() : 0);
    result = 31 * result + (getPrice() != null ? getPrice().hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "Product{" +
        "id='" + id + '\'' +
        ", name='" + name + '\'' +
        ", price=" + price +
        '}';
  }
}
