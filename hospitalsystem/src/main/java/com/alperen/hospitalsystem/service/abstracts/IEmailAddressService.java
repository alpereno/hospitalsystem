package com.alperen.hospitalsystem.service.abstracts;

import com.alperen.hospitalsystem.entity.EmailAddress;

import java.util.List;

public interface IEmailAddressService {
    public List<EmailAddress> findAll();
    public EmailAddress findById();
    public EmailAddress save(EmailAddress emailAddress);
    public void deleteById(int id);
}
