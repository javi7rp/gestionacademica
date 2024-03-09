# Inicio del proyecto
Para iniciar nuestro proyecto, deberemos primero tener la extension de *Spring Boot Dashboard* que será con la que vamos a iniciar nuestro proyecto.
Y deberemos levantar los contenedores Docker. Donde el docker-compose se encontrará en la carpeta 'stack-spring'
Una vez levantados nos podemos ir al icono de Spring boot y comenzar la conexion. En nuestro navegador podremos visualizar la pagina web en el puerto *8080* y la base de datos 'gestionacademica' en el puerto *8181*

# Gestion Académica

Este proyecto de Gestion Académica está diseñado para gestionar profesores, alumnos, gestores y asignaturas.

## Profesores

Los profesores tienen los siguientes atributos:
- **Usuario**
- **Contraseña**
- **Nombre**
- **Apellidos**
- **Email**

## Alumnos

Los alumnos tienen los siguientes atributos:
- **Usuario**
- **Contraseña**
- **Nombre**
- **Apellido**
- **Teléfono**
- **Email**

## Gestores

Los gestores tienen los siguientes atributos:
- **Usuario**
- **Contraseña**

## Asignaturas

Las asignaturas tienen los siguientes atributos:
- **Nombre**
- **Curso**
- **Ciclo**

Es importante tener en cuenta que:
- Una asignatura puede ser impartida por uno o dos profesores.
- Se pueden matricular hasta 32 alumnos en una asignatura.

## Modelos
Y comenzaremos nuestro proyecto creando los modelos necesarios para abarcar toda la informacion. Algunos ejemplo de las Entitys son: 

```java

@Entity
@Data
@NoArgsConstructor
public class Alumno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 25)
    private String username;
    @Column(length = 100)
    private String password; 
    @Column(length = 50)
    private String nombre; 
    private String apellido; 
    @Column(length = 15)
    private String telefono;
    @Column(length = 100)
    private String email; 

    @Column(columnDefinition = "VARCHAR(255) DEFAULT 'ALUMNO'")
    private String authority;

    @ManyToMany
    private List<Asignatura> asignaturas;
}

@Entity
@Data
@NoArgsConstructor
public class Asignatura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 25)
    private String nombre;
    @Column(length = 25)
    private int curso;
    @Column(length = 25)
    private String ciclo;

    @ManyToMany(mappedBy = "asignaturas")
    private List<Alumno> alumnos;

    @ManyToOne
    private Profesor profesor;
}

@Entity
@Data
@NoArgsConstructor
public class Profesor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 25)
    private String username; 
    @Column(length = 100)
    private String password;
    @Column(length = 50)
    private String nombre; 
    private String apellido; 
    @Column(length = 100)
    private String email; 

    @Column(columnDefinition = "VARCHAR(255) DEFAULT 'PROFESOR'")
    private String authority;

    @OneToMany
    private List<Asignatura> asignaturas;

}
```

y tambien necesitaremos crear una clase Usuario en la que manejaremos la autoridad y rol de cada usuario: 

```java
@Entity
@Data
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 25, unique = true)
    private String username;
    @Column(length = 100)
    private String password;
    @Column(length = 15)
    private String authority;
    
    @OneToOne
    private Gestor gestor;
    @OneToOne
    private Alumno alumno;
    @OneToOne
    private Profesor profesor;
}
```

y crearemos tambien la clase: 

```java
@Data
public class Matricula {

    private Long alumnoId;
    private Long asignaturaId;
    
}
```
que relacionara alumno y asignatura. Y como podemos ver, no es una entidad ya que no tiene @Entity

Una vez creados los modelos, necesitaremos crear un Repositorio para cada uno de ellos: 

## Repositorios

Todos los repositorios extenderan de 'JpaRepository' y tendran la entidad que manejaran como por ejemplo 'Alumno' : 
```java
public interface RepoAlumno extends JpaRepository<Alumno, Long>{
    
    @Query(value = "SELECT * FROM alumno WHERE username = :username", nativeQuery = true)
    Alumno findByUsername(@Param("username") String username);
}

public interface RepoUsuario extends JpaRepository<Usuario, Long>{

    @Query(value = "SELECT * FROM usuario WHERE alumno_id = :alumnoId", nativeQuery = true)
    Usuario findByAlumnoId(@Param("alumnoId") Long alumnoId);

    void deleteByAlumnoId(Long AlumnoId);

    @Query(value = "SELECT * FROM usuario WHERE profesor_id = :profesorId", nativeQuery = true)
    Usuario findByProfesorId(@Param("profesorId") Long profesorId);

    void deleteByProfesorId(Long ProfesorId);
    
}
```

