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
        return patientRepository.findAll();
    }

    @Override
    public Patient findById(int id) {
        return patientRepository.findById(id).orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));
    }

    @Override
    public List<Patient> findByName(String name) {
        return patientRepository.findByFirstName(name);
    }

    @Override
    public List<Patient> findByLastName(String lastName) {
        return patientRepository.findByLastName(lastName);
    }

    @Override
    public List<Patient> findByGender(char gender) {
        if (gender == 'M' || gender == 'F'){
            return patientRepository.findByGender(gender);
        }
        return null;
    }

    @Override
    public List<Patient> findByDateOfBirthBetween(int startAge, int endAge) {
        Timestamp startDate = Timestamp.valueOf(LocalDate.now().minusYears(endAge + 1).plusDays(1).atStartOfDay());
        Timestamp endDate = Timestamp.valueOf(LocalDate.now().minusYears(startAge).atStartOfDay());

        return patientRepository.findByDateOfBirthBetween(startDate, endDate);
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
        Patient patient = patientRepository.findById(id).orElseThrow(() -> new RuntimeException("Patient not found"));
        patient.updateFields(updatedPatient);
        return patientRepository.save(patient);
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
