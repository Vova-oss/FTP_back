package com.example.demo.Service;


import com.example.demo.Controller.AuxiliaryClasses.StaticMethods;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Service
@Validated
public class ValidationService {

    /**
     * Валидация объекта, который передаётся в параметрах. Валидация происходит по анатациям, объявленных в Entities
     * Для работы ValidatorFactory и Validator необходима зависимость в pom.xml "spring-boot-starter-validation"
     * @param t объект, который необходимо провалидировать
     * @param <T> класс объекта t
     * @return true, если валидация прошла успеша; false, если обнаружены ошибки
     *
     * @code 400 - Incorrect validation
     */
    public <T> boolean validation(T t){

        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<T>> violations = validator.validate(t);

        if (!violations.isEmpty()) {
            List<String> fields = new LinkedList<>();
            List<String> info = new LinkedList<>();
            for (ConstraintViolation<T> constraintViolation : violations) {
                fields.add(String.valueOf(constraintViolation.getPropertyPath()));
                info.add(constraintViolation.getMessage());
            }
            StaticMethods.createBadResponseDueToValidation(fields, info);
            return false;
        }
        return true;

    }





}
