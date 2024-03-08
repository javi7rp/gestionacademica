INSERT INTO `gestor` (`id`, `username`, `password`) VALUES (1, 'gestor', '$$2a$10$.nlNNZl4VJVbiJUxLtOd5e8VKE/jv7EyBXqCSKVYSKEu.T4WT5Z7e'); 

INSERT INTO `alumno` (`id`, `username`, `password`, `nombre`, `apellido`, `telefono`, `email`) VALUES (1, 'javi', '$2a$10$TJQy9HvrU9Wsh4/kZ2cdD.kHHNDw.K6zDj37ghRJxHTXHPjiU8bJy', 'Javi', 'Redondo', '699699699', 'javiRedondo@example.es'); 

INSERT INTO `profesor` (`id`, `username`, `password`, `nombre`, `apellido`, `email`) VALUES (1, 'grego', '$2a$10$FfDNdOq5Iwq6te7Z7cFzve0yrOb9J4k8LX9cX4MEo20xtJLuKdsle', 'Gregorio', 'Godoy', 'grego@example.es'); 

INSERT INTO `asignatura` (`id`, `nombre`, `curso`, `ciclo`) VALUES (1, 'SGe', 2, 'DAM');
INSERT INTO `asignatura` (`id`, `nombre`, `curso`, `ciclo`) VALUES (2, 'Acceso A Datos', 2, 'DAM');
INSERT INTO `asignatura` (`id`, `nombre`, `curso`, `ciclo`) VALUES (3, 'PsP', 2, 'DAM');


INSERT INTO `usuario` (`id`, `username`, `password`, `authority`, `alumno_id`) VALUES (1, 'javi', '$2a$10$.nlNNZl4VJVbiJUxLtOd5e8VKE/jv7EyBXqCSKVYSKEu.T4WT5Z7e', 'ALUMNO', 1); 
INSERT INTO `usuario` (`id`, `username`, `password`, `authority`, `profesor_id`) VALUES (2, 'grego', '$2a$10$FfDNdOq5Iwq6te7Z7cFzve0yrOb9J4k8LX9cX4MEo20xtJLuKdsle', 'PROFESOR', 1); 
INSERT INTO `usuario` (`id`, `username`, `password`, `authority`, `gestor_id`) VALUES (3, 'gestor', '$2a$10$.nlNNZl4VJVbiJUxLtOd5e8VKE/jv7EyBXqCSKVYSKEu.T4WT5Z7e', 'GESTOR', 1); 

