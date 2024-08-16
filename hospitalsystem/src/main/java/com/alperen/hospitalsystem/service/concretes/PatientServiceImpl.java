package com.alperen.hospitalsystem.service.concretes;

import com.alperen.hospitalsystem.entity.Patient;
import com.alperen.hospitalsystem.repository.abstracts.IPatientRepository;
import com.alperen.hospitalsystem.service.abstracts.IPatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Service
public class PatientServiceImpl implements IPatientService {

    private IPatientRepository patientRepository;

    @Autowired
    public PatientServiceImpl(IPatientRepository patientRepository){
        this.patientRepository = patientRepository;
    }

    @Override
    public List<Patient> findAll() {
        return patientRepository.findAllByIsActiveTrue();
    }

    @Override
    public Patient findById(int id) {
        return patientRepository.findById(id).orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));
    }

    @Override
    public List<Patient> findByName(String name) {
        return patientRepository.findByFirstNameContainingIgnoreCaseAndIsActiveTrue(name);
    }

    @Override
    public List<Patient> findByLastName(String lastName) {
        return patientRepository.findByLastNameContainingIgnoreCaseAndIsActiveTrue(lastName);
    }

    @Override
    public List<Patient> findByGender(char gender) {
        if (gender == 'M' || gender == 'F'){
            return patientRepository.findByGenderAndIsActiveTrue(gender);
        }
        return null;
    }

    @Override
    public List<Patient> findByDateOfBirthBetween(int startAge, int endAge) {
        Timestamp startDate = Timestamp.valueOf(LocalDate.now().minusYears(endAge + 1).plusDays(1).atStartOfDay());
        Timestamp endDate = Timestamp.valueOf(LocalDate.now().minusYears(startAge).atStartOfDay());

        return patientRepository.findByDateOfBirthBetweenAndIsActiveTrue(startDate, endDate);
    }

//    @Override
//    public Patient save(PatientRequest patient) {
//        //Patient patient1 = new Patient();
//        //patient request ten gelen dataları patiente setle
//        // mappeer sil
//        // en son save
//        //patient.set
//
//        //return null;
//        //return patientRepository.save(patientMapper.toEntity(patient));
//    }

    @Override
    public Patient save(Patient patient){
        patient.arrangePatientPhoneNumbers();
        patient.arrangePatientEmailAddress();
        return patientRepository.save(patient);
    }

    @Override
    public Patient update(int id, Patient updatedPatient) {
        // check if patient active

        // Find the existing patient by ID
        Patient existingPatient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        // Deactivate the existing patient record
        existingPatient.setActive(false);
        patientRepository.save(existingPatient);

        // Create a new patient record with the updated details
        Patient newPatient = new Patient();
        newPatient.setTckn(existingPatient.getTckn());
        newPatient.setPassportNumber(existingPatient.getPassportNumber());
        newPatient.setDateOfBirth(existingPatient.getDateOfBirth());
        newPatient.updateFields(updatedPatient); // Copy the updated fields to the new patient
        newPatient.setVersionNumber(existingPatient.getVersionNumber() + 1); // Increment the version number

        return patientRepository.save(newPatient); // Save the new patient record
    }

    @Override
    public Patient deleteById(int id) {
        // instead of deleting change the patient's activity status
        Patient patient =  patientRepository.findById(id).orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));

        patient.arrangeUpdateTime();
        patient.setActive(false);
        return patientRepository.save(patient);
        // patientRepository.deleteById(id);
    }
}
