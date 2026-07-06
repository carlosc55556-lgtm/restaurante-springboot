-- Clientes iniciales
INSERT INTO clientes (nombres, apellidos, dni, direccion, telefono, email, rol, password)
VALUES ('Juan', 'Pérez', '12345678', 'Av. Lima 123', '987654321', 'juan@correo.com', 'CLIENTE', '1234');

INSERT INTO clientes (nombres, apellidos, dni, direccion, telefono, email, rol, password)
VALUES ('María', 'Gómez', '87654321', 'Av. Perú 456', '912345678', 'maria@correo.com', 'MOZO', '1234');

INSERT INTO clientes (nombres, apellidos, dni, direccion, telefono, email, rol, password)
VALUES ('Carlos', 'Ramírez', '11223344', 'Av. Sur 789', '999888777', 'carlos@correo.com', 'ADMIN', '1234');

INSERT INTO clientes (nombres, apellidos, dni, direccion, telefono, email, rol, password)
VALUES ('Lucía', 'Fernández', '44556677', 'Av. Norte 321', '987123456', 'lucia@correo.com', 'JEFE_MOZOS', '1234');

-- Platos iniciales
INSERT INTO platos (nombre, descripcion, precio)
VALUES ('Ceviche', 'Plato típico peruano con pescado y limón', 25.00);

INSERT INTO platos (nombre, descripcion, precio)
VALUES ('Lomo Saltado', 'Carne salteada con papas y arroz', 30.00);

INSERT INTO platos (nombre, descripcion, precio)
VALUES ('Pollo a la Brasa', 'Pollo marinado y horneado con papas fritas', 28.00);

-- Reservas iniciales
INSERT INTO reservas (cliente_id, fechaHora, numeroPersonas)
VALUES (1, '2026-06-15 19:00:00', 4);

-- Ventas iniciales
INSERT INTO ventas (cliente_id, fecha, total)
VALUES (1, '2026-06-10 12:00:00', 55.00);
