package com.functional.services;

import com.functional.models.Employee;
import io.vavr.control.Try;
import org.junit.jupiter.api.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.util.function.Function;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class EmailNotificationServiceTest {

  private Employee employee;
  private NotificationService emailNotificationService;

  @BeforeAll
  static void initAll() {}

  @AfterAll
  static void tearDownAll() {}

  @BeforeEach
  void init() {
    this.employee = new Employee("John", "Bull", LocalDate.of(1912, 1, 1), "john.bull@test.me");
    this.emailNotificationService = new EmailNotificationService();
  }

  @AfterEach
  void tearDown() {}

  @Test
  void sendTest() {
    assertThat(this.emailNotificationService.send(this.employee).isRight(), is(true));
  }

  @Test
  void sendEmptyEmployee_Test() {
    assertThat(this.emailNotificationService.send(null).isLeft(), is(true));
  }

  @Test
  void sendEmptyEmployee_verifyText_Test() {
    assertThat(
        this.emailNotificationService.send(null).getLeft(),
        is("No able to send mail for empty employee"));
  }

  @Test
  void sendReturnsError() throws NoSuchFieldException, IllegalAccessException {

    // allow EmailService.send field to be changed
    Field send = EmailNotificationService.class.getDeclaredField("sendMail");
    send.setAccessible(true);

    // remove final modifier for this Field
    Field modifiers = Field.class.getDeclaredField("modifiers");
    modifiers.setAccessible(true);
    modifiers.setInt(send, send.getModifiers() & ~Modifier.FINAL);

    // Set Mock value
    Function<Employee, Try<Employee>> sendMock =
        employee -> {
          return Try.of(
              () -> {
                System.out.println("Mock Sending Email for employee: " + employee.getEmail());
                throw new RuntimeException("Test error");
              });
        };
    // Field is static, therefore first arg is null
    send.set(null, sendMock);

    assertThat(
        this.emailNotificationService.send(this.employee).getLeft(),
        is("No able to send mail for employee: " + this.employee));
  }
}
