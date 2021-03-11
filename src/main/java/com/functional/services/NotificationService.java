package com.functional.services;

import com.functional.models.Employee;
import io.vavr.control.Either;
import org.springframework.stereotype.Service;

@Service
public interface NotificationService {

  public Either<String, Employee> send(Employee employee);
}