## Controladores
Ahora ya podemos pasar a la creacion de los controladores. En nuestro caso vamos a tener 3, papra *Alumno, Asignatura y Profesor* y 1 general. Estos controladores son utilizados para manejar todas las solicitudes HTTP entrantes y producir una respuesta, asi como un intermediario entre la vista y el modelo. 

En nuestro controller podemos destacar el uso de *@Autowired* el cual utilizaremos para inyectar automaticamente una instacia (RepoAlumno) en una variable (repoAlumno) : 


```java
    @Autowired
    private RepoAlumno repoAlumno;

    @Autowired
    private RepoAsignatura repoAsignatura;
```

En él vamos a manejar todas las peticiones del CRUD: 
### Add
```java
@GetMapping
    public String listarAlumnos(Model modelo) {
        modelo.addAttribute("alumnos", repoAlumno.findAll());
        return "alumno/alumnos";
    }

    @GetMapping("/add")
    public String addAlumnoGet(Model modelo) {
        modelo.addAttribute("alumno", new Alumno());
        return "alumno/add";
    }
    @PostMapping("/add")
    public String addAlumno(@ModelAttribute("alumno") @NonNull Alumno alumno) {
        alumno.setPassword(encoder.encode(alumno.getPassword()));
        alumno.setAuthority("ALUMNO");
        repoAlumno.save(alumno);

        Usuario usuario = new Usuario();
        usuario.setUsername(alumno.getUsername());
        usuario.setPassword(alumno.getPassword());
        usuario.setAuthority(alumno.getAuthority());
        usuario.setAlumno(alumno);
        repoUsuario.save(usuario);
        return "redirect:/alumno";
    }
```
### Delete
```java
@GetMapping("/delete/{id}")
    public String deleteAlumnoForm(Model modelo, @PathVariable("id") @NonNull Long id) {
        Optional<Alumno> oAlumno = repoAlumno.findById(id);
        if (oAlumno.isPresent())
            modelo.addAttribute("alumno", oAlumno.get());
        else {
            modelo.addAttribute("mensaje", "El alumno consultado no existe.");
            return "error";
        }
        return "alumno/delete";
    }
    @Transactional
    @PostMapping("/delete/{id}")
    public String deleteAlumno(@PathVariable("id") @NonNull Long id) {
        repoUsuario.deleteByAlumnoId(id);
        repoAlumno.deleteById(id);
        return "redirect:/alumno";
    }
```
### Edit
```java
@GetMapping("/edit/{id}")
    public String editAlumnoForm(Model modelo, @PathVariable("id") @NonNull Long id) {
        Optional<Alumno> oAlumno = repoAlumno.findById(id);
        if (oAlumno.isPresent()) {
            modelo.addAttribute("alumno", oAlumno.get());
            return "alumno/edit";
        } else {
            modelo.addAttribute("mensaje", "El alumno consultado no existe.");
            return "error";
        }
    }
    @PostMapping("/edit/{id}")
    public String editAlumno(Model modelo, @PathVariable("id") @NonNull Long id,
            @ModelAttribute("alumnoModificado") @NonNull Alumno alumnoModificado) {
        Optional<Alumno> oAlumno = repoAlumno.findById(id);

        if (oAlumno.isPresent()) {
            Alumno alumnoExistente = oAlumno.get();
            Usuario usuarioExistente = repoUsuario.findByAlumnoId(id);

            alumnoExistente.setNombre(alumnoModificado.getNombre());
            alumnoExistente.setApellido(alumnoModificado.getApellido());
            alumnoExistente.setTelefono(alumnoModificado.getTelefono());
            alumnoExistente.setEmail(alumnoModificado.getEmail());
            alumnoExistente.setUsername(alumnoModificado.getUsername());

            usuarioExistente.setUsername(alumnoModificado.getUsername());

            repoAlumno.save(alumnoExistente);
            repoUsuario.save(usuarioExistente);

            return "redirect:/alumno";
        } else {
            return "error";
        }
    }
```

