create schema if not exists bbdd_santuario;
set search_path to bbdd_santuario;


-- Tabla: Usuarios
CREATE TABLE Usuarios (
	id SERIAL PRIMARY KEY,
	email VARCHAR(100) NOT NULL UNIQUE,
	nombre VARCHAR(50) NOT NULL UNIQUE,
	contraseña VARCHAR(255) NOT null,
	esAdmin BOOLEAN DEFAULT FALSE NOT NULL
 );




-- Tabla: Cualidades
CREATE TABLE Cualidades (
	id_usuario INT PRIMARY KEY,
	foto VARCHAR(255), -- URL o ruta a la foto
	genero CHAR(1) CHECK (genero IN ('M', 'F')) NOT NULL,
	edad VARCHAR(20) CHECK (edad IN ('cachorro', 'adulto', 'senior')) NOT NULL,
	raza VARCHAR(50) NOT NULL CHECK (raza IN ( 
'Airedale Terrier', 'Akita', 'Akita Americano', 'American Bully', 'American Pit Bull Terrier', 'American Staffordshire Terrier', 'Azawakh', 'Barbet', 'Basenji', 'Basset Azul de Gascuña', 'Basset Hound', 'Beagle', 'Beauceron', 'Bedlington Terrier', 'Bichón Frisé', 'Bichón Habanero', 'Bichón Maltés', 'Billy', 'Bloodhound', 'Bóxer', 'Boyero Appenzeller', 'Boyero Australiano', 'Boyero de Berna', 'Boyero de Entlebuch', 'Braco Alemán de Pelo Corto', 'Braco Alemán de Pelo Duro', 'Braco de Auvernia', 'Braco de Saint-Germain', 'Braco Francés', 'Braco Italiano', 'Bull Terrier', 'Bull Terrier Miniatura', 'Bulldog Americano', 'Bulldog Francés', 'Bulldog Inglés', 'Bullmastiff', 'Cairn Terrier', 'Cane Corso', 'Caniche', 'Carlino (Pug)', 'Cavalier King Charles Spaniel', 'Chesapeake Bay Retriever', 'Chihuahua', 'Chin Japonés', 'Chow Chow', 'Cirneco del Etna', 'Clumber Spaniel', 'Cocker Spaniel Americano', 'Cocker Spaniel Inglés', 'Collie', 'Coonhound Negro y Fuego', 'Coonhound Redbone', 'Corgi Galés de Cardigan', 'Corgi Galés de Pembroke', 'Coton de Tuléar', 'Dachshund (Teckel)', 'Dálmata', 'Dandie Dinmont Terrier', 'Dingo Australiano', 'Dogo Alemán (Gran Danés)', 'Dogo Argentino', 'Dogo de Burdeos', 'Dóberman', 'Elkhound Noruego', 'Eurasier', 'Fila Brasileño', 'Fox Terrier de Pelo Liso', 'Fox Terrier de Pelo Duro', 'Foxhound Americano', 'Foxhound Inglés', 'Galgo Afgano', 'Galgo Español', 'Galgo Italiano', 'Golden Retriever', 'Gordon Setter', 'Gran Pirineo', 'Greyhound (Galgo Inglés)', 'Grifón Azul de Gascuña', 'Grifón de Bruselas', 'Grifón de Pelo Duro', 'Husky Siberiano', 'Jack Russell Terrier', 'Keeshond', 'Komondor', 'Kuvasz', 'Labrador Retriever', 'Lagotto Romagnolo', 'Lhasa Apso', 'Lebrel Escocés (Deerhound)', 'Lebrel Irlandés (Irish Wolfhound)', 'Leonberger', 'Lowchen', 'Malamute de Alaska', 'Maltés', 'Mastín Español', 'Mastín Napolitano', 'Mastín del Pirineo', 'Mastín Tibetano', 'Norfolk Terrier', 'Norwich Terrier', 'Otterhound', 'Papillón', 'Pastor Alemán', 'Pastor Australiano', 'Pastor Belga Malinois', 'Pastor Belga Tervuren', 'Pastor Blanco Suizo', 'Pastor de Asia Central', 'Pastor de Beauce', 'Pastor de los Pirineos', 'Pastor Ganadero Australiano', 'Pastor Holandés', 'Pequeño Brabanzón', 'Perdiguero de Burgos', 'Perdiguero Portugués', 'Perro de Agua Americano', 'Perro de Agua Español', 'Perro de Agua Portugués', 'Perro de Canaan', 'Perro de Montaña de los Pirineos', 'Perro Esquimal Americano', 'Perro Lobo Checoslovaco', 'Perro Lobo de Saarloos', 'Pinscher Alemán', 'Pinscher Miniatura', 'Pitbull Terrier', 'Podenco Andaluz', 'Podenco Canario', 'Podenco Ibicenco', 'Pomerania', 'Presa Canario', 'Pudelpointer', 'Puli', 'Retriever de Pelo Liso', 'Retriever de Pelo Rizado', 'Ridgeback de Rodesia', 'Rottweiler', 'Sabueso Español', 'Sabueso Italiano', 'Samoyedo', 'San Bernardo', 'Schipperke', 'Schnauzer Gigante', 'Schnauzer Mediano', 'Schnauzer Miniatura', 'Setter Gordon', 'Setter Inglés', 'Setter Irlandés', 'Shar Pei', 'Shiba Inu', 'Shih Tzu', 'Skye Terrier', 'Spaniel Azul Picardo', 'Spaniel Bretón', 'Spaniel de Campo', 'Spaniel de Sussex', 'Spaniel Francés', 'Spaniel Tibetano', 'Spinone Italiano', 'Springer Spaniel Galés', 'Springer Spaniel Inglés', 'Staffordshire Bull Terrier', 'Terranova', 'Terrier Australiano', 'Terrier Escocés', 'Terrier Irlandés', 'Terrier Jack Russell', 'Terrier Tibetano', 'Tosa Inu', 'Vizsla (Braco Húngaro)', 'Volpino Italiano', 'Weimaraner', 'West Highland White Terrier', 'Whippet', 'Yorkshire Terrier' 
)),


	nivel_energia INT CHECK (nivel_energia >= 1 AND nivel_energia <= 10),
	sociabilidad INT CHECK (sociabilidad >= 1 AND sociabilidad <= 10),
	tamaño INT CHECK (tamaño >= 1 AND tamaño <= 10),
	tolerancia_especies INT CHECK (tolerancia_especies >= 1 AND tolerancia_especies <= 10),
	nivel_dominancia INT CHECK (nivel_dominancia >= 1 AND nivel_dominancia <= 10),
	tendencia_juego INT CHECK (tendencia_juego >= 1 AND tendencia_juego <= 10),
	temperamento INT CHECK (temperamento >= 1 AND temperamento <= 10),
	experiencia INT CHECK (experiencia >= 1 AND experiencia <= 10),
	territorialidad INT CHECK (territorialidad >= 1 AND territorialidad <= 10),
	
	
CONSTRAINT fk_usuario FOREIGN KEY (id_usuario) REFERENCES Usuarios(id) ON DELETE CASCADE
);




