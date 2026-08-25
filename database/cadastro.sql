CREATE DATABASE IF NOT EXISTS cadastro_cli
  CHARACTER SET utf8
  COLLATE utf8_general_ci;

USE cadastro_cli;

SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS cliente;
SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE cliente (
    id INT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(120) NOT NULL,
    cpf VARCHAR(14) NOT NULL,
    email VARCHAR(120) NOT NULL,
    data_nascimento DATE NOT NULL,
    senha_hash CHAR(64) NOT NULL,
    senha_salt CHAR(32) NOT NULL,
    ativo TINYINT(1) NOT NULL DEFAULT 1,

    PRIMARY KEY (id),
    UNIQUE KEY uk_cliente_cpf (cpf),
    UNIQUE KEY uk_cliente_email (email),
    KEY ix_cliente_nome (nome)
) ENGINE=InnoDB;