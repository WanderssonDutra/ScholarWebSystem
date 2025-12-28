package Project.SchoolWebApp.services;

import Project.SchoolWebApp.dtos.professor_dtos.ProfessorBasicInfoDTO;
import Project.SchoolWebApp.dtos.student_dtos.StudentBasicInfoDTO;
import Project.SchoolWebApp.exceptions.BadRequestException;
import Project.SchoolWebApp.exceptions.DataNotFoundException;
import Project.SchoolWebApp.mappers.UserMap;
import Project.SchoolWebApp.repositories.ProfessorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

/**
 * Class service that handles business logic
 */
@Service
@RequiredArgsConstructor
public class ProfessorService {

    private final ProfessorRepository repository;

    /**
     * Lists all professors in DB
     * @return A list of ProfessorBasicInfoDTO
     */
    public List<ProfessorBasicInfoDTO> allProfessors(){

        List<ProfessorBasicInfoDTO> responseDTO = repository.findAll().stream()
                                                            .map(ProfessorBasicInfoDTO::new).toList();
        return responseDTO;
    }

    /**
     * Search a professor from DB by id
     * @param code the professor id
     * @return A ProfessorBasicInfoDTO
     */
    public ProfessorBasicInfoDTO searchByCode(String code){

        if(!(code.substring(0,3).contains("pfr") && code.substring(4,9).matches("^[0-9]+$")))
            throw new BadRequestException("the code must begin with the letters: \"pfr\" " +
                                          "following a sequence of six numbers.");

        var responseDTO = repository.findById(code).map(ProfessorBasicInfoDTO::new)
                                                 .orElseThrow(()-> new DataNotFoundException
                                                         ("there is no user with this id."));

        return responseDTO;
    }

    /**
     * Search professors that matches the name passed
     * @param name the String to be searched
     * @return A list of ProfessorBasicInfoDTO
     */
    public List<ProfessorBasicInfoDTO> searchByName(String name){

        var response = repository.findByName(name).stream().map(ProfessorBasicInfoDTO::new).toList();

        if(response.isEmpty()){
            throw new DataNotFoundException("The name does not exist.");
        }

        return response;
    }
}
