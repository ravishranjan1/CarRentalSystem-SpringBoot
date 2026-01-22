package com.rentify.carrental.validators;

import java.util.List;

public interface DataValidator <T>{
    List<String> validate(T data);
}