-- Tabla: Follows
CREATE TABLE Follows(
id_seguidor INT NOT NULL,
id_seguido INT NOT NULL,

PRIMARY KEY (id_seguidor, id_seguido), 
FOREIGN KEY (id_seguidor) REFERENCES Usuarios(id) ON DELETE CASCADE, FOREIGN KEY (id_seguido) REFERENCES Usuarios(id) ON DELETE CASCADE 
);



-------------------------------------------------------------------------------




-- Tabla: Chats
CREATE TABLE Chats (
	id SERIAL PRIMARY KEY,
	nombre VARCHAR(100) DEFAULT NULL, -- Opcional: Solo para chats grupales
	tipo VARCHAR(20) NOT NULL CHECK (tipo IN ('privado', 'grupal'))
);



-- Tabla: Miembros_Chat
CREATE TABLE Miembros_Chat (
	id_chat INT NOT NULL,
	id_usuario INT NOT NULL,
	PRIMARY KEY (id_chat, id_usuario),
	
	CONSTRAINT fk_chat FOREIGN KEY (id_chat) REFERENCES Chats(id) ON DELETE CASCADE,
	CONSTRAINT fk_usuario_chat FOREIGN KEY (id_usuario) REFERENCES Usuarios(id) ON DELETE CASCADE
);



-- Tabla: Mensajes
CREATE TABLE Mensajes (
	id SERIAL PRIMARY KEY,
	id_chat INT NOT NULL,
	id_emisor INT NOT NULL,
	contenido TEXT NOT NULL CHECK (contenido <> ''), -- Asegurar que no esté vacío
	fecha TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

CONSTRAINT fk_chat_mensaje FOREIGN KEY (id_chat) REFERENCES Chats(id) ON DELETE CASCADE,
CONSTRAINT fk_emisor FOREIGN KEY (id_emisor) REFERENCES Usuarios(id) ON DELETE CASCADE 
);




-------------------------------------------------------------------------------




