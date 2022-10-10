package com.example.dynamoDemo.mappers;

import org.mapstruct.Mapping;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.CLASS)
@Mapping(source = "address.city", target = "address.city")
@Mapping(source = "address.country", target = "address.country")
@Mapping(source = "address.phoneNumber", target = "address.phoneNumber")
@Mapping(source = "address.postcode", target = "address.postcode")
@Mapping(source = "address.street", target = "address.street")
public @interface ConvertAddress {}
