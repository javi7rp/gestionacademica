INSERT INTO `gestor` (`id`, `username`, `password`) VALUES 
(1, 'gestor', '$2a$10$SMURoLEmDeDc8hcQdS7HY.yTlukjYIjr3C71UnIVTOr2tirUy0xZi'); --password: gestor


INSERT INTO `alumno` (`id`, `username`, `password`, `nombre`, `apellidos`, `telefono`, `email`) VALUES 
(1, 'javi', '$2a$10$TJQy9HvrU9Wsh4/kZ2cdD.kHHNDw.K6zDj37ghRJxHTXHPjiU8bJy', 'Javi', 'Redondo', '699699699', 'javiRedondo@example.es'); --password: javi

INSERT INTO `profesor` (`id`, `username`, `password`, `nombre`, `apellidos`, `email`) VALUES
 (1, 'grego', '$2a$10$FfDNdOq5Iwq6te7Z7cFzve0yrOb9J4k8LX9cX4MEo20xtJLuKdsle', 'Gregorio', 'Godoy', 'grego@example.es'); --password: 123

INSERT INTO `asignatura` (`id`, `nombre`, `curso`, `ciclo`) VALUES 
(1, 'SGe', 'Segundo', 'DAM');
INSERT INTO `asignatura` (`id`, `nombre`, `curso`, `ciclo`) VALUES 
(2, 'Acceso A Datos', 'Segundo', 'DAM');
INSERT INTO `asignatura` (`id`, `nombre`, `curso`, `ciclo`) VALUES 
(3, 'PsP', 'Segundo', 'DAM');


INSERT INTO `usuario` (`id`, `username`, `password`, `authority`, `alumno_id`) VALUES 
(1, 'javi', '$2a$10$.nlNNZl4VJVbiJUxLtOd5e8VKE/jv7EyBXqCSKVYSKEu.T4WT5Z7e', 'ALUMNO', 1); --password: javi
INSERT INTO `usuario` (`id`, `username`, `password`, `authority`, `profesor_id`) VALUES 
(2, 'grego', '$2a$10$FfDNdOq5Iwq6te7Z7cFzve0yrOb9J4k8LX9cX4MEo20xtJLuKdsle', 'PROFESOR', 1); --password: 123
INSERT INTO `usuario` (`id`, `username`, `password`, `authority`, `gestor_id`) VALUES 
(3, 'gestor', ' $2a$10$SMURoLEmDeDc8hcQdS7HY.yTlukjYIjr3C71UnIVTOr2tirUy0xZi', 'GESTOR', 1); --password: gestor