-- Tabla: Publicaciones
CREATE TABLE Publicaciones (
	id SERIAL PRIMARY KEY,
	texto TEXT,
	imagen VARCHAR(255),
	id_creador INT NOT NULL,
	fecha TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	
	CONSTRAINT fk_creador FOREIGN KEY (id_creador) REFERENCES Usuarios(id) ON DELETE CASCADE
);




-- Tabla: Likes 
CREATE TABLE Likes (
id_publicacion INT NOT NULL,
id_usuario INT NOT NULL, 

PRIMARY KEY (id_publicacion, id_usuario), 
CONSTRAINT fk_publicacion FOREIGN KEY (id_publicacion) REFERENCES Publicaciones(id) ON DELETE CASCADE, 
CONSTRAINT fk_usuario_like FOREIGN KEY (id_usuario) REFERENCES Usuarios(id) ON DELETE CASCADE
); 




-- Tabla: Comentarios 
CREATE TABLE Comentarios (
id SERIAL PRIMARY KEY, 
id_publicacion INT NOT NULL,
id_usuario INT NOT NULL, 
texto TEXT NOT NULL, 

CONSTRAINT fk_publicacion_comentario FOREIGN KEY (id_publicacion) REFERENCES Publicaciones(id) ON DELETE CASCADE, 
CONSTRAINT fk_usuario_comentario FOREIGN KEY (id_usuario) REFERENCES Usuarios(id) ON DELETE CASCADE 
);











----------------------------------------- datos ----------------------------------------------------


-- Insertar usuarios
INSERT INTO Usuarios (email, nombre, contraseña, esAdmin) VALUES
('admin@example.com', 'AdminUser', 'hashed_password_1', TRUE),
('user1@example.com', 'JohnDoe', 'hashed_password_2', FALSE),
('user2@example.com', 'JaneSmith', 'hashed_password_3', FALSE),
('user3@example.com', 'MaxPower', 'hashed_password_4', FALSE),
('user4@example.com', 'LunaMoon', 'hashed_password_5', FALSE);

-- Insertar cualidades
INSERT INTO Cualidades (id_usuario, foto, genero, edad, raza, nivel_energia, sociabilidad, tamaño, tolerancia_especies, nivel_dominancia, tendencia_juego, temperamento, experiencia, territorialidad)
VALUES
(2, 'https://example.com/photo1.jpg', 'M', 'adulto', 'Golden Retriever', 8, 9, 7, 8, 5, 7, 9, 6, 5),
(3, 'https://example.com/photo2.jpg', 'F', 'cachorro', 'Beagle', 6, 8, 4, 7, 4, 6, 7, 5, 3),
(4, 'https://example.com/photo3.jpg', 'M', 'senior', 'Bulldog Inglés', 3, 7, 6, 5, 2, 4, 6, 4, 2),
(5, 'https://example.com/photo4.jpg', 'F', 'adulto', 'Husky Siberiano', 9, 8, 8, 9, 6, 9, 8, 7, 6);

-- Insertar follows
INSERT INTO Follows (id_seguidor, id_seguido) VALUES
(2, 3),
(2, 4),
(3, 2),
(4, 5),
(5, 2);

-- Insertar chats
INSERT INTO Chats (nombre, tipo) VALUES
(NULL, 'privado'),
('Grupo de Prueba', 'grupal');

-- Insertar miembros de chat
INSERT INTO Miembros_Chat (id_chat, id_usuario) VALUES
(1, 2),
(1, 3),
(2, 2),
(2, 3),
(2, 4);

-- Insertar mensajes
INSERT INTO Mensajes (id_chat, id_emisor, contenido) VALUES
(1, 2, 'Hola, ¿cómo estás?'),
(1, 3, '¡Todo bien! ¿Y tú?'),
(2, 2, 'Bienvenidos al grupo.'),
(2, 4, 'Gracias por invitarme.');

-- Insertar publicaciones
INSERT INTO Publicaciones (texto, imagen, id_creador) VALUES
('Mi primera publicación.', NULL, 2),
('Un día soleado.', 'https://example.com/sunny.jpg', 3),
('Disfrutando de un paseo.', 'https://example.com/walk.jpg', 4);

-- Insertar likes
INSERT INTO Likes (id_publicacion, id_usuario) VALUES
(1, 3),
(2, 4),
(3, 2);

-- Insertar comentarios
INSERT INTO Comentarios (id_publicacion, id_usuario, texto) VALUES
(1, 4, '¡Qué buena publicación!'),
(2, 2, 'Hermoso día.'),
(3, 3, 'Me encanta esa foto.');















