package com.functional.services;

import com.functional.models.Employee;
import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public final class EmailNotificationService implements NotificationService {

  private static final Function<Employee, Try<Employee>> sendMail =
      employee ->
          Try.of(
              () -> {
                System.out.println("Sending Email for employee: " + employee.getEmail());
                return employee;
              });

  @Override
  public Either<String, Employee> send(Employee employee) {
    return Option.of(employee).isEmpty()
        ? Either.left("No able to send mail for empty employee")
        : Either.<String, Employee>right(employee)
            .filterOrElse(
                e -> EmailNotificationService.sendMail.apply(e).isSuccess(),
                out -> "No able to send mail for employee: " + out);
  }
}