y tambien manejaremos las peticiones para matricular un alumno en una asignatura, obtener las asignaturas matriculadas por ese alumno y obtener los profesores que imparten esas asignaturas. Como por ejemplo:  
### Obtener asignaturas matriculadas del alumno
```java
@GetMapping("/matriculas/{id}")
    public String asignaturasMatriculadas(Model modelo, @PathVariable("id") @NonNull Long id) {
        Alumno alumnoMatriculado;
        if (id != 0) {
            Optional<Alumno> oAlumno = repoAlumno.findById(id);

            if (oAlumno.isPresent()) {
                alumnoMatriculado = oAlumno.get();
            } else {
                modelo.addAttribute("mensaje", "El alumno consultado no existe.");
                return "error";
            }
        } else {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String nombreAlumnoLogueado = authentication.getName();

            alumnoMatriculado = repoAlumno.findByUsername(nombreAlumnoLogueado);

            if (alumnoMatriculado == null) {
                modelo.addAttribute("mensaje", "El profesor consultado no existe.");
                return "error";
            }
        }

        List<Asignatura> asignaturasMatriculadas = alumnoMatriculado.getAsignaturas();

        modelo.addAttribute("alumno", alumnoMatriculado);
        modelo.addAttribute("asignaturas", asignaturasMatriculadas);
        return "alumno/matriculas";
    }
```

Una vez manejadas todas las peticiones, ya podemos mostrar las plantillas Html para realizar cada accion que sea necesaria. Estas plantillas las vamos a manejar a traves de un menu de navegacion 'BootStrap 5.3.3' que estara definido en nuestra plantilla *fragments/General.html*
En esta plantilla tambien introduciremos la autoridad, y dependiendo del acceso que tenga el usuario se mostraran unos menus u otros. Lo vemos dee la forma: 

```html
    <li sec:authorize="hasAuthority('GESTOR')"><a id="menu_modulo" class="dropdown-item" href="/asignatura">Listado Asignaturas</a></li>
    <li sec:authorize="hasAuthority('PROFESOR')"><a id="menu_modulo" class="dropdown-item" href="/profesor/imparte/0">Listado Asignaturas</a></li>
    <li sec:authorize="hasAuthority('ALUMNO')"><a id="menu_modulo" class="dropdown-item" href="/alumno/matriculas/0">Listado Asignaturas</a></li>
    <li sec:authorize="hasAuthority('GESTOR')"><a id="menu_enseñanza" class="dropdown-item" href="/asignatura/add">Añadir Asignaturas</a></li>
```

Ya solo queda crear las plantillas para cada accion que queramos realizar con el alumno: 

## Templates

### alumno / add
```html
    <!DOCTYPE html>
<html lang="es">

<head>
    <title>Gestión Academica: Añadir un alumno</title>
    <th:block th:replace="~{fragments/general.html :: headerfiles}"></th:block>
</head>

<body>

    <div th:replace="~{fragments/general.html :: navigation}"> </div>

    <div class="container">
        <h3>Añadir alumno</h3>

        <form th:action="@{/alumno/add}" th:object="${alumno}" method="post">
            <div class="border">
                <div class="row g-3 m-2">
                    <div class="col-md-6">
                        <label class="form-label">Nombre: <input type="text" th:field="*{nombre}" class="form-control" /></label>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label">Apellido: <input type="text" th:field="*{apellido}" class="form-control" /></label>
                    </div>
                </div>
            </div>
            <div class="border">
                <div class="row g-3 m-2">
                    <div class="col-md-6">
                        <label class="form-label">Email: <input type="email" th:field="*{email}" class="form-control" /></label>
                    </div>
                </div>
            </div>
            <div class="border">
                <div class="row g-3 m-2">
                    <div class="col-md-6">
                        <label class="form-label">Nombre de usuario: <input type="text" th:field="*{username}" class="form-control" /></label>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label">Contraseña: <input type="password" th:field="*{password}" class="form-control" /></label>
                    </div>
                </div>
            </div>
            <div class="border">
                <div class="row g-3 m-2">
                    <div class="col-md-6">
                        <label class="form-label">Telefono: <input type="text" th:field="*{telefono}"
                            class="form-control" required/></label>
                    </div>
                </div>
            </div>
            <div class="border">
                <div class="row g-3 m-2">
                    <div class="col-md-6">
                        <button type="submit" class="btn btn-primary">Guardar</button>
                        <a href="/alumno" class="btn btn-danger">Cancelar</a>
                    </div>
                </div>
            </div>
        </form>

    </div>

    <div th:replace="~{fragments/general.html :: footer}"></div>

</body>

</html>

```
### alumno / delete
```html
    <!DOCTYPE html>
<html lang="es">

<head>
    <title>Gestión Academica: Eliminar un alumno</title>
    <th:block th:replace="~{fragments/general.html :: headerfiles}"></th:block>
</head>

<body>

    <div th:replace="~{fragments/general.html :: navigation}"> </div>

    <div class="container">
        <h3>Eliminar alumno</h3>

        <form th:action="|/alumno/delete/${alumno.id}|" th:object="${alumno}" method="post">
            <div class="border">
                <div class="row g-3 m-2">
                    <div class="col-md-6">
                        <label class="form-label">Nombre: </label>
                        <input type="text" th:field="*{nombre}" class="form-control" disabled/>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label">Apellido: </label>
                        <input type="text" th:field="*{apellido}" class="form-control" disabled />
                    </div>
                </div>
            </div>
            <div class="border">
                <div class="row g-3 m-2">
                    <div class="col-md-6">
                        <label class="form-label">Email: </label>
                        <input type="email" th:field="*{email}" class="form-control" disabled/>
                    </div>
                </div>
            </div>
            <div class="border">
                <div class="row g-3 m-2">
                    <div class="col-md-6">
                        <label class="form-label">Nombre de usuario: </label>
                        <input type="text" th:field="*{username}" class="form-control" disabled/>
                    </div>
                </div>
            </div>
            <div class="border">
                <div class="row g-3 m-2">
                    <div class="col-md-6">
                        <label class="form-label">Telefono: </label>
                            <input type="text" th:field="*{telefono}" class="form-control" disabled />
                    </div>
                </div>
            </div>
            <div class="border">
                <div class="row g-3 m-2">
                    <div class="col-md-6">
                        <button type="submit" class="btn btn-danger">Eliminar</button>
                        <a href="/alumno" class="btn btn-primary">Cancelar</a>
                    </div>
                </div>
            </div>
        </form>

    </div>

    <div th:replace="~{fragments/general.html :: footer}"></div>

</body>

</html>

```

