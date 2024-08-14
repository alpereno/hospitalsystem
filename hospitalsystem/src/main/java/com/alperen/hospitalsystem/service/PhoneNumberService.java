package com.alperen.hospitalsystem.service;

import com.alperen.hospitalsystem.entity.PhoneNumber;

import java.util.List;

public interface PhoneNumberService {
    public List<PhoneNumber> findAll();
    public PhoneNumber findById();
    public PhoneNumber save();
    public void deleteById(int id);
}
