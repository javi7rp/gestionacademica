INSERT INTO `rol` (`id`, `nombre`) VALUES (1,	'alumno');
INSERT INTO `rol` (`id`, `nombre`) VALUES  (2,	'profesor');
INSERT INTO `rol` (`id`, `nombre`) VALUES  (3,	'gestor');


INSERT INTO `telefono` (`id`, `numero`, `codigo_pais`, `usuario_id`) VALUES (1, 123456789,	34, 1);
INSERT INTO `telefono` (`id`, `numero`, `codigo_pais`, `usuario_id`) VALUES (2, 987654321,	34, 1);
INSERT INTO `telefono` (`id`, `numero`, `codigo_pais`, `usuario_id`) VALUES (3, 555123456,	34, 2);


INSERT INTO `Asignatura` (`nombre`, `curso`, `ciclo`) VALUES 
('Matemáticas', 1, 'Ciencias'),
('Física', 2, 'Ciencias'),
('Historia', 1, 'Humanidades');

-- Insertar datos de ejemplo para la tabla de alumnos
INSERT INTO `Alumno` (`username`, `password`, `nombre`, `apellido`, `enabled`, `email`) VALUES 
('alumno1', 'password1', 'Juan', 'García', 1, 'juan.garcia@email.com'),
('alumno2', 'password2', 'María', 'López', 1, 'maria.lopez@email.com');

-- Insertar datos de ejemplo para la tabla de profesores
INSERT INTO `Profesor` (`username`, `password`, `nombre`, `apellido`, `email`) VALUES 
('profesor1', 'password1', 'Pedro', 'Martínez', 'pedro.martinez@email.com'),
('profesor2', 'password2', 'Ana', 'Gómez', 'ana.gomez@email.com');

-- Insertar datos de ejemplo para la tabla de gestores
INSERT INTO `Gestor` (`usuario`, `password`) VALUES 
('gestor1', 'password1'),
('gestor2', 'password2');