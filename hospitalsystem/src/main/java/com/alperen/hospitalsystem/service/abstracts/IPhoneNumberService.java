package com.alperen.hospitalsystem.service.abstracts;

import com.alperen.hospitalsystem.entity.PhoneNumber;

import java.util.List;

public interface IPhoneNumberService {
    public List<PhoneNumber> findAll();
    public PhoneNumber findById();
    public PhoneNumber save();
    public void deleteById(int id);
}
