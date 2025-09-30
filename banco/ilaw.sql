-- MySQL dump 10.13  Distrib 5.7.28, for Linux (x86_64)
--
-- Host: localhost    Database: ilaw
-- ------------------------------------------------------
-- Server version	5.7.28-0ubuntu0.19.04.2

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `area_atuacao`
--

DROP TABLE IF EXISTS `area_atuacao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `area_atuacao` (
  `cdarea` int(11) NOT NULL AUTO_INCREMENT,
  `noarea` varchar(100) NOT NULL,
  `cdempresa` int(11) NOT NULL,
  PRIMARY KEY (`cdarea`),
  KEY `fk_area_empresa` (`cdempresa`),
  CONSTRAINT `fk_area_empresa` FOREIGN KEY (`cdempresa`) REFERENCES `empresa` (`CDEMPRESA`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `area_atuacao`
--

LOCK TABLES `area_atuacao` WRITE;
/*!40000 ALTER TABLE `area_atuacao` DISABLE KEYS */;
INSERT INTO `area_atuacao` VALUES (1,'Cível',1),(2,'Trabalhista',1),(3,'Previdenciário',1),(4,'Criminal',1);
/*!40000 ALTER TABLE `area_atuacao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `arquivo_pessoa`
--

DROP TABLE IF EXISTS `arquivo_pessoa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `arquivo_pessoa` (
  `cdcontrole` int(11) NOT NULL AUTO_INCREMENT,
  `cdpessoa` int(11) NOT NULL,
  `noarquivo` varchar(200) NOT NULL,
  `desarquivo` varchar(200) DEFAULT NULL,
  `dtarquivo` date NOT NULL,
  `cdusuario` int(11) NOT NULL,
  PRIMARY KEY (`cdcontrole`),
  KEY `fk_arquivo_pessoa` (`cdpessoa`),
  KEY `fk_arquivo_usuario` (`cdusuario`),
  CONSTRAINT `fk_arquivo_pessoa` FOREIGN KEY (`cdpessoa`) REFERENCES `pessoa` (`cdpessoa`),
  CONSTRAINT `fk_arquivo_usuario` FOREIGN KEY (`cdusuario`) REFERENCES `usuario` (`cdusuario`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `arquivo_pessoa`
--

LOCK TABLES `arquivo_pessoa` WRITE;
/*!40000 ALTER TABLE `arquivo_pessoa` DISABLE KEYS */;
INSERT INTO `arquivo_pessoa` VALUES (3,21,'Captura de tela de 2020-06-19 17-40-57.png','','2022-02-05',8),(4,21,'Captura de tela de 2020-06-19 17-41-24.png','','2022-02-05',8),(5,21,'Captura de tela de 2020-06-19 17-54-24.png','','2022-02-05',8);
/*!40000 ALTER TABLE `arquivo_pessoa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `arquivo_pessoa_temp`
--

