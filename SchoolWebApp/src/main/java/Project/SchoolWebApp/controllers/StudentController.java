package Project.SchoolWebApp.controllers;

import Project.SchoolWebApp.annotations.ValidName;
import Project.SchoolWebApp.dtos.student_dtos.StudentBasicInfoDTO;
import Project.SchoolWebApp.dtos.student_dtos.StudentDTO;
import Project.SchoolWebApp.services.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping("/all")
    public ResponseEntity<List<StudentBasicInfoDTO>> allStudents(){

        List<StudentBasicInfoDTO> allStudentsDTO = studentService.allStudents();

        return new ResponseEntity<List<StudentBasicInfoDTO>>(allStudentsDTO, HttpStatus.OK);
    }

    @GetMapping("/id/{code}")
    public ResponseEntity<StudentBasicInfoDTO> searchByCode(@PathVariable String code){

        StudentBasicInfoDTO responseDTO = studentService.searchByCode(code);

        return new ResponseEntity<StudentBasicInfoDTO>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<StudentBasicInfoDTO>> searchByName(@ValidName @PathVariable String name){

        List<StudentBasicInfoDTO> responseDTO = studentService.searchByName(name);

        return new ResponseEntity<List<StudentBasicInfoDTO>>(responseDTO, HttpStatus.OK);
    }

    @PostMapping("/new")
    public ResponseEntity<StudentBasicInfoDTO> createStudent(@Valid @RequestBody StudentDTO data){

        StudentBasicInfoDTO student = studentService.create(data);

        return new ResponseEntity<StudentBasicInfoDTO>(student, HttpStatus.CREATED);
    }

    @PutMapping("update/{code}")
    public ResponseEntity<StudentBasicInfoDTO> updatedStudent(@PathVariable String code,
                                                              @Valid @RequestBody StudentDTO data){

        StudentBasicInfoDTO DTO = studentService.update(code, data);

        return new ResponseEntity<>(DTO, HttpStatus.OK);
    }
}
