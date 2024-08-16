package com.alperen.hospitalsystem.controller;

import com.alperen.hospitalsystem.Request.PatientRequest;
import com.alperen.hospitalsystem.entity.Patient;
import com.alperen.hospitalsystem.service.abstracts.IPatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patient")
public class PatientController {

    private final IPatientService patientService;

    @Autowired
    public PatientController(IPatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping("/addPatient")
    public Patient save(@RequestBody Patient patient){
        return patientService.save(patient);
    }

    @GetMapping("/getall")
    public List<Patient> getAllPatient(){
        return patientService.findAll();
    }

    @GetMapping("searchByGender/{gender}")
    public List<Patient> searchByGender(@PathVariable("gender") char gender){
        return  patientService.findByGender(gender);
    }


    @GetMapping("searchName/{name}")
    public List<Patient> searchByName(@PathVariable("name") String name){
        return  patientService.findByName(name);
    }

    @GetMapping("searchLastName/{lastname}")
    public List<Patient> searchByLastName(@PathVariable("lastname") String lastName){
        return  patientService.findByLastName(lastName);
    }

    @PutMapping("/update/{id}")
    public Patient updatePatient(@PathVariable("id")int id, @RequestBody Patient patient){
        return patientService.update(id, patient);
    }

    @DeleteMapping("/delete/{id}")
    public Patient detelePatient(@PathVariable("id")int id){
        return patientService.deleteById(id);
    }



//    @PostMapping(   consumes = MediaType.APPLICATION_JSON_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<Patient> save(@RequestBody PatientRequest PatientRequest){
//        return null;
//        //return new ResponseEntity<>(patientService.save(patient), HttpStatus.CREATED);
//    }

//    @GetMapping("/searchByGender/{gender}")
//    public ResponseEntity<List<Patient>> searchByGender(@PathVariable("gender") String gender){
//        return new ResponseEntity<>(patientService.findByGender(gender), HttpStatus.CREATED);
//    }
}
