package com.functional.repositories;

import com.functional.models.Employee;
import org.junit.jupiter.api.*;

import java.nio.file.Paths;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class EmployeeFileRepositoryTest {

  private final String DEFAULT_FILE_SOURCE_FILE_PATH =
      System.getProperty("user.dir") + Paths.get("/src/test/resources/employee_test.csv");

  private EmployeeFileRepository employeeRepository;

  @BeforeAll
  static void initAll() {}

  @AfterAll
  static void tearDownAll() {}

  @BeforeEach
  void init() {
    this.employeeRepository = new EmployeeFileRepository();
  }

  @AfterEach
  void tearDown() {}

  @Test
  void readFileThatExists_Test() {
    List<Employee> employees = employeeRepository.getAll();
    assertThat(employees.size(), is(2));
  }

  @Test
  void readFileDoesNotExists_Test() {
    this.employeeRepository.setFileSourcePath("/src/test/resources/does_not_exists.csv");

    assertThat(this.employeeRepository.getAll().size(), is(0));
  }

  @Test
  void pathAsNull() {
    this.employeeRepository.setFileSourcePath(null);
    assertThat(this.employeeRepository.getAll().size(), is(0));
  }
}
