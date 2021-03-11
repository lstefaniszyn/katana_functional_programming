package com.functional.models;

import java.time.LocalDate;

public class Employee {

  private final String firstName;
  private final String lastName;
  private final LocalDate birthday;
  private final String email;

  public Employee(String firstName, String lastName, LocalDate birthday, String email) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.birthday = birthday;
    this.email = email;
  }

  public String getEmail() {
    return email;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public LocalDate getBirthday() {
    return birthday;
  }

  @Override
  public String toString() {
    return String.join(
        ", ",
        this.getFirstName(),
        this.getLastName(),
        this.getBirthday().toString(),
        this.getEmail(),
        ";");
  }
}
