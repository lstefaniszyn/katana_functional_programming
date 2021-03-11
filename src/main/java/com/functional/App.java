package com.functional;

import com.functional.models.Employee;
import com.functional.repositories.EmployeeFileRepository;
import com.functional.repositories.EmployeeRepository;
import com.functional.services.BirthdayService;
import com.functional.services.EmailNotificationService;
import com.functional.services.NotificationService;
import io.vavr.control.Either;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/** Hello world! */
@SpringBootApplication
public class App implements CommandLineRunner {

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

  @Override
  public void run(String... args) {
    // filename -> Open file -> parse files -> take employees -> save into
    // EmployeeRepository -> checkEmployeeBirthdate -> send notification to employee

    EmployeeRepository employeeRepository = new EmployeeFileRepository();
    NotificationService notificationService = new EmailNotificationService();

    Stream<Either<String, Employee>> employees =
        BirthdayService.getEmployeesWithTodaysBirthday
            .apply(employeeRepository)
            .map(notificationService::send);

    System.out.println("Errors: " + employees.filter(Either::isLeft).collect(Collectors.toList()));
    System.out.println(
        "Success: " + employees.filter(Either::isRight).collect(Collectors.toList()));
  }
}