DROP TABLE IF EXISTS `arquivo_pessoa_temp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `arquivo_pessoa_temp` (
  `cdcontrole` int(11) NOT NULL AUTO_INCREMENT,
  `noarquivo` varchar(200) NOT NULL,
  `dsarquivo` varchar(200) DEFAULT NULL,
  `cdusuario` int(11) DEFAULT NULL,
  PRIMARY KEY (`cdcontrole`),
  KEY `fk_usuarioArq_temp` (`cdusuario`),
  CONSTRAINT `fk_usuarioArq_temp` FOREIGN KEY (`cdusuario`) REFERENCES `usuario` (`cdusuario`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `arquivo_pessoa_temp`
--

LOCK TABLES `arquivo_pessoa_temp` WRITE;
/*!40000 ALTER TABLE `arquivo_pessoa_temp` DISABLE KEYS */;
INSERT INTO `arquivo_pessoa_temp` VALUES (45,'Captura de tela de 2020-06-19 17-40-57.png','',8),(46,'Captura de tela de 2020-06-19 17-54-24.png','',8),(47,'Captura de tela de 2020-06-19 17-41-24.png','',8);
/*!40000 ALTER TABLE `arquivo_pessoa_temp` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `arquivo_processo`
--

DROP TABLE IF EXISTS `arquivo_processo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `arquivo_processo` (
  `cdarquivo` int(11) NOT NULL AUTO_INCREMENT,
  `cdprocesso` int(11) NOT NULL,
  `noarquivo` varchar(200) NOT NULL,
  `dsarquivo` varchar(200) DEFAULT NULL,
  `dtarquivo` date NOT NULL,
  `cdusuario` int(11) NOT NULL,
  PRIMARY KEY (`cdarquivo`),
  KEY `fk_arquivo_processo` (`cdprocesso`),
  KEY `fk_arquivo_usuario2` (`cdusuario`),
  CONSTRAINT `fk_arquivo_processo` FOREIGN KEY (`cdprocesso`) REFERENCES `processo` (`cdprocesso`),
  CONSTRAINT `fk_arquivo_usuario2` FOREIGN KEY (`cdusuario`) REFERENCES `usuario` (`cdusuario`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `arquivo_processo`
--

LOCK TABLES `arquivo_processo` WRITE;
/*!40000 ALTER TABLE `arquivo_processo` DISABLE KEYS */;
/*!40000 ALTER TABLE `arquivo_processo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cidade`
--

DROP TABLE IF EXISTS `cidade`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cidade` (
  `CDCIDADE` int(11) NOT NULL AUTO_INCREMENT,
  `NOCIDADE` varchar(100) NOT NULL,
  `CDESTADO` int(11) NOT NULL,
  PRIMARY KEY (`CDCIDADE`),
  KEY `fk_estado_cidade` (`CDESTADO`),
  CONSTRAINT `fk_estado_cidade` FOREIGN KEY (`CDESTADO`) REFERENCES `estado` (`CDESTADO`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cidade`
--

LOCK TABLES `cidade` WRITE;
/*!40000 ALTER TABLE `cidade` DISABLE KEYS */;
INSERT INTO `cidade` VALUES (2,'João Pessoa',1),(3,'Santa Rita',1);
/*!40000 ALTER TABLE `cidade` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `custas_processo`
--

DROP TABLE IF EXISTS `custas_processo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `custas_processo` (
  `cdcontrole` int(11) NOT NULL AUTO_INCREMENT,
  `cdprocesso` int(11) NOT NULL,
  `cdcustas` int(11) NOT NULL,
  `vlcustas` double NOT NULL,
  `tipo` varchar(1) NOT NULL,
  `dtpagamento` date NOT NULL,
  PRIMARY KEY (`cdcontrole`),
  KEY `fk_custas_processo` (`cdprocesso`),
  KEY `fk_custas_custa` (`cdcustas`),
  CONSTRAINT `fk_custas_custa` FOREIGN KEY (`cdcustas`) REFERENCES `tipo_custa` (`cdtipo`),
  CONSTRAINT `fk_custas_processo` FOREIGN KEY (`cdprocesso`) REFERENCES `processo` (`cdprocesso`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `custas_processo`
--

LOCK TABLES `custas_processo` WRITE;
/*!40000 ALTER TABLE `custas_processo` DISABLE KEYS */;
INSERT INTO `custas_processo` VALUES (1,7,1,2300.45,'D','2020-12-09'),(2,7,2,1300.45,'D','2020-12-09');
/*!40000 ALTER TABLE `custas_processo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `empresa`
--

DROP TABLE IF EXISTS `empresa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `empresa` (
  `CDEMPRESA` int(11) NOT NULL AUTO_INCREMENT,
  `CPFCNPJ` varchar(50) NOT NULL,
  `TPPESSOA` varchar(1) NOT NULL,
  `NOEMPRESA` varchar(100) NOT NULL,
  `NOENDERECO` varchar(200) DEFAULT NULL,
  `NRCEP` varchar(50) DEFAULT NULL,
  `NOBAIRRO` varchar(100) DEFAULT NULL,
  `CDCIDADE` int(11) NOT NULL,
  `DATA_CADASTRO` date NOT NULL,
  `TELEFONE1` varchar(50) NOT NULL,
  `TELEFONE2` varchar(50) DEFAULT NULL,
  `EMAIL` varchar(50) NOT NULL,
  `CDSTATUS` int(11) NOT NULL,
  `QTUSUARIOS` int(11) NOT NULL,
  `ESPACO` int(11) NOT NULL,
  `QTDEPROCPUSH` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`CDEMPRESA`),
  KEY `FK_CIDADE_EMPRESA` (`CDCIDADE`),
  CONSTRAINT `FK_CIDADE_EMPRESA` FOREIGN KEY (`CDCIDADE`) REFERENCES `cidade` (`CDCIDADE`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `empresa`
--

LOCK TABLES `empresa` WRITE;
/*!40000 ALTER TABLE `empresa` DISABLE KEYS */;
INSERT INTO `empresa` VALUES (1,'12344321','J','PLANO RAPIDO','R. CEL AURELIANO, 170','58301085','CENTRO',2,'2021-06-20','83981300623','','vasconcelos.kleiton@gmail.com',1,12,15,1500);
/*!40000 ALTER TABLE `empresa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `estado`
--

DROP TABLE IF EXISTS `estado`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `estado` (
  `CDESTADO` int(11) NOT NULL AUTO_INCREMENT,
  `DSESTADO` varchar(2) DEFAULT NULL,
  PRIMARY KEY (`CDESTADO`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `estado`
--

LOCK TABLES `estado` WRITE;
/*!40000 ALTER TABLE `estado` DISABLE KEYS */;
INSERT INTO `estado` VALUES (1,'PB'),(2,'PE');
/*!40000 ALTER TABLE `estado` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `estado_civil`
--

DROP TABLE IF EXISTS `estado_civil`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `estado_civil` (
  `cdestado_civil` varchar(10) NOT NULL,
  `noestado` varchar(50) NOT NULL,
  PRIMARY KEY (`cdestado_civil`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `estado_civil`
--

LOCK TABLES `estado_civil` WRITE;
/*!40000 ALTER TABLE `estado_civil` DISABLE KEYS */;
INSERT INTO `estado_civil` VALUES ('CA','Casado'),('DI','Divorciado'),('OT','Outro'),('SO','Solteiro'),('VI','Viúvo');
/*!40000 ALTER TABLE `estado_civil` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `garantia_processo`
--

DROP TABLE IF EXISTS `garantia_processo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `garantia_processo` (
  `cdcontrole` int(11) NOT NULL AUTO_INCREMENT,
  `cdprocesso` int(11) NOT NULL,
  `cdgarantia` int(11) NOT NULL,
  `vlpenhora` double NOT NULL,
  PRIMARY KEY (`cdcontrole`),
  KEY `fk_garantia_processo` (`cdprocesso`),
  KEY `fk_garantia_garantia` (`cdgarantia`),
  CONSTRAINT `fk_garantia_garantia` FOREIGN KEY (`cdgarantia`) REFERENCES `tipo_garantia` (`cdtipo`),
  CONSTRAINT `fk_garantia_processo` FOREIGN KEY (`cdprocesso`) REFERENCES `processo` (`cdprocesso`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `garantia_processo`
--

LOCK TABLES `garantia_processo` WRITE;
/*!40000 ALTER TABLE `garantia_processo` DISABLE KEYS */;
INSERT INTO `garantia_processo` VALUES (3,7,1,20000),(4,7,2,130000);
/*!40000 ALTER TABLE `garantia_processo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `grupo_cliente`
--

DROP TABLE IF EXISTS `grupo_cliente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `grupo_cliente` (
  `cdgrupo` int(11) NOT NULL AUTO_INCREMENT,
  `nogrupo` varchar(100) NOT NULL,
  `dsgrupo` varchar(200) DEFAULT NULL,
  `cdempresa` int(11) NOT NULL,
  PRIMARY KEY (`cdgrupo`),
  KEY `fk_empresa_grupo_cliente` (`cdempresa`),
  CONSTRAINT `fk_empresa_grupo_cliente` FOREIGN KEY (`cdempresa`) REFERENCES `empresa` (`CDEMPRESA`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `grupo_cliente`
--

LOCK TABLES `grupo_cliente` WRITE;
/*!40000 ALTER TABLE `grupo_cliente` DISABLE KEYS */;
INSERT INTO `grupo_cliente` VALUES (1,'Padrão','Grupo de Clientes Padrão',1);
/*!40000 ALTER TABLE `grupo_cliente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `grupo_cliente_usuario`
--

DROP TABLE IF EXISTS `grupo_cliente_usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `grupo_cliente_usuario` (
  `cdcontrole` int(11) NOT NULL AUTO_INCREMENT,
  `cdgrupo` int(11) NOT NULL,
  `cdusuario` int(11) DEFAULT NULL,
  PRIMARY KEY (`cdcontrole`),
  KEY `fk_usuario_grupo_cliente` (`cdusuario`),
  KEY `fk_grupo_grupo_cliente` (`cdgrupo`),
  CONSTRAINT `fk_grupo_grupo_cliente` FOREIGN KEY (`cdgrupo`) REFERENCES `grupo_cliente` (`cdgrupo`),
  CONSTRAINT `fk_usuario_grupo_cliente` FOREIGN KEY (`cdusuario`) REFERENCES `usuario` (`cdusuario`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `grupo_cliente_usuario`
--

LOCK TABLES `grupo_cliente_usuario` WRITE;
/*!40000 ALTER TABLE `grupo_cliente_usuario` DISABLE KEYS */;
INSERT INTO `grupo_cliente_usuario` VALUES (3,1,8);
/*!40000 ALTER TABLE `grupo_cliente_usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `grupo_trabalho`
--

DROP TABLE IF EXISTS `grupo_trabalho`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `grupo_trabalho` (
  `CDGRUPO` int(11) NOT NULL AUTO_INCREMENT,
  `NOGRUPO` varchar(50) NOT NULL,
  `DSGRUPO` varchar(100) DEFAULT NULL,
  `CDEMPRESA` int(11) NOT NULL,
  `CDGRUPO_PAI` int(11) DEFAULT NULL,
  PRIMARY KEY (`CDGRUPO`),
  KEY `fk_empresa_grupo` (`CDEMPRESA`),
  KEY `fk_grupo_pai` (`CDGRUPO_PAI`),
  CONSTRAINT `fk_empresa_grupo` FOREIGN KEY (`CDEMPRESA`) REFERENCES `empresa` (`CDEMPRESA`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `grupo_trabalho`
--

LOCK TABLES `grupo_trabalho` WRITE;
/*!40000 ALTER TABLE `grupo_trabalho` DISABLE KEYS */;
INSERT INTO `grupo_trabalho` VALUES (5,'Grupo Almeida','Grupo de trabalho almeida',1,5),(13,'Ana Emilia Alterado','Ana Emilia Pedrosa Cunha',1,13),(23,'Kleiton Vasconcelos','Kleiton Vasconcelos Costa',1,23),(24,'Sub-Grupo Alterado','Sub-Grupo da Ana Emilia Alterado',1,13),(27,'Sub-Grupo','Sub-Grupo dos Kleiton',1,23),(28,'Mizara','Grupos de trabalho da Mizar',1,28);
/*!40000 ALTER TABLE `grupo_trabalho` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `historico_alteracao_processo`
--

DROP TABLE IF EXISTS `historico_alteracao_processo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historico_alteracao_processo` (
  `cdalteracao` int(11) NOT NULL AUTO_INCREMENT,
  `cdprocesso` int(11) NOT NULL,
  `cdusuario_alterou` int(11) NOT NULL,
  `dsalteracao` varchar(4000) NOT NULL,
  `dtalteracao` date NOT NULL,
  `tpalteracao` varchar(1) NOT NULL,
  PRIMARY KEY (`cdalteracao`),
  KEY `fk_hist_alt_pro_pro` (`cdprocesso`),
  KEY `fk_hist_alt_pro_usu` (`cdusuario_alterou`),
  CONSTRAINT `fk_hist_alt_pro_pro` FOREIGN KEY (`cdprocesso`) REFERENCES `processo` (`cdprocesso`),
  CONSTRAINT `fk_hist_alt_pro_usu` FOREIGN KEY (`cdusuario_alterou`) REFERENCES `usuario` (`cdusuario`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `historico_alteracao_processo`
--

LOCK TABLES `historico_alteracao_processo` WRITE;
/*!40000 ALTER TABLE `historico_alteracao_processo` DISABLE KEYS */;
/*!40000 ALTER TABLE `historico_alteracao_processo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `historico_pessoa`
--

DROP TABLE IF EXISTS `historico_pessoa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historico_pessoa` (
  `cdhistorico` int(11) NOT NULL AUTO_INCREMENT,
  `cdpessoa` int(11) NOT NULL,
  `dshistorico` varchar(300) NOT NULL,
  `dthistorico` date NOT NULL,
  `tphistorico` varchar(1) NOT NULL,
  `cdusuario` int(11) NOT NULL,
  PRIMARY KEY (`cdhistorico`),
  KEY `fk_pessoa_historico` (`cdpessoa`),
  KEY `fk_usuario_historico` (`cdusuario`),
  CONSTRAINT `fk_pessoa_historico` FOREIGN KEY (`cdpessoa`) REFERENCES `pessoa` (`cdpessoa`),
  CONSTRAINT `fk_usuario_historico` FOREIGN KEY (`cdusuario`) REFERENCES `usuario` (`cdusuario`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `historico_pessoa`
--

LOCK TABLES `historico_pessoa` WRITE;
/*!40000 ALTER TABLE `historico_pessoa` DISABLE KEYS */;
INSERT INTO `historico_pessoa` VALUES (2,10,'Inclusão de nova pessoa','2021-10-02','I',8),(3,11,'Inclusão de nova pessoa','2021-10-02','I',8),(4,8,'Alteração de pessoa','2021-10-02','A',8),(5,8,'Inativação de psssoa','2021-10-02','X',8),(6,12,'Inclusão de nova pessoa','2021-10-02','I',8),(15,21,'Inclusão de nova pessoa','2022-02-05','I',8);
/*!40000 ALTER TABLE `historico_pessoa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `historico_processo`
--

DROP TABLE IF EXISTS `historico_processo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historico_processo` (
  `cdhistorico` int(11) NOT NULL AUTO_INCREMENT,
  `cdprocesso` int(11) NOT NULL,
  `cdusuario` int(11) NOT NULL,
  `dshistorico` varchar(5000) NOT NULL,
  `dtocorrencia` date NOT NULL,
  `dthistorico` date NOT NULL,
  PRIMARY KEY (`cdhistorico`),
  KEY `fk_historico_processo` (`cdprocesso`),
  KEY `fk_historico_pro_usu` (`cdusuario`),
  CONSTRAINT `fk_historico_pro_usu` FOREIGN KEY (`cdusuario`) REFERENCES `usuario` (`cdusuario`),
  CONSTRAINT `fk_historico_processo` FOREIGN KEY (`cdprocesso`) REFERENCES `processo` (`cdprocesso`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `historico_processo`
--

LOCK TABLES `historico_processo` WRITE;
/*!40000 ALTER TABLE `historico_processo` DISABLE KEYS */;
INSERT INTO `historico_processo` VALUES (1,7,8,'Incluindo primeiro histórico de processo','2022-01-02','2022-01-02'),(2,7,8,'- version: 3.15.200\n- copyright: The Eclipse Foundation makes available all content in this plug-in (\"Content\"). Unless otherwise indicated below, the Content is provided to you under the terms and conditions of the Eclipse Public License Version 2.0 (\"EPL\"). A copy of the EPL is available at \nhttp://www.eclipse.org/legal/epl-2.0\n. For purposes of the EPL, \"Program\" will mean the Content.\nIf you did not receive this Content directly from the Eclipse Foundation, the Content is being redistributed by another party (\"Redistributor\") and different terms and conditions may apply to your use of any object code in the Content. Check the Redistributor\'s license that was provided with the Content. If no such license exists, contact the Redistributor. Unless otherwise indicated below, the terms and conditions of the EPL still apply to any source code in the Content and such source code may be obtained at \nhttp://www.eclipse.org','2022-01-02','2022-01-02'),(4,7,8,'Implementation of certain elements of the OSGi Materials may be subject to third party intellectual property rights, including without limitation, patent rights (such a third party may or may not be a member of the OSGi Alliance). The OSGi Alliance and its members are not responsible and shall not be held responsible in any manner for identifying or failing to identify any or all such third party intellectual property rights.\nApache Felix Resolver\nThis bundle includes software developed by The Apache Software Foundation as part of the Felix project. this includes all files in the following sub-directories (and their sub-directories):\norg/apache/felix/resolver\nYour use of the Resolver code is subject to the terms and conditions of the Apache Software License 2.0. A copy of the license is contained in the file \nLICENSE-2.0.txt\n and is also available at \nhttp://www.apache.org/licenses/LICENSE-2.0.html','2021-12-09','2022-01-02');
/*!40000 ALTER TABLE `historico_processo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `indice_processo`
--

DROP TABLE IF EXISTS `indice_processo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `indice_processo` (
  `cdindice` int(11) NOT NULL AUTO_INCREMENT,
  `cdprocesso` int(11) NOT NULL,
  `indice` varchar(4000) NOT NULL,
  `cdempresa` int(11) NOT NULL,
  PRIMARY KEY (`cdindice`),
  KEY `fk_processo_indice` (`cdprocesso`),
  KEY `fk_processo_indice_empresa` (`cdempresa`),
  CONSTRAINT `fk_processo_indice` FOREIGN KEY (`cdprocesso`) REFERENCES `processo` (`cdprocesso`),
  CONSTRAINT `fk_processo_indice_empresa` FOREIGN KEY (`cdempresa`) REFERENCES `empresa` (`CDEMPRESA`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `indice_processo`
--

LOCK TABLES `indice_processo` WRITE;
/*!40000 ALTER TABLE `indice_processo` DISABLE KEYS */;
INSERT INTO `indice_processo` VALUES (3,7,'EBT-2033 3356886778445112 54321 Ana Emilia Cunha PedrosaKleiton Vasconcelos Costa ',1);
/*!40000 ALTER TABLE `indice_processo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `log_usuario`
--

DROP TABLE IF EXISTS `log_usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `log_usuario` (
  `cdlog` int(11) NOT NULL AUTO_INCREMENT,
  `cdusuario` int(11) NOT NULL,
  `dtlog` datetime NOT NULL,
  `tplog` varchar(1) NOT NULL,
  PRIMARY KEY (`cdlog`),
  KEY `fk_usuario_log_usuario` (`cdusuario`),
  CONSTRAINT `fk_usuario_log_usuario` FOREIGN KEY (`cdusuario`) REFERENCES `usuario` (`cdusuario`)
) ENGINE=InnoDB AUTO_INCREMENT=152 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `log_usuario`
--

LOCK TABLES `log_usuario` WRITE;
/*!40000 ALTER TABLE `log_usuario` DISABLE KEYS */;
INSERT INTO `log_usuario` VALUES (7,8,'2021-12-08 01:19:17','E'),(8,8,'2021-12-08 01:19:47','S'),(9,8,'2021-12-08 01:20:01','E'),(10,8,'2021-12-08 06:26:32','E'),(11,8,'2021-12-08 09:46:54','S'),(12,8,'2021-12-08 09:46:56','E'),(13,8,'2021-12-09 07:23:44','E'),(14,8,'2021-12-09 07:45:51','S'),(15,8,'2021-12-09 11:28:33','E'),(16,8,'2021-12-09 11:28:37','E'),(17,8,'2021-12-09 11:29:17','S'),(18,8,'2021-12-09 11:29:18','E'),(19,8,'2021-12-10 01:28:46','S'),(20,8,'2021-12-10 08:00:38','E'),(21,8,'2021-12-10 08:16:53','S'),(22,8,'2021-12-10 08:16:59','E'),(23,8,'2021-12-10 08:17:04','S'),(24,8,'2021-12-10 08:18:17','E'),(25,8,'2021-12-10 08:18:40','E'),(26,8,'2021-12-10 08:18:55','S'),(27,8,'2021-12-10 08:19:06','E'),(28,8,'2021-12-10 08:19:51','S'),(29,8,'2021-12-14 04:37:42','E'),(30,8,'2021-12-14 04:38:09','E'),(31,8,'2021-12-14 04:38:46','E'),(32,8,'2021-12-14 04:39:27','E'),(33,8,'2021-12-14 04:39:33','E'),(34,8,'2021-12-14 04:39:53','S'),(35,8,'2021-12-14 04:39:56','E'),(36,8,'2021-12-14 04:40:39','S'),(37,8,'2021-12-14 04:40:40','E'),(38,8,'2021-12-14 04:45:27','E'),(39,8,'2021-12-14 04:48:20','E'),(40,8,'2021-12-14 04:48:26','E'),(41,8,'2021-12-14 04:48:28','E'),(42,8,'2021-12-14 04:48:29','E'),(43,8,'2021-12-14 04:48:45','E'),(44,8,'2021-12-14 04:50:14','E'),(45,8,'2021-12-14 07:17:27','E'),(46,8,'2021-12-14 08:23:05','E'),(47,8,'2021-12-14 08:31:30','E'),(48,8,'2021-12-16 09:01:41','E'),(49,8,'2021-12-17 06:40:37','E'),(50,8,'2021-12-17 06:43:33','E'),(51,8,'2021-12-17 03:24:49','S'),(52,8,'2021-12-17 03:46:17','E'),(53,8,'2021-12-17 05:09:55','S'),(54,8,'2021-12-17 05:09:57','E'),(55,8,'2021-12-17 05:40:58','E'),(56,8,'2021-12-17 07:08:20','E'),(57,8,'2021-12-18 12:02:47','E'),(58,8,'2021-12-18 05:24:27','E'),(59,8,'2021-12-18 11:17:42','E'),(60,8,'2021-12-20 08:41:03','E'),(61,8,'2021-12-20 09:15:20','E'),(62,8,'2021-12-21 02:44:44','E'),(63,8,'2021-12-21 09:47:48','E'),(64,8,'2021-12-22 12:12:13','E'),(65,8,'2021-12-22 03:25:03','E'),(66,8,'2021-12-22 07:09:17','S'),(67,8,'2021-12-22 07:13:19','E'),(68,8,'2021-12-22 08:42:58','S'),(69,8,'2021-12-22 08:42:59','E'),(70,8,'2021-12-22 09:55:25','E'),(71,8,'2021-12-23 05:45:16','E'),(72,8,'2021-12-24 07:22:09','E'),(73,8,'2021-12-24 11:04:07','E'),(74,8,'2021-12-25 12:45:25','S'),(75,8,'2021-12-25 03:22:07','E'),(76,8,'2021-12-25 10:49:23','E'),(77,8,'2021-12-26 12:00:21','E'),(78,8,'2021-12-26 10:31:22','E'),(79,8,'2021-12-26 12:09:31','S'),(80,8,'2021-12-27 10:01:03','E'),(81,8,'2021-12-28 03:38:47','E'),(82,8,'2021-12-29 06:34:22','E'),(83,8,'2021-12-29 07:24:32','E'),(84,8,'2021-12-29 09:15:14','S'),(85,8,'2021-12-29 09:15:15','E'),(86,8,'2021-12-29 10:09:41','E'),(87,8,'2021-12-30 06:49:52','E'),(88,8,'2021-12-30 06:56:26','E'),(89,8,'2021-12-30 06:59:52','E'),(90,8,'2021-12-30 07:05:38','E'),(91,8,'2021-12-30 07:08:02','E'),(92,8,'2021-12-30 05:59:08','S'),(93,8,'2021-12-30 05:59:09','E'),(94,8,'2021-12-30 06:36:43','E'),(95,8,'2021-12-30 06:40:21','E'),(96,8,'2021-12-30 06:43:40','E'),(97,8,'2021-12-30 06:46:57','E'),(98,8,'2022-01-02 01:02:26','E'),(99,8,'2022-01-02 11:18:40','E'),(100,8,'2022-01-02 12:01:58','E'),(101,8,'2022-01-02 01:43:35','E'),(102,8,'2022-01-02 01:57:22','S'),(103,8,'2022-01-02 01:57:24','E'),(104,8,'2022-01-03 11:11:47','E'),(105,8,'2022-01-03 11:41:52','E'),(106,8,'2022-01-06 10:17:32','E'),(107,8,'2022-01-06 10:19:57','E'),(108,8,'2022-01-07 12:26:48','E'),(109,8,'2022-01-07 12:40:26','E'),(110,8,'2022-01-07 12:42:09','E'),(111,8,'2022-01-07 12:44:44','E'),(112,8,'2022-01-07 12:50:56','E'),(113,8,'2022-01-14 06:38:18','E'),(114,8,'2022-01-15 12:58:50','E'),(115,8,'2022-01-16 09:43:16','E'),(116,8,'2022-01-16 11:07:57','E'),(117,8,'2022-01-16 12:15:58','E'),(118,8,'2022-01-16 12:40:15','E'),(119,8,'2022-01-16 04:43:44','E'),(120,8,'2022-01-17 06:51:50','E'),(121,8,'2022-01-17 09:58:09','E'),(122,8,'2022-01-17 09:58:24','E'),(123,8,'2022-01-17 09:58:30','E'),(124,8,'2022-01-17 09:58:37','E'),(125,8,'2022-01-17 09:58:47','E'),(126,8,'2022-01-22 02:26:42','E'),(127,8,'2022-01-22 02:27:48','E'),(128,8,'2022-01-22 02:27:55','E'),(129,8,'2022-01-23 09:03:40','E'),(130,8,'2022-01-23 07:31:16','E'),(131,8,'2022-01-23 11:51:31','S'),(132,8,'2022-01-24 12:04:33','E'),(133,8,'2022-01-24 12:39:04','S'),(134,8,'2022-01-24 12:39:06','E'),(135,8,'2022-01-24 07:59:39','E'),(136,8,'2022-01-24 08:25:04','E'),(137,8,'2022-01-25 02:27:46','E'),(138,8,'2022-01-25 05:26:45','E'),(139,8,'2022-01-25 05:42:54','S'),(140,8,'2022-01-25 05:43:03','E'),(141,8,'2022-01-26 06:34:26','E'),(142,8,'2022-01-27 09:41:25','E'),(143,8,'2022-01-28 12:38:16','S'),(144,8,'2022-01-28 12:54:50','E'),(145,8,'2022-01-28 12:56:22','S'),(146,8,'2022-01-28 03:36:47','E'),(147,8,'2022-01-28 10:07:38','E'),(148,8,'2022-02-02 08:56:01','E'),(149,8,'2022-02-02 11:09:30','E'),(150,8,'2022-02-03 08:49:10','E'),(151,8,'2022-02-04 03:34:58','E');
/*!40000 ALTER TABLE `log_usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `objeto_acao`
--

DROP TABLE IF EXISTS `objeto_acao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `objeto_acao` (
  `CDOBJETO` int(11) NOT NULL AUTO_INCREMENT,
  `NOOBJETO` varchar(100) NOT NULL,
  `CDEMPRESA` int(11) NOT NULL,
  PRIMARY KEY (`CDOBJETO`),
  KEY `fk_empresa_objeto_acao` (`CDEMPRESA`),
  CONSTRAINT `fk_empresa_objeto_acao` FOREIGN KEY (`CDEMPRESA`) REFERENCES `empresa` (`CDEMPRESA`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `objeto_acao`
--

LOCK TABLES `objeto_acao` WRITE;
/*!40000 ALTER TABLE `objeto_acao` DISABLE KEYS */;
INSERT INTO `objeto_acao` VALUES (1,'Danos Morais',1),(2,'Danos Materiais',1),(4,'Negligências',1);
/*!40000 ALTER TABLE `objeto_acao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `objeto_acao_processo`
--

DROP TABLE IF EXISTS `objeto_acao_processo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `objeto_acao_processo` (
  `cdcontrole` int(11) NOT NULL AUTO_INCREMENT,
  `cdprocesso` int(11) NOT NULL,
  `cdobjeto` int(11) NOT NULL,
  PRIMARY KEY (`cdcontrole`),
  KEY `fk_processo_objeto` (`cdprocesso`),
  KEY `fk_objeto_objeto` (`cdobjeto`),
  CONSTRAINT `fk_objeto_objeto` FOREIGN KEY (`cdobjeto`) REFERENCES `objeto_acao` (`CDOBJETO`),
  CONSTRAINT `fk_processo_objeto` FOREIGN KEY (`cdprocesso`) REFERENCES `processo` (`cdprocesso`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `objeto_acao_processo`
--

LOCK TABLES `objeto_acao_processo` WRITE;
/*!40000 ALTER TABLE `objeto_acao_processo` DISABLE KEYS */;
INSERT INTO `objeto_acao_processo` VALUES (11,7,2),(42,7,1),(43,7,4);
/*!40000 ALTER TABLE `objeto_acao_processo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pagamento_processo`
--

DROP TABLE IF EXISTS `pagamento_processo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pagamento_processo` (
  `cdcontrole` int(11) NOT NULL AUTO_INCREMENT,
  `cdprocesso` int(11) NOT NULL,
  `cdpagamento` int(11) NOT NULL,
  `vlpagamento` double NOT NULL,
  `tipo` varchar(1) NOT NULL,
  `dtpagamento` date DEFAULT NULL,
  PRIMARY KEY (`cdcontrole`),
  KEY `fk_pagamento_processo` (`cdprocesso`),
  KEY `fk_pagamento_pagamento` (`cdpagamento`),
  CONSTRAINT `fk_pagamento_pagamento` FOREIGN KEY (`cdpagamento`) REFERENCES `tipo_pagamento` (`cdtipo`),
  CONSTRAINT `fk_pagamento_processo` FOREIGN KEY (`cdprocesso`) REFERENCES `processo` (`cdprocesso`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pagamento_processo`
--

LOCK TABLES `pagamento_processo` WRITE;
/*!40000 ALTER TABLE `pagamento_processo` DISABLE KEYS */;
INSERT INTO `pagamento_processo` VALUES (1,7,1,300,'C','2021-11-12'),(2,7,2,200,'C','2021-12-12');
/*!40000 ALTER TABLE `pagamento_processo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `partes`
--

DROP TABLE IF EXISTS `partes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `partes` (
  `cdcontrole` int(11) NOT NULL AUTO_INCREMENT,
  `cdprocesso` int(11) NOT NULL,
  `cdpessoa` int(11) NOT NULL,
  `tpparte` varchar(1) NOT NULL,
  PRIMARY KEY (`cdcontrole`),
  KEY `fk_partes_processo` (`cdprocesso`),
  KEY `fk_partes_pessoa` (`cdpessoa`),
  CONSTRAINT `fk_partes_pessoa` FOREIGN KEY (`cdpessoa`) REFERENCES `pessoa` (`cdpessoa`),
  CONSTRAINT `fk_partes_processo` FOREIGN KEY (`cdprocesso`) REFERENCES `processo` (`cdprocesso`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `partes`
--

LOCK TABLES `partes` WRITE;
/*!40000 ALTER TABLE `partes` DISABLE KEYS */;
INSERT INTO `partes` VALUES (1,1,1,'A'),(2,1,8,'R'),(8,7,8,'R'),(9,67,1,'A'),(10,67,8,'A'),(11,67,10,'R'),(14,7,10,'A'),(15,7,1,'A'),(16,7,12,'A');
/*!40000 ALTER TABLE `partes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `perfil`
--

DROP TABLE IF EXISTS `perfil`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `perfil` (
  `cdperfil` int(11) NOT NULL AUTO_INCREMENT,
  `noperfil` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`cdperfil`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `perfil`
--

LOCK TABLES `perfil` WRITE;
/*!40000 ALTER TABLE `perfil` DISABLE KEYS */;
INSERT INTO `perfil` VALUES (1,'Administrador'),(2,'Advogado Sênior'),(3,'Advogado'),(4,'Estagiário'),(5,'Administrativo'),(6,'Financeiro'),(7,'Consulta');
/*!40000 ALTER TABLE `perfil` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `perfil_pessoa`
--

DROP TABLE IF EXISTS `perfil_pessoa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `perfil_pessoa` (
  `cdperfil` varchar(10) NOT NULL,
  `noperfil` varchar(50) NOT NULL,
  PRIMARY KEY (`cdperfil`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `perfil_pessoa`
--

LOCK TABLES `perfil_pessoa` WRITE;
/*!40000 ALTER TABLE `perfil_pessoa` DISABLE KEYS */;
INSERT INTO `perfil_pessoa` VALUES ('CL','Cliente'),('PC','Parte Contrária');
/*!40000 ALTER TABLE `perfil_pessoa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pessoa`
--

DROP TABLE IF EXISTS `pessoa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pessoa` (
  `cdpessoa` int(11) NOT NULL AUTO_INCREMENT,
  `nopessoa` varchar(200) NOT NULL,
  `tppessoa` varchar(1) NOT NULL,
  `cdperfil` varchar(2) NOT NULL,
  `dtnascimento` date DEFAULT NULL,
  `profissao` varchar(100) DEFAULT NULL,
  `atividade_economica` varchar(200) DEFAULT NULL,
  `cdestado_civil` varchar(2) DEFAULT NULL,
  `cpfcnpj` varchar(30) DEFAULT NULL,
  `rg` varchar(30) DEFAULT NULL,
  `passaporte` varchar(30) DEFAULT NULL,
  `titulo` varchar(30) DEFAULT NULL,
  `reservista` varchar(30) DEFAULT NULL,
  `pis` varchar(30) DEFAULT NULL,
  `cnh` varchar(30) DEFAULT NULL,
  `ctps` varchar(50) DEFAULT NULL,
  `nomae` varchar(200) DEFAULT NULL,
  `nopai` varchar(200) DEFAULT NULL,
  `naturalidade` varchar(200) DEFAULT NULL,
  `nacionalidade` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `telefone1` varchar(20) DEFAULT NULL,
  `telefone2` varchar(20) DEFAULT NULL,
  `endereco` varchar(300) DEFAULT NULL,
  `numero` varchar(10) DEFAULT NULL,
  `cdtipo_endereco` varchar(30) DEFAULT NULL,
  `bairro` varchar(100) DEFAULT NULL,
  `cdcidade` int(11) NOT NULL,
  `cep` varchar(30) DEFAULT NULL,
  `conta_corrente` varchar(50) DEFAULT NULL,
  `agencia` varchar(50) DEFAULT NULL,
  `banco` varchar(50) DEFAULT NULL,
  `pix` varchar(100) DEFAULT NULL,
  `observacao` varchar(3000) DEFAULT NULL,
  `cdstatus` int(11) NOT NULL DEFAULT '0',
  `dtcadastro` date NOT NULL,
  `cdempresa` int(11) NOT NULL,
  `cdgrupo_cliente` int(11) DEFAULT NULL,
  `tipo_conta` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`cdpessoa`),
  KEY `fk_cidade_pessoa` (`cdcidade`),
  KEY `fk_empresa_pessoa` (`cdempresa`),
  KEY `fk_grupo_cliente_pessoa` (`cdgrupo_cliente`),
  CONSTRAINT `fk_cidade_pessoa` FOREIGN KEY (`cdcidade`) REFERENCES `cidade` (`CDCIDADE`),
  CONSTRAINT `fk_empresa_pessoa` FOREIGN KEY (`cdempresa`) REFERENCES `empresa` (`CDEMPRESA`),
  CONSTRAINT `fk_grupo_cliente_pessoa` FOREIGN KEY (`cdgrupo_cliente`) REFERENCES `grupo_cliente` (`cdgrupo`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pessoa`
--

LOCK TABLES `pessoa` WRITE;
/*!40000 ALTER TABLE `pessoa` DISABLE KEYS */;
INSERT INTO `pessoa` VALUES (1,'Kleiton Vasconcelos Costa','F','CL','1981-07-06','Engenheiro de Sistemas','Software','SO','03564942416','2282480','3131313','123456789','123456789','6546464','987654321','16464646464','Izabel Margarida de Vasconcelos Costa','José Francisco da Costa Irmão','Santa Rita','Brasileira','vasconcelos.kleiton@gmail.com','83981300623','83981300123','Coronel Aureliano','170','RUA','Centro',2,'58301085','201568','201','Santander','03564942416','56464646464646',0,'2021-10-02',1,1,NULL),(8,'Ana Emilia Cunha Pedrosa','F','CL','1981-07-06','Assistente administrativo 2','Software','SO','03564944182','2282480','3131313','123456789','123456789','6546464','987654321','16464646464','Izabel Margarida de Vasconcelos Costa','José Francisco da Costa Irmão','Santa Rita','Brasileira','vasconcelos.kleiton@gmail.com','83981300623','646464646','Coronel Aureliano','170','RUA','Centro',2,'58301085','201568','201','Santander','03564942416','56464646464646',1,'2021-10-02',1,NULL,NULL),(10,'Izabel Margarida de Vasconcelos Costa','F','CL','1976-12-14','Aposentada','Aposentada','CA','03564944189','2282480','3131313','123456789','123456789','6546464','987654321','16464646464','Izabel Margarida de Vasconcelos Costa','José Francisco da Costa Irmão','Santa Rita','Brasileira','ana.emilia@gmail.com','83981300623','646464646','Coronel Aureliano','170','RUA','Centro',2,'58301085','201568','201','Santander','03564942416','56464646464646',0,'2021-10-02',1,1,NULL),(11,'Jose Francisco da Costa Irmão','F','CL','1974-12-14','Aposentado','Aposentado','CA','03564877745','2282480','3131313','123456789','123456789','6546464','987654321','16464646464','Izabel Margarida de Vasconcelos Costa','José Francisco da Costa Irmão','Santa Rita','Brasileira','jose.francisco@gmail.com','83981300623','646464646','Coronel Aureliano','170','RUA','Centro',2,'58301085','201568','201','Santander','03564942416','56464646464646',0,'2021-10-02',1,1,NULL),(12,'Patricia vasconcelos Costa','F','CL','1974-12-14','Aposentado','Aposentado','CA','03564877712','2282480','3131313','123456789','123456789','6546464','987654321','16464646464','Izabel Margarida de Vasconcelos Costa','José Francisco da Costa Irmão','Santa Rita','Brasileira','patricia.vasconcelos@gmail.com','83981300623','646464646','Coronel Aureliano','170','RUA','Centro',2,'58301085','201568','201','Santander','03564942416','56464646464646',0,'2021-10-02',1,1,NULL),(21,'Diego Souza','F','CL','1992-08-09','Engenheiro de Sistemas','Software','SO','035.649.424-12','2233333','','','','','','','','','Santa Rita','Brasileira','','(83)-98130-0623','','Rua Coronel Aureliano','170','RUA','',2,'58301085','','','','','',0,'2022-02-05',1,NULL,NULL);
/*!40000 ALTER TABLE `pessoa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `proccess`
--

DROP TABLE IF EXISTS `proccess`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `proccess` (
  `cdprocesso` int(11) NOT NULL AUTO_INCREMENT,
  `cdgrupo` int(11) NOT NULL,
  `pasta` varchar(100) DEFAULT NULL,
  `nrprocesso` varchar(100) DEFAULT NULL,
  `nrcnj` varchar(100) DEFAULT NULL,
  `cdempresa` int(11) NOT NULL,
  PRIMARY KEY (`cdprocesso`),
  KEY `fk_mizera_grupo` (`cdgrupo`),
  KEY `fk_mizera_empresa` (`cdempresa`),
  CONSTRAINT `fk_mizera_empresa` FOREIGN KEY (`cdempresa`) REFERENCES `empresa` (`CDEMPRESA`),
  CONSTRAINT `fk_mizera_grupo` FOREIGN KEY (`cdgrupo`) REFERENCES `grupo_trabalho` (`CDGRUPO`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `proccess`
--

LOCK TABLES `proccess` WRITE;
/*!40000 ALTER TABLE `proccess` DISABLE KEYS */;
INSERT INTO `proccess` VALUES (2,5,'kleiton','1234566789','123',1),(3,5,'kleiton','1234566789','123',1),(4,5,'kleiton','1234566789','123',1),(5,5,'kleiton','1234566789','123',1);
/*!40000 ALTER TABLE `proccess` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `processo`
--

DROP TABLE IF EXISTS `processo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `processo` (
  `cdprocesso` int(11) NOT NULL AUTO_INCREMENT,
  `cdgrupo` int(11) NOT NULL,
  `pasta` varchar(30) DEFAULT NULL,
  `nrprocesso` varchar(30) DEFAULT NULL,
  `nrcnj` varchar(30) DEFAULT NULL,
  `nrinstancia` varchar(30) DEFAULT NULL,
  `dscomarca` varchar(200) DEFAULT NULL,
  `cdtipo_acao` int(11) NOT NULL,
  `cdstatus_processual` int(11) NOT NULL,
  `cdarea_atuacao` int(11) NOT NULL,
  `cdtipo_decisao` int(11) NOT NULL,
  `dtdistribuicao` date DEFAULT NULL,
  `dtultima_decisao` date DEFAULT NULL,
  `vlprovavel` double DEFAULT NULL,
  `vlpossivel` double DEFAULT NULL,
  `vlremoto` double DEFAULT NULL,
  `vlcausa` double DEFAULT NULL,
  `dspedidos` varchar(200) DEFAULT NULL,
  `dsobservacao` varchar(3000) DEFAULT NULL,
  `cdusuario` int(11) NOT NULL,
  `cdresponsavel` int(11) NOT NULL,
  `cdstatus_interno` int(11) NOT NULL,
  `dtcadastro` date NOT NULL,
  `snpush` varchar(1) NOT NULL,
  `snhistorico` varchar(1) NOT NULL,
  `snemail` varchar(1) NOT NULL,
  `cdstatus` int(11) NOT NULL,
  `cdempresa` int(11) NOT NULL,
  `dtultima_movimentacao` date DEFAULT NULL,
  `dtsentenca` date DEFAULT NULL,
  `snimportante` varchar(1) NOT NULL,
  PRIMARY KEY (`cdprocesso`),
  KEY `fk_processo_grupo` (`cdgrupo`),
  KEY `fk_processo_tipo_acao` (`cdtipo_acao`),
  KEY `fk_processo_status_processual` (`cdstatus_processual`),
  KEY `fk_processo_area_atuacao` (`cdarea_atuacao`),
  KEY `fk_processo_tipo_decisao` (`cdtipo_decisao`),
  KEY `fk_processo_usuario` (`cdusuario`),
  KEY `fk_processo_responsavel` (`cdresponsavel`),
  KEY `fk_processo_empresa` (`cdempresa`),
  CONSTRAINT `fk_processo_area_atuacao` FOREIGN KEY (`cdarea_atuacao`) REFERENCES `area_atuacao` (`cdarea`),
  CONSTRAINT `fk_processo_empresa` FOREIGN KEY (`cdempresa`) REFERENCES `empresa` (`CDEMPRESA`),
  CONSTRAINT `fk_processo_grupo` FOREIGN KEY (`cdgrupo`) REFERENCES `grupo_trabalho` (`CDGRUPO`),
  CONSTRAINT `fk_processo_responsavel` FOREIGN KEY (`cdresponsavel`) REFERENCES `usuario` (`cdusuario`),
  CONSTRAINT `fk_processo_status_processual` FOREIGN KEY (`cdstatus_processual`) REFERENCES `status_processual` (`cdstatus`),
  CONSTRAINT `fk_processo_tipo_acao` FOREIGN KEY (`cdtipo_acao`) REFERENCES `tipo_acao` (`cdtipo`),
  CONSTRAINT `fk_processo_tipo_decisao` FOREIGN KEY (`cdtipo_decisao`) REFERENCES `tipo_decisao` (`cdtipo`),
  CONSTRAINT `fk_processo_usuario` FOREIGN KEY (`cdusuario`) REFERENCES `usuario` (`cdusuario`)
) ENGINE=InnoDB AUTO_INCREMENT=68 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `processo`
--

LOCK TABLES `processo` WRITE;
/*!40000 ALTER TABLE `processo` DISABLE KEYS */;
INSERT INTO `processo` VALUES (1,5,'EBT-3320','12345','3356889778445112','2 Grau','Primeira Comarca de Santa Rita',1,1,1,1,'2021-09-20','2021-10-23',0,0,0,0,NULL,'Uma observação',8,8,0,'2021-10-29','S','S','S',0,1,'2021-10-29',NULL,'N'),(7,23,'EBT-2033','5432121','88888888888888882021',NULL,'Primeira Comarca de Pilar',1,2,3,1,'2021-12-29','2021-12-31',23000,0,0,0,'DANOS MORAIS','Vou colocar uma observação válida',8,8,0,'2021-12-17','S','S','S',0,1,NULL,'2021-12-30','S'),(67,23,'KLE','8888888-88.8888.8.88.8888','W',NULL,'SANTA RITA',1,1,1,1,NULL,NULL,0,0,0,0,'DANOS','',8,8,0,'2021-12-21','N','N','N',0,1,'2021-12-21',NULL,'N');
/*!40000 ALTER TABLE `processo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `processo_importancia`
--

DROP TABLE IF EXISTS `processo_importancia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `processo_importancia` (
  `codigo` int(11) NOT NULL AUTO_INCREMENT,
  `cdusuario` int(11) NOT NULL,
  `cdprocesso` int(11) NOT NULL,
  PRIMARY KEY (`codigo`),
  KEY `fk_processo_importancia` (`cdprocesso`),
  KEY `fk_usuario_importancia` (`cdusuario`),
  CONSTRAINT `fk_processo_importancia` FOREIGN KEY (`cdprocesso`) REFERENCES `processo` (`cdprocesso`),
  CONSTRAINT `fk_usuario_importancia` FOREIGN KEY (`cdusuario`) REFERENCES `usuario` (`cdusuario`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `processo_importancia`
--

LOCK TABLES `processo_importancia` WRITE;
/*!40000 ALTER TABLE `processo_importancia` DISABLE KEYS */;
INSERT INTO `processo_importancia` VALUES (3,8,7);
/*!40000 ALTER TABLE `processo_importancia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `status_processual`
--

DROP TABLE IF EXISTS `status_processual`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `status_processual` (
  `cdstatus` int(11) NOT NULL AUTO_INCREMENT,
  `descricao` varchar(100) NOT NULL,
  `cdempresa` int(11) NOT NULL,
  PRIMARY KEY (`cdstatus`),
  KEY `fk_empresa_status` (`cdempresa`),
  CONSTRAINT `fk_empresa_status` FOREIGN KEY (`cdempresa`) REFERENCES `empresa` (`CDEMPRESA`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `status_processual`
--

LOCK TABLES `status_processual` WRITE;
/*!40000 ALTER TABLE `status_processual` DISABLE KEYS */;
INSERT INTO `status_processual` VALUES (1,'A Juizar',1),(2,'Arquivado',1);
/*!40000 ALTER TABLE `status_processual` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tipo_acao`
--

DROP TABLE IF EXISTS `tipo_acao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tipo_acao` (
  `cdtipo` int(11) NOT NULL AUTO_INCREMENT,
  `notipo` varchar(100) NOT NULL,
  `cdempresa` int(11) NOT NULL,
  PRIMARY KEY (`cdtipo`),
  KEY `fk_tipo_acao_empresa` (`cdempresa`),
  CONSTRAINT `fk_tipo_acao_empresa` FOREIGN KEY (`cdempresa`) REFERENCES `empresa` (`CDEMPRESA`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tipo_acao`
--

LOCK TABLES `tipo_acao` WRITE;
/*!40000 ALTER TABLE `tipo_acao` DISABLE KEYS */;
INSERT INTO `tipo_acao` VALUES (1,'Ação civil pública',1),(2,'Agravo de Instrumento',1);
/*!40000 ALTER TABLE `tipo_acao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tipo_custa`
--

DROP TABLE IF EXISTS `tipo_custa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tipo_custa` (
  `cdtipo` int(11) NOT NULL AUTO_INCREMENT,
  `notipo` varchar(100) NOT NULL,
  `cdempresa` int(11) NOT NULL,
  PRIMARY KEY (`cdtipo`),
  KEY `fk_tipo_custa_empresa` (`cdempresa`),
  CONSTRAINT `fk_tipo_custa_empresa` FOREIGN KEY (`cdempresa`) REFERENCES `empresa` (`CDEMPRESA`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tipo_custa`
--

LOCK TABLES `tipo_custa` WRITE;
/*!40000 ALTER TABLE `tipo_custa` DISABLE KEYS */;
INSERT INTO `tipo_custa` VALUES (1,'Custas de Diligências',1),(2,'Custas Iniciais',1),(3,'Custas Finais',1),(5,'Honorários com Clientes',1);
/*!40000 ALTER TABLE `tipo_custa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tipo_decisao`
--

DROP TABLE IF EXISTS `tipo_decisao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tipo_decisao` (
  `cdtipo` int(11) NOT NULL AUTO_INCREMENT,
  `notipo` varchar(100) NOT NULL,
  `cdempresa` int(11) NOT NULL,
  PRIMARY KEY (`cdtipo`),
  KEY `fk_tipo_decisao_empresa` (`cdempresa`),
  CONSTRAINT `fk_tipo_decisao_empresa` FOREIGN KEY (`cdempresa`) REFERENCES `empresa` (`CDEMPRESA`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tipo_decisao`
--

LOCK TABLES `tipo_decisao` WRITE;
/*!40000 ALTER TABLE `tipo_decisao` DISABLE KEYS */;
INSERT INTO `tipo_decisao` VALUES (1,'Acordo Favorável',1),(2,'Acordo Desfavorável Alterado',1),(3,'Sentença Condenatória',1);
/*!40000 ALTER TABLE `tipo_decisao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tipo_endereco`
--

DROP TABLE IF EXISTS `tipo_endereco`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tipo_endereco` (
  `cdtipo_endereco` varchar(10) NOT NULL,
  `notipo` varchar(50) NOT NULL,
  PRIMARY KEY (`cdtipo_endereco`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tipo_endereco`
--

LOCK TABLES `tipo_endereco` WRITE;
/*!40000 ALTER TABLE `tipo_endereco` DISABLE KEYS */;
INSERT INTO `tipo_endereco` VALUES ('AV','Avenida'),('RUA','Rua'),('TVR','Travessa');
/*!40000 ALTER TABLE `tipo_endereco` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tipo_garantia`
--

DROP TABLE IF EXISTS `tipo_garantia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tipo_garantia` (
  `cdtipo` int(11) NOT NULL AUTO_INCREMENT,
  `notipo` varchar(50) NOT NULL,
  `cdempresa` int(11) NOT NULL,
  PRIMARY KEY (`cdtipo`),
  KEY `fk_tipo_gar_empresa` (`cdempresa`),
  CONSTRAINT `fk_tipo_gar_empresa` FOREIGN KEY (`cdempresa`) REFERENCES `empresa` (`CDEMPRESA`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tipo_garantia`
--

LOCK TABLES `tipo_garantia` WRITE;
/*!40000 ALTER TABLE `tipo_garantia` DISABLE KEYS */;
INSERT INTO `tipo_garantia` VALUES (1,'Depósito Judicial',1),(2,'Imóvel',1),(3,'Carro',1);
/*!40000 ALTER TABLE `tipo_garantia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tipo_pagamento`
--

DROP TABLE IF EXISTS `tipo_pagamento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tipo_pagamento` (
  `cdtipo` int(11) NOT NULL AUTO_INCREMENT,
  `notipo` varchar(100) NOT NULL,
  `cdempresa` int(11) NOT NULL,
  PRIMARY KEY (`cdtipo`),
  KEY `fk_tipo_pagto_empresa` (`cdempresa`),
  CONSTRAINT `fk_tipo_pagto_empresa` FOREIGN KEY (`cdempresa`) REFERENCES `empresa` (`CDEMPRESA`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tipo_pagamento`
--

LOCK TABLES `tipo_pagamento` WRITE;
/*!40000 ALTER TABLE `tipo_pagamento` DISABLE KEYS */;
INSERT INTO `tipo_pagamento` VALUES (1,'Danos Morais',1),(2,'Danos Materiais',1),(4,'Honorarios',1),(6,'Juros e Correções Monetário',1);
/*!40000 ALTER TABLE `tipo_pagamento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usuario` (
  `cdusuario` int(11) NOT NULL AUTO_INCREMENT,
  `nousuario` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `senha` varchar(200) NOT NULL,
  `cpf` varchar(20) DEFAULT NULL,
  `cdsituacao` int(11) NOT NULL,
  `endereco` varchar(200) DEFAULT NULL,
  `numero` varchar(10) DEFAULT NULL,
  `complemento` varchar(50) DEFAULT NULL,
  `cep` varchar(20) DEFAULT NULL,
  `nobairro` varchar(100) DEFAULT NULL,
  `cdcidade` int(11) NOT NULL,
  `telefone1` varchar(20) NOT NULL,
  `telefone2` varchar(20) DEFAULT NULL,
  `dtcadastro` date NOT NULL,
  `cdempresa` int(11) NOT NULL,
  `cdperfil` int(11) NOT NULL,
  `foto` longblob,
  `status_login` varchar(1) NOT NULL,
  PRIMARY KEY (`cdusuario`),
  KEY `fk_empresa_usuario` (`cdempresa`),
  KEY `fk_perfil_usuario` (`cdperfil`),
  CONSTRAINT `fk_empresa_usuario` FOREIGN KEY (`cdempresa`) REFERENCES `empresa` (`CDEMPRESA`),
  CONSTRAINT `fk_perfil_usuario` FOREIGN KEY (`cdperfil`) REFERENCES `perfil` (`cdperfil`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES (8,'Kleiton Vasconcelos Costa','vasconcelos.kleiton@gmail.com','550237b8fbcdf3741bb1127d0fc7f6bf','03564942416',0,NULL,NULL,NULL,NULL,NULL,2,'83981300623',NULL,'2021-09-24',1,1,NULL,'C');
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario_grupo_trabalho`
--

DROP TABLE IF EXISTS `usuario_grupo_trabalho`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usuario_grupo_trabalho` (
  `cdcontrole` int(11) NOT NULL AUTO_INCREMENT,
  `cdgrupo` int(11) NOT NULL,
  `cdusuario` int(11) NOT NULL,
  PRIMARY KEY (`cdcontrole`),
  KEY `fk_grupo_trabalho_gr` (`cdgrupo`),
  KEY `fk_grupo_trabalho_usr` (`cdusuario`),
  CONSTRAINT `fk_grupo_trabalho_gr` FOREIGN KEY (`cdgrupo`) REFERENCES `grupo_trabalho` (`CDGRUPO`),
  CONSTRAINT `fk_grupo_trabalho_usr` FOREIGN KEY (`cdusuario`) REFERENCES `usuario` (`cdusuario`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario_grupo_trabalho`
--

LOCK TABLES `usuario_grupo_trabalho` WRITE;
/*!40000 ALTER TABLE `usuario_grupo_trabalho` DISABLE KEYS */;
INSERT INTO `usuario_grupo_trabalho` VALUES (9,23,8);
/*!40000 ALTER TABLE `usuario_grupo_trabalho` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'ilaw'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-02-05 18:14:46
