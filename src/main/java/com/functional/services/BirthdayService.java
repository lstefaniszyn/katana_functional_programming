package com.functional.services;

import java.time.LocalDate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.functional.models.Employee;
import com.functional.repositories.EmployeeRepository;

import org.springframework.stereotype.Service;

@Service
public final class BirthdayService {
        
        private BirthdayService() {
        }
        
        private static Supplier<LocalDate> today = () -> LocalDate.now();
        
        private static Function<Employee, Predicate<LocalDate>> isEqualMonth = e -> date -> e.getBirthday()
                        .getMonthValue() == date.getMonthValue();
        private static Function<Employee, Predicate<LocalDate>> isEqualDay = e -> date -> e.getBirthday()
                        .getDayOfMonth() == date.getDayOfMonth();
        static Function<Employee, Predicate<LocalDate>> isTodayBirthdate = e -> date -> isEqualMonth.apply(e)
                        .and(isEqualDay.apply(e))
                        .test(date);
        
        public static Function<EmployeeRepository, Stream<Employee>> getEmployeesWithTodaysBirthday = repo -> repo.getAll()
                        .stream()
                        .filter(e -> isTodayBirthdate.apply(e)
                                        .test(today.get()));
        
}
