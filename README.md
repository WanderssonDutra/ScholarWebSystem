# ScholarWebSystem
A software web system to manage students and their grades, as well as scholar periods and it's subjects.

## Class Diagrams

<img width="411" height="316" alt="Scholar system website class diagram - User Heritage drawio" src="https://github.com/user-attachments/assets/c88dcae3-b594-4220-ada0-ec777c06e69c" /><br/><br/>
<img width="1286" height="674" alt="Scholar system website diagram - Student  Version 2drawio drawio" src="https://github.com/user-attachments/assets/39fb17d2-9cdd-42fe-bfe4-d5019b9aee4b" /><br/><br/>
<img width="1511" height="729" alt="Scholar system website diagrams - Professor Version 2 drawio" src="https://github.com/user-attachments/assets/53db8cb1-608a-4c46-98b2-c6e754ae6421" /><br/><br/>
## EndPoints<br/>
### StudentController Endpoints<br/>

* GET
  * student/all: returns all students from DB
  * student/id/{code}: returns a student with the specified id
  * student/name/{name}: returns all students with the matched name
* POST
  * student/new: creates a new student in DB
* PUT
  * student/update/{code}: update student from DB
* DELETE
  * student/delete/{code}: deletes student from DB

### ProfessorController Endpoints<br/>
* GET
  * professor/all: returns all professors from DB
  * professor/id/{code}: returns a professor with the specified id
  * professor/name/{name}: returns all the professors with the matched name
* POST
  * professor/new: creates a new professor in DB
* PUT
  * professor/update/{code}: update professor from DB
* DELETE
  * professor/delete/{code}: deletes professor from DB
