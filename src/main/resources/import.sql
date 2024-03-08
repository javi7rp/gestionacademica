INSERT INTO `asignatura` (`id`, `nombre`, `curso`, `ciclo`) VALUES 
(1,'Matemáticas', 1, 'Ciencias'),
(2,'Física', 2, 'Ciencias'),
(3,'Historia', 1, 'Humanidades');

-- Insertar datos de ejemplo para la tabla de alumnos
INSERT INTO `alumno` (`id`, `username`, `password`, `nombre`, `apellido`, `telefono`, `email`) VALUES 
('alumno1', 'password1', 'Juan', 'García', 1, 'juan.garcia@email.com'),
('alumno2', 'password2', 'Jose', 'Juan', 1, 'juan.garcia@email.com'),
('alumno3', 'password3', 'Perico', 'Palotes', 1, 'juan.garcia@email.com')
