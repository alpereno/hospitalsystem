package com.alperen.hospitalsystem.controller;

import com.alperen.hospitalsystem.Request.PatientRequest;
import com.alperen.hospitalsystem.Response.PatientResponse;
import com.alperen.hospitalsystem.exception.IncorrectSavePatientException;
import com.alperen.hospitalsystem.service.abstracts.IPatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patient")
@CrossOrigin(origins = "http://localhost:3000")

public class PatientController {

    private final IPatientService patientService;

    @Autowired
    public PatientController(IPatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping("/addPatient")
    public ResponseEntity<PatientResponse> save(@RequestBody PatientRequest patient) throws IncorrectSavePatientException {
        return new ResponseEntity<>(patientService.save(patient), HttpStatus.CREATED);
    }

    @GetMapping("/getall")
    public ResponseEntity<List<PatientResponse>> getAllPatient(){
        return new ResponseEntity<>(patientService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/searchByGender/{gender}")
    public ResponseEntity<List<PatientResponse>> searchByGender(@PathVariable("gender") char gender){
        return new ResponseEntity<>(patientService.findByGender(gender), HttpStatus.OK);
    }

    @GetMapping("/searchName/{name}")
    public ResponseEntity<List<PatientResponse>> searchByName(@PathVariable("name") String name){
        return new ResponseEntity<>(patientService.findByName(name), HttpStatus.OK);
    }

    @GetMapping("/searchLastName/{lastname}")
    public ResponseEntity<List<PatientResponse>> searchByLastName(@PathVariable("lastname") String lastName){
        return new ResponseEntity<>(patientService.findByLastName(lastName), HttpStatus.OK);
    }

    @GetMapping("/listAgeBetween/{start}/{end}")
    public ResponseEntity<List<PatientResponse>> listAgeBetween(@PathVariable("start") int start, @PathVariable("end") int end){
        return new ResponseEntity<>(patientService.findByDateOfBirthBetween(start, end), HttpStatus.OK);
    }

    @GetMapping("/listAgeBetweenAndGender/{start}/{end}/{gender}")
    public ResponseEntity<List<PatientResponse>> listAgeBetweenAndGender(@PathVariable("start") int start, @PathVariable("end") int end,
                                                                         @PathVariable char gender){
        return new ResponseEntity<>(patientService.findByAgeRangeAndGender(start, end, gender), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<PatientResponse> updatePatient(@PathVariable("id")int id, @RequestBody PatientRequest patientRequest){
        return new ResponseEntity<>(patientService.update(id, patientRequest), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deletePatient(@PathVariable("id")int id){
        return new ResponseEntity<>(patientService.deleteById(id), HttpStatus.OK);
    }
}
