DROP SCHEMA IF EXISTS bbdd_santuario CASCADE;


-- Crear esquema y usarlo
CREATE SCHEMA IF NOT EXISTS bbdd_santuario;
SET search_path TO bbdd_santuario;

-- Tabla: Usuarios
CREATE TABLE Usuarios (
                        id SERIAL PRIMARY KEY,
                        email VARCHAR(100) NOT NULL UNIQUE,
                        nombre VARCHAR(50) NOT NULL UNIQUE,
                        contraseña VARCHAR(255) NOT NULL,
                        es_admin BOOLEAN DEFAULT FALSE NOT NULL
);

-- Tabla: Cualidades
CREATE TABLE Cualidades (
                          id_usuario INT PRIMARY KEY,
                          foto VARCHAR(255),
                          genero CHAR(1) CHECK (genero IN ('M', 'F')) NOT NULL,
                          edad VARCHAR(20) CHECK (edad IN ('cachorro', 'adulto', 'senior')) NOT NULL,
                          raza VARCHAR(50) NOT NULL,
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
                      FOREIGN KEY (id_seguidor) REFERENCES Usuarios(id) ON DELETE CASCADE,
                      FOREIGN KEY (id_seguido) REFERENCES Usuarios(id) ON DELETE CASCADE
);

-- Tabla: Chats
CREATE TABLE Chats (
                     id SERIAL PRIMARY KEY,
                     nombre VARCHAR(100) DEFAULT NULL,
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
                        contenido TEXT NOT NULL CHECK (contenido <> ''),
                        fecha TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        CONSTRAINT fk_chat_mensaje FOREIGN KEY (id_chat) REFERENCES Chats(id) ON DELETE CASCADE,
                        CONSTRAINT fk_emisor FOREIGN KEY (id_emisor) REFERENCES Usuarios(id) ON DELETE CASCADE
);

-- Tabla: Publicaciones
CREATE TABLE Publicaciones (
                             id SERIAL PRIMARY KEY,
                             texto TEXT,
                             imagen VARCHAR(255),
                             id_creador INT NOT NULL,
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
                           fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           CONSTRAINT fk_publicacion_comentario FOREIGN KEY (id_publicacion) REFERENCES Publicaciones(id) ON DELETE CASCADE,
                           CONSTRAINT fk_usuario_comentario FOREIGN KEY (id_usuario) REFERENCES Usuarios(id) ON DELETE CASCADE
);

-- Inserts de ejemplo
INSERT INTO Usuarios (email, nombre, contraseña, es_admin) VALUES
                                                             ('admin@example.com', 'AdminUser', 'hashed_password_1', TRUE),
                                                             ('user1@example.com', 'JohnDoe', 'hashed_password_2', FALSE),
                                                             ('user2@example.com', 'JaneSmith', 'hashed_password_3', FALSE);

INSERT INTO Chats (nombre, tipo) VALUES
                                   (NULL, 'privado'),
                                   ('Grupo de Amigos', 'grupal');

INSERT INTO Miembros_Chat (id_chat, id_usuario) VALUES
                                                  (1, 2),
                                                  (1, 3),
                                                  (2, 2),
                                                  (2, 3);

INSERT INTO Mensajes (id_chat, id_emisor, contenido) VALUES
                                                       (1, 2, 'Hola, ¿cómo están?'),
                                                       (1, 3, '¡Todo bien! ¿Y tú?'),
                                                       (2, 2, 'Bienvenidos al grupo.'),
                                                       (2, 3, 'Gracias por invitarme.');

INSERT INTO Publicaciones (texto, imagen, id_creador) VALUES
                                                        ('Mi primera publicación.', NULL, 2),
                                                        ('Un día soleado.', 'https://example.com/sunny.jpg', 3);

INSERT INTO Likes (id_publicacion, id_usuario) VALUES
                                                 (1, 3),
                                                 (2, 2);

INSERT INTO Comentarios (id_publicacion, id_usuario, texto) VALUES
                                                              (1, 3, '¡Qué buena publicación!'),
                                                              (2, 2, 'Hermoso día.');


