package Project.SchoolWebApp.controllers;

import Project.SchoolWebApp.annotations.ValidName;
import Project.SchoolWebApp.dtos.professor_dtos.ProfessorBasicInfoDTO;
import Project.SchoolWebApp.services.ProfessorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    @GetMapping("id/{code}")
    public ResponseEntity<ProfessorBasicInfoDTO> searchById(@PathVariable String code){

        var response = professorService.searchByCode(code);

        return new ResponseEntity<ProfessorBasicInfoDTO>(response, HttpStatus.OK);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<ProfessorBasicInfoDTO>> searchByName(@ValidName @PathVariable String name){

        var response = professorService.searchByName(name);

        return new ResponseEntity<List<ProfessorBasicInfoDTO>>(response, HttpStatus.OK);
    }
}
