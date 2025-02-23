-- Active: 1740248261053@@dpg-cut0ujdsvqrc73e2vodg-a.frankfurt-postgres.render.com@5432@bbdd_santuario_ll1a

-- Eliminar el esquema si existe
DROP SCHEMA IF EXISTS bbdd_santuario CASCADE;

-- Crear el esquema y usarlo
CREATE SCHEMA IF NOT EXISTS bbdd_santuario;
SET search_path TO bbdd_santuario;

-- Tabla: Usuarios
CREATE TABLE Usuarios (
                        id SERIAL PRIMARY KEY,
                        email VARCHAR(100) NOT NULL UNIQUE,
                        nombre VARCHAR(50) NOT NULL UNIQUE,
                        contraseña VARCHAR(255) NOT NULL,
                        es_admin BOOLEAN DEFAULT FALSE NOT NULL,
                        verificado BOOLEAN DEFAULT FALSE NOT NULL,
                        baneado BOOLEAN DEFAULT FALSE NOT NULL
);

-- Tabla: Cualidades
CREATE TABLE Cualidades (
                          id_usuario INT PRIMARY KEY,
                          foto VARCHAR(255),
                          genero CHAR(1) CHECK (genero IN ('M', 'F')) NOT NULL,
                          edad VARCHAR(20) CHECK (edad IN ('cachorro', 'adulto', 'senior')) NOT NULL,
                          raza VARCHAR(50) NOT NULL,
                          nivel_energia INT CHECK (nivel_energia BETWEEN 1 AND 10),
                          sociabilidad INT CHECK (sociabilidad BETWEEN 1 AND 10),
                          tamaño INT CHECK (tamaño BETWEEN 1 AND 10),
                          tolerancia_especies INT CHECK (tolerancia_especies BETWEEN 1 AND 10),
                          nivel_dominancia INT CHECK (nivel_dominancia BETWEEN 1 AND 10),
                          tendencia_juego INT CHECK (tendencia_juego BETWEEN 1 AND 10),
                          temperamento INT CHECK (temperamento BETWEEN 1 AND 10),
                          experiencia INT CHECK (experiencia BETWEEN 1 AND 10),
                          territorialidad INT CHECK (territorialidad BETWEEN 1 AND 10),
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
                             FOREIGN KEY (id_chat) REFERENCES Chats(id) ON DELETE CASCADE,
                             FOREIGN KEY (id_usuario) REFERENCES Usuarios(id) ON DELETE CASCADE
);

-- Tabla: Mensajes
CREATE TABLE Mensajes (
                        id SERIAL PRIMARY KEY,
                        id_chat INT NOT NULL,
                        id_emisor INT NOT NULL,
                        contenido TEXT NOT NULL CHECK (contenido <> ''),
                        fecha TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (id_chat) REFERENCES Chats(id) ON DELETE CASCADE,
                        FOREIGN KEY (id_emisor) REFERENCES Usuarios(id) ON DELETE CASCADE
);

-- Tabla: Publicaciones
CREATE TABLE Publicaciones (
                             id SERIAL PRIMARY KEY,
                             texto TEXT,
                             imagen VARCHAR(255),
                             id_creador INT NOT NULL,
                             FOREIGN KEY (id_creador) REFERENCES Usuarios(id) ON DELETE CASCADE
);

-- Tabla: Likes
CREATE TABLE Likes (
                     id_publicacion INT NOT NULL,
                     id_usuario INT NOT NULL,
                     PRIMARY KEY (id_publicacion, id_usuario),
                     FOREIGN KEY (id_publicacion) REFERENCES Publicaciones(id) ON DELETE CASCADE,
                     FOREIGN KEY (id_usuario) REFERENCES Usuarios(id) ON DELETE CASCADE
);

-- Tabla: Comentarios
CREATE TABLE Comentarios (
                           id SERIAL PRIMARY KEY,
                           id_publicacion INT NOT NULL,
                           id_usuario INT NOT NULL,
                           texto TEXT NOT NULL,
                           fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           FOREIGN KEY (id_publicacion) REFERENCES Publicaciones(id) ON DELETE CASCADE,
                           FOREIGN KEY (id_usuario) REFERENCES Usuarios(id) ON DELETE CASCADE
);
