package com.functional.services;

import com.functional.models.Employee;
import com.functional.repositories.EmployeeRepository;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class BirthdayServiceTest {

  private EmployeeRepository employeeRepository;

  @BeforeAll
  static void initAll() {}

  @AfterAll
  static void tearDownAll() {}

  @BeforeEach
  void init() {
    this.employeeRepository = new MockRepo();
  }

  @AfterEach
  void tearDown() {}

  @Test
  void noEmployeesWithTodaysBirthdayTest() {
    List<Employee> employees =
        BirthdayService.getEmployeesWithTodaysBirthday
            .apply(this.employeeRepository)
            .collect(Collectors.toList());
    assertThat(employees.size(), is(0));
  }

  @Test
  @DisplayName("One Employees With Todays Birthday - mock static method")
  void oneEmployeesWithTodaysBirthdayTest() {
    LocalDate testDate = this.employeeRepository.getAll().get(0).getBirthday();
    try (MockedStatic<LocalDate> theMock = Mockito.mockStatic(LocalDate.class)) {
      theMock
          .when(
              () -> {
                LocalDate.now();
              })
          .thenReturn(testDate);
      List<Employee> employees =
          BirthdayService.getEmployeesWithTodaysBirthday
              .apply(this.employeeRepository)
              .collect(Collectors.toList());
      assertThat(employees.size(), is(1));
    }
  }

  @Test
  @DisplayName("Two Employees With Todays Birthday - mock static method")
  void twoEmployeesWithTodaysBirthdayTest() {
    LocalDate testDate = LocalDate.of(1938, 10, 1);
    try (MockedStatic<LocalDate> theMock = Mockito.mockStatic(LocalDate.class)) {
      theMock.when(LocalDate::now).thenReturn(testDate);
      List<Employee> employees =
          BirthdayService.getEmployeesWithTodaysBirthday
              .apply(this.employeeRepository)
              .collect(Collectors.toList());
      assertThat(employees.size(), is(2));
    }
  }
}

class MockRepo implements EmployeeRepository {

  List<Employee> emp =
      List.of(
          new Employee("John", "Bull", LocalDate.of(1912, 1, 1), "john.bull@test.me"),
          new Employee("Adrey", "Collins", LocalDate.of(2000, 12, 12), "adrey.collins@test.me"),
          new Employee("Bugs", "Bunny", LocalDate.of(1938, 10, 1), "bugs.bunny@test.me"),
          new Employee("Yosemite", "Sam", LocalDate.of(1938, 10, 1), "yosemite.sam@test.me"));

  @Override
  public List<Employee> getAll() {
    return emp;
  }
}
