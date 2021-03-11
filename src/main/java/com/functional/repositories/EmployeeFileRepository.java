package com.functional.repositories;

import com.functional.models.Employee;
import io.vavr.control.Try;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class EmployeeFileRepository implements EmployeeRepository {

  static Function<String, Try<BufferedReader>> fileContent =
      path ->
          Try.of(
              () -> {
                File file = new File(path);
                FileReader fileReader = new FileReader(file);
                System.out.println("Opened");
                return new BufferedReader(fileReader);
              });

  static Function<String, List<Employee>> employees =
      (String path) -> {
        var employees = new ArrayList<Employee>();
        fileContent
            .apply(path)
            .onSuccess(
                (BufferedReader f) -> {
                  employees.addAll(
                      f.lines()
                          .dropWhile(
                              line -> line.contains("last_name; first_name; date_of_birth; email"))
                          .map(line -> line.split(";"))
                          .map(s -> Arrays.stream(s).map(String::trim).toArray(String[]::new))
                          .map(el -> new Employee(el[1], el[0], LocalDate.parse(el[2]), el[3]))
                          .collect(Collectors.toList()));
                })
            .onFailure(
                e ->
                    System.out.println(
                        "Error: Unable to read file in path:"
                            + path
                            + ". Error message: "
                            + e.getMessage()))
            .andThenTry(BufferedReader::close)
            .andFinally(() -> System.out.println("Closing file."));
        return employees;
      };

  private String fileSourcePath =
      System.getProperty("user.dir") + Paths.get("/src/test/resources/employee_test.csv");

  public void setFileSourcePath(String fileSourcePath) {
    this.fileSourcePath = fileSourcePath;
  }

  @Override
  public List<Employee> getAll() {
    return EmployeeFileRepository.employees.apply(this.fileSourcePath);
  }
}
