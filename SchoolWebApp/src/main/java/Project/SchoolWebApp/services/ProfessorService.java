package Project.SchoolWebApp.services;

import Project.SchoolWebApp.dtos.professor_dtos.ProfessorBasicInfoDTO;
import Project.SchoolWebApp.dtos.professor_dtos.ProfessorDTO;
import Project.SchoolWebApp.dtos.professor_dtos.ProfessorUpdateDTO;
import Project.SchoolWebApp.dtos.student_dtos.StudentBasicInfoDTO;
import Project.SchoolWebApp.exceptions.BadRequestException;
import Project.SchoolWebApp.exceptions.DataConflictException;
import Project.SchoolWebApp.exceptions.DataNotFoundException;
import Project.SchoolWebApp.factory.UserCodeFactory;
import Project.SchoolWebApp.mappers.UserMap;
import Project.SchoolWebApp.models.Professor;
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

    private final ProfessorRepository professorRepository;

    /**
     * Lists all professors in DB
     * @return A list of ProfessorBasicInfoDTO
     */
    public List<ProfessorBasicInfoDTO> allProfessors(){

        List<ProfessorBasicInfoDTO> responseDTO = professorRepository.findAll().stream()
                                                            .map(ProfessorBasicInfoDTO::new).toList();
        return responseDTO;
    }

    /**
     * Search a professor from DB by id
     * @param code the professor id
     * @return A ProfessorBasicInfoDTO
     */
    public ProfessorBasicInfoDTO searchByCode(String code){

        var responseDTO = professorRepository.findById(code).map(ProfessorBasicInfoDTO::new)
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

        var response = professorRepository.findByName(name).stream().map(ProfessorBasicInfoDTO::new).toList();

        if(response.isEmpty()){
            throw new DataNotFoundException("The name does not exist.");
        }

        return response;
    }

    /***
     * Creates a new professor in DB
     * @param data object used to transfer data to the real entity
     * @return The basic information of the new professor from DB
     */
    public ProfessorBasicInfoDTO create(ProfessorDTO data){

        if(professorRepository.existsByEmail(data.email()))
            throw new DataConflictException("the email is already in use.");
        String code = null;
        while(true){

            code = UserCodeFactory.generate("professor");
            if(professorRepository.existsById(code))
                continue;
            break;
        }

        Professor professor = UserMap.toEntity(data, code);
        professorRepository.save(professor);

        return UserMap.toDTO(professor);
    }

    /***
     * Update a professor from DB
     * @param code the id used to find the professor to be updated
     * @param data the object with the information to be updated
     * @return the basic information from the updated professor
     */
    public ProfessorBasicInfoDTO update(String code, ProfessorUpdateDTO data){

        Professor professor = professorRepository.findById(code)
                                                 .orElseThrow(()-> new DataNotFoundException
                                                         ("there is no professor with this id."));
        if(professor.getEmail().equals(data.email()))
            throw new BadRequestException("This user already uses the current email.");
        if(professorRepository.existsByEmail(data.email()))
            throw new DataConflictException("The email is already in use.");

        professor = UserMap.updateEntity(data, professor);
        professorRepository.save(professor);

        return UserMap.toDTO(professor);
    }

    /***
     * Delete professor from DB by informing the professor id
     * @param code the id used to delete the professor
     * @return The basic information of the deleted professor
     */
    public ProfessorBasicInfoDTO delete(String code){

        Professor professor = professorRepository.findById(code)
                                                 .orElseThrow(()-> new DataNotFoundException
                                                         ("there is no professor with this code."));
        ProfessorBasicInfoDTO response = UserMap.toDTO(professor);
        professorRepository.delete(professor);

        return response;
    }
}
