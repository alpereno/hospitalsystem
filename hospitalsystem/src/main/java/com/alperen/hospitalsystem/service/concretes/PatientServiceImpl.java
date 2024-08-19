package com.alperen.hospitalsystem.service.concretes;

import com.alperen.hospitalsystem.Request.PatientRequest;
import com.alperen.hospitalsystem.Response.PatientResponse;
import com.alperen.hospitalsystem.entity.Patient;
import com.alperen.hospitalsystem.exception.InappropriateRequestException;
import com.alperen.hospitalsystem.exception.IncorrectSavePatientException;
import com.alperen.hospitalsystem.repository.abstracts.IPatientRepository;
import com.alperen.hospitalsystem.service.abstracts.IPatientService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PatientServiceImpl implements IPatientService {

    private IPatientRepository patientRepository;
    @Value("${rabbitmq.exchange.name}")
    private final DirectExchange exchange;
    private final AmqpTemplate rabbitTemplate;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;
    @Value("${rabbitmq.queue.name}")
    private String queueName;

    @Autowired
    public PatientServiceImpl(IPatientRepository patientRepository, DirectExchange exchange, AmqpTemplate rabbitTemplate){
        this.patientRepository = patientRepository;
        this.exchange = exchange;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public List<PatientResponse> findAll() {
        List<Patient> patients = patientRepository.findAllByIsActiveTrue();
        List<PatientResponse> patientResponseList = new ArrayList<>();
        fillResponseList(patients, patientResponseList);
        return patientResponseList;
    }

    @Override
    public PatientResponse findById(int id) throws InappropriateRequestException {
        Patient patient = patientRepository.findById(id).orElseThrow(() -> new InappropriateRequestException("Patient not found with id: " + id));
        PatientResponse response = new PatientResponse();
        fillPatientResponse(patient, response);
        return response;
    }

    @Override
    public List<PatientResponse> findByName(String name) {
        List<Patient> patients = patientRepository.findByFirstNameContainingIgnoreCaseAndIsActiveTrue(name);
        List<PatientResponse> patientResponseList = new ArrayList<>();
        fillResponseList(patients, patientResponseList);
        return patientResponseList;
    }

    @Override
    public List<PatientResponse> findByLastName(String lastName) {
        List<Patient> patients = patientRepository.findByLastNameContainingIgnoreCaseAndIsActiveTrue(lastName);
        List<PatientResponse> patientResponseList = new ArrayList<>();
        fillResponseList(patients, patientResponseList);
        return patientResponseList;
    }

    @Override
    public List<PatientResponse> findByGender(char gender) {
        if (gender == 'M' || gender == 'F'){
            List<Patient> patients = patientRepository.findByGenderAndIsActiveTrue(gender);
            List<PatientResponse> patientResponseList = new ArrayList<>();
            fillResponseList(patients, patientResponseList);
            return patientResponseList;
        }
        return null;
    }

    @Override
    public List<PatientResponse> findByDateOfBirthBetween(int startAge, int endAge) {
        Timestamp startDate = Timestamp.valueOf(LocalDate.now().minusYears(endAge + 1).plusDays(1).atStartOfDay());
        Timestamp endDate = Timestamp.valueOf(LocalDate.now().minusYears(startAge).plusDays(1).atStartOfDay());
        List<Patient> patients = patientRepository.findByDateOfBirthBetweenAndIsActiveTrue(startDate, endDate);
        List<PatientResponse> patientResponseList = new ArrayList<>();
        fillResponseList(patients, patientResponseList);
        return patientResponseList;
    }

    @Override
    public List<PatientResponse> findByAgeRangeAndGender(int startAge, int endAge, char gender) {
        Timestamp startDate = Timestamp.valueOf(LocalDate.now().minusYears(endAge + 1).plusDays(1).atStartOfDay());
        Timestamp endDate = Timestamp.valueOf(LocalDate.now().minusYears(startAge).plusDays(1).atStartOfDay());
        List<Patient> patients = patientRepository.findByDateOfBirthBetweenAndGenderAndIsActiveTrue(startDate, endDate, gender);
        List<PatientResponse> patientResponseList = new ArrayList<>();
        fillResponseList(patients, patientResponseList);

        //rabbitTemplate.convertAndSend(secondExchange.getName(), secondRoutingKey, patientResponseList);
        return patientResponseList;
    }

    @Override
    public PatientResponse save(PatientRequest patientRequest) throws IncorrectSavePatientException {
        Patient newPatient = new Patient();
        if (patientRequest.getTckn() == null) throw new IncorrectSavePatientException("TCKN is not appropriate");
        if(!patientRepository.findByFirstNameContainingIgnoreCaseAndIsActiveTrue(patientRequest.getFirstName()).isEmpty())
            throw new IncorrectSavePatientException("This record already exist");

        fillPatientFields(newPatient, patientRequest, true);

        newPatient = patientRepository.save(newPatient);

        PatientResponse response = new PatientResponse();
        fillPatientResponse(newPatient, response);

        rabbitTemplate.convertAndSend(exchange.getName(), routingKey, response);
        return response;
    }

    @Override
    public PatientResponse update(int id, PatientRequest patientRequest) throws InappropriateRequestException{
        // check if patient active
        // Find the existing patient by ID

        Patient existingPatient = patientRepository.findById(id)
                .orElseThrow(() -> new InappropriateRequestException("Patient not found"));

        // Deactivate the existing patient record
        existingPatient.setActive(false);
        patientRepository.save(existingPatient);

        // Create a new patient record with the updated details
        Patient newPatient = new Patient();
        newPatient.setTckn(existingPatient.getTckn());
        newPatient.setPassportNumber(existingPatient.getPassportNumber());
        newPatient.setDateOfBirth(existingPatient.getDateOfBirth());
        fillPatientFields(newPatient, patientRequest, false);
        newPatient.setVersionNumber(existingPatient.getVersionNumber() + 1); // Increment the version number
        patientRepository.save(newPatient); // Save the new patient record

        PatientResponse response = new PatientResponse();
        fillPatientResponse(newPatient, response);

        rabbitTemplate.convertAndSend(exchange.getName(), routingKey, response);

        return response;
    }

    @Override
    public boolean deleteById(int id) throws InappropriateRequestException {
        // instead of deleting change the patient's activity status
        Patient patient =  patientRepository.findById(id).orElseThrow(() -> new InappropriateRequestException("Patient not found with id: " + id));

        patient.arrangeUpdateTime();
        patient.setActive(false);
        patientRepository.save(patient);
        return patient.isActive();
    }

    private void fillPatientFields(Patient patient, PatientRequest patientRequest, boolean isNewRecord){
        patient.setFirstName(patientRequest.getFirstName());
        patient.setMiddleName(patientRequest.getMiddleName());
        patient.setLastName(patientRequest.getLastName());
        patient.setGender(patientRequest.getGender());
        patient.setAddress(patientRequest.getAddress());
        patient.setEmailActive(patientRequest.isEmailActive());
        patient.setSmsActive(patientRequest.isSmsActive());

        LocalDateTime localDateTime = LocalDateTime.now();
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        patient.setUpdatedAt(Timestamp.from(zonedDateTime.toInstant()));

        patient.setActive(true);

        patient.setEmailAddresses(patientRequest.getEmailAddresses());
        patient.setPhoneNumbers(patientRequest.getPhoneNumbers());

        patient.arrangePatientEmailAddress();
        patient.arrangePatientPhoneNumbers();

        if (isNewRecord){
            patient.setDateOfBirth(patientRequest.getDateOfBirth());
            patient.setTckn(patientRequest.getTckn());
            patient.setPassportNumber(patientRequest.getPassportNumber());
            patient.setCreatedAt(Timestamp.from(zonedDateTime.toInstant()));
        }
        else {
            patient.setCreatedAt(patientRequest.getCreatedAt());
        }
    }

    public void fillPatientResponse(Patient patient, PatientResponse response){
         response.setId(patient.getId());
         response.setFirstName(patient.getFirstName());
         response.setMiddleName(patient.getMiddleName());
         response.setLastName(patient.getLastName());
         response.setDateOfBirth(patient.getDateOfBirth());
         response.setGender(patient.getGender());
         response.setAddress(patient.getAddress());
         response.setTckn(patient.getTckn());
         response.setPassportNumber(patient.getPassportNumber());
         response.setPhoneNumbers(patient.getPhoneNumbers());
         response.setEmailAddresses(patient.getEmailAddresses());
         response.setEmailActive(patient.isEmailActive());
         response.setSmsActive(patient.isSmsActive());
    }

    public void fillResponseList(List<Patient> patients, List<PatientResponse> patientResponseList){
        for (Patient p:patients){
            PatientResponse response = new PatientResponse();
            fillPatientResponse(p, response);
            patientResponseList.add(response);
        }
    }
}
