CREATE TABLE `agenda` (
  `cdagenda` int(11) NOT NULL AUTO_INCREMENT,
  `cdatividade` int(11) NOT NULL,
  `titulo` varchar(200) NOT NULL,
  `cdusuario` int(11) NOT NULL,
  `cdgrupo` int(11) NOT NULL,
  `cdstatus` int(11) NOT NULL,
  `responsavel` varchar(200) NOT NULL,
  `processo` varchar(100) DEFAULT NULL,
  `dtcompromisso` datetime NOT NULL,
  PRIMARY KEY (`cdagenda`),
  KEY `fk_atividade_agenda` (`cdatividade`),
  KEY `fk_grupo_agenda` (`cdgrupo`),
  KEY `fk_usuario_agenda` (`cdusuario`),
  CONSTRAINT `fk_atividade_agenda` FOREIGN KEY (`cdatividade`) REFERENCES `atividade` (`cdatividade`),
  CONSTRAINT `fk_grupo_agenda` FOREIGN KEY (`cdgrupo`) REFERENCES `grupo_trabalho` (`CDGRUPO`),
  CONSTRAINT `fk_usuario_agenda` FOREIGN KEY (`cdusuario`) REFERENCES `usuario` (`cdusuario`)
) ENGINE=InnoDB AUTO_INCREMENT=5201 DEFAULT CHARSET=latin1;