y asi con todas las plantillas necesarias. 
Al igual que realizamos todo esto para la entidad de *Alumnos*, hay que realizarla para *Profesores* y *Asignaturas* de la forma correcta

## Import
Importaremos datos de forma auutomatica en la base de datos para facilitar el manejo de estos: 
La contraseña para todos ellos será 'clave'

```sql
INSERT INTO `gestor` (`id`, `username`, `password`) VALUES (1, 'gestor', '$2a$10$.nlNNZl4VJVbiJUxLtOd5e8VKE/jv7EyBXqCSKVYSKEu.T4WT5Z7e'); 

INSERT INTO `alumno` (`id`, `username`, `password`, `nombre`, `apellido`, `telefono`, `email`) VALUES (1, 'javi', '$2a$10$.nlNNZl4VJVbiJUxLtOd5e8VKE/jv7EyBXqCSKVYSKEu.T4WT5Z7e', 'Javi', 'Redondo', '699699699', 'javiRedondo@example.es'); 

INSERT INTO `profesor` (`id`, `username`, `password`, `nombre`, `apellido`, `email`) VALUES (1, 'grego', '$2a$10$.nlNNZl4VJVbiJUxLtOd5e8VKE/jv7EyBXqCSKVYSKEu.T4WT5Z7e', 'Gregorio', 'Godoy', 'grego@example.es'); 

INSERT INTO `asignatura` (`id`, `nombre`, `curso`, `ciclo`) VALUES (1, 'SGe', 2, 'DAM');
INSERT INTO `asignatura` (`id`, `nombre`, `curso`, `ciclo`) VALUES (2, 'Acceso A Datos', 2, 'DAM');
INSERT INTO `asignatura` (`id`, `nombre`, `curso`, `ciclo`) VALUES (3, 'PsP', 2, 'DAM');


INSERT INTO `usuario` (`id`, `username`, `password`, `authority`, `alumno_id`) VALUES (1, 'javi', '$2a$10$.nlNNZl4VJVbiJUxLtOd5e8VKE/jv7EyBXqCSKVYSKEu.T4WT5Z7e', 'ALUMNO', 1); 
INSERT INTO `usuario` (`id`, `username`, `password`, `authority`, `profesor_id`) VALUES (2, 'grego', '$2a$10$.nlNNZl4VJVbiJUxLtOd5e8VKE/jv7EyBXqCSKVYSKEu.T4WT5Z7e', 'PROFESOR', 1); 
INSERT INTO `usuario` (`id`, `username`, `password`, `authority`, `gestor_id`) VALUES (3, 'gestor', '$2a$10$.nlNNZl4VJVbiJUxLtOd5e8VKE/jv7EyBXqCSKVYSKEu.T4WT5Z7e', 'GESTOR', 1); 
```

y ya podremos hacer login en nuestra pagina con los datos introducidos en el import