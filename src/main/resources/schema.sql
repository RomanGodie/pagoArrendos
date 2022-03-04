CREATE TABLE pagos (
    id INT NOT NULL auto_increment,
    documentoIdentificacionArrendatario INT (50) NOT NULL,
    codigoInmueble VARCHAR (50) NOT NULL,
    valorPagado INT NOT NULL DEFAULT 0,
    fechaPago VARCHAR (50) NOT NULL,
    PRIMARY KEY (id)
);