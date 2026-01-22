package Project.SchoolWebApp.controllers;

import Project.SchoolWebApp.annotations.ValidCode;
import Project.SchoolWebApp.annotations.ValidName;
import Project.SchoolWebApp.dtos.professor_dtos.ProfessorBasicInfoDTO;
import Project.SchoolWebApp.dtos.professor_dtos.ProfessorDTO;
import Project.SchoolWebApp.dtos.professor_dtos.ProfessorUpdateDTO;
import Project.SchoolWebApp.services.ProfessorService;
import Project.SchoolWebApp.validations.ValidationType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/professor")
public class ProfessorController {

    private final ProfessorService professorService;

    @GetMapping("/all")
    public ResponseEntity<List<ProfessorBasicInfoDTO>> allProfessors(){

        List<ProfessorBasicInfoDTO> responseDTO = professorService.allProfessors();

        return new ResponseEntity<List<ProfessorBasicInfoDTO>>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("/id/{code}")
    public ResponseEntity<ProfessorBasicInfoDTO> searchByCode(@ValidCode (type = ValidationType.PROFESSOR_CODE)
                                                                @PathVariable String code){

        var response = professorService.searchByCode(code);

        return new ResponseEntity<ProfessorBasicInfoDTO>(response, HttpStatus.OK);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<ProfessorBasicInfoDTO>> searchByName(@ValidName @PathVariable String name){

        var response = professorService.searchByName(name);

        return new ResponseEntity<List<ProfessorBasicInfoDTO>>(response, HttpStatus.OK);
    }

    @PostMapping("/new")
    public ResponseEntity<ProfessorBasicInfoDTO> createProfessor(@Valid @RequestBody ProfessorDTO data){

        ProfessorBasicInfoDTO response = professorService.create(data);

        return new ResponseEntity<ProfessorBasicInfoDTO>(response, HttpStatus.CREATED);
    }

    @PutMapping("/update/{code}")
    public ResponseEntity<ProfessorBasicInfoDTO> updateProfessor(@ValidCode(type = ValidationType
                                                                            .PROFESSOR_CODE)
                                                                 @PathVariable String code,
                                                                 @RequestBody ProfessorUpdateDTO data){

        ProfessorBasicInfoDTO response = professorService.update(code, data);

        return new ResponseEntity<ProfessorBasicInfoDTO>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{code}")
    public ResponseEntity<ProfessorBasicInfoDTO> deleteProfessor(@ValidCode(type = ValidationType
                                                                            .PROFESSOR_CODE)
                                                                 @PathVariable String code){

        ProfessorBasicInfoDTO response = professorService.delete(code);

        return new ResponseEntity<ProfessorBasicInfoDTO>(response, HttpStatus.OK);
    }
}
