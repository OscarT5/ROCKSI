-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: localhost    Database: rocksi
-- ------------------------------------------------------
-- Server version	9.4.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `clase`
--

DROP TABLE IF EXISTS `clase`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `clase` (
  `ID_Clase` varchar(45) NOT NULL,
  `nombre` varchar(45) NOT NULL,
  `horario` varchar(45) NOT NULL,
  `cupoMaximo` int NOT NULL,
  `maestro` varchar(100) NOT NULL,
  `diasImpartida` varchar(150) NOT NULL,
  PRIMARY KEY (`ID_Clase`),
  UNIQUE KEY `nombre_UNIQUE` (`nombre`),
  CONSTRAINT `ID_Itemfk2` FOREIGN KEY (`ID_Clase`) REFERENCES `item` (`ID_Item`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clase`
--

LOCK TABLES `clase` WRITE;
/*!40000 ALTER TABLE `clase` DISABLE KEYS */;
INSERT INTO `clase` VALUES ('CLA1000','Escalado','12:00-13:30',10,'Jose Torres de la Cruz','Jueves y viernes'),('CLA1002','Spinning','13:03-14:03',15,'Abad','Lunes, Martes y Jueves'),('CLA1006','Escalado2','15:38-16:32',12,'Luis','Nunca');
/*!40000 ALTER TABLE `clase` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cliente`
--

DROP TABLE IF EXISTS `cliente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cliente` (
  `ID_Cliente` varchar(45) NOT NULL,
  `nombreCompleto` varchar(100) NOT NULL,
  `telefono` varchar(15) NOT NULL,
  `credito` double NOT NULL,
  `fechaRegistro` date NOT NULL,
  `sexo` enum('masculino','femenino') NOT NULL,
  `segundoTelefono` varchar(15) DEFAULT NULL,
  `cantidadDineroMensual` double NOT NULL,
  `estatus` tinyint NOT NULL DEFAULT '1',
  PRIMARY KEY (`ID_Cliente`),
  UNIQUE KEY `telefono_UNIQUE` (`telefono`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cliente`
--

LOCK TABLES `cliente` WRITE;
/*!40000 ALTER TABLE `cliente` DISABLE KEYS */;
INSERT INTO `cliente` VALUES ('CLI1000','Luis Fernando Espinoza Diaz','6863894372',0,'2025-11-28','masculino',NULL,800,1),('CLI1001','Abraham Flores','6861003080',5000,'2025-11-28','masculino',NULL,1100,1),('CLI68','Apertura de Caja','1231231231',0,'2025-11-21','masculino',NULL,0,0),('CLI69','Retiro de Caja','6969696969',0,'2025-11-28','masculino',NULL,0,0),('CLI999','NADA','1212123123',0,'2025-11-25','masculino',NULL,0,0);
/*!40000 ALTER TABLE `cliente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cortecaja`
--

DROP TABLE IF EXISTS `cortecaja`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cortecaja` (
  `ID_Corte` varchar(45) NOT NULL,
  `fecha` date NOT NULL,
  `totalDiario` double NOT NULL,
  `ID_UsuarioRecep` varchar(45) NOT NULL,
  PRIMARY KEY (`ID_Corte`),
  KEY `ID_UsuarioRecepfk_idx` (`ID_UsuarioRecep`),
  CONSTRAINT `ID_UsuarioRecepfk` FOREIGN KEY (`ID_UsuarioRecep`) REFERENCES `usuariorecepcionista` (`ID_UsuarioRecep`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cortecaja`
--

LOCK TABLES `cortecaja` WRITE;
/*!40000 ALTER TABLE `cortecaja` DISABLE KEYS */;
/*!40000 ALTER TABLE `cortecaja` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `estainscrito`
--

DROP TABLE IF EXISTS `estainscrito`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `estainscrito` (
  `ID_Cliente` varchar(45) NOT NULL,
  `ID_Clase` varchar(45) NOT NULL,
  PRIMARY KEY (`ID_Cliente`,`ID_Clase`),
  KEY `ID_Clasefk1_idx` (`ID_Clase`),
  CONSTRAINT `ID_Clasefk1` FOREIGN KEY (`ID_Clase`) REFERENCES `clase` (`ID_Clase`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ID_Clientefk1` FOREIGN KEY (`ID_Cliente`) REFERENCES `cliente` (`ID_Cliente`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `estainscrito`
--

LOCK TABLES `estainscrito` WRITE;
/*!40000 ALTER TABLE `estainscrito` DISABLE KEYS */;
INSERT INTO `estainscrito` VALUES ('CLI1000','CLA1000'),('CLI1000','CLA1002'),('CLI1000','CLA1006');
/*!40000 ALTER TABLE `estainscrito` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inventario_diario`
--

DROP TABLE IF EXISTS `inventario_diario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `inventario_diario` (
  `id_inventario` int NOT NULL AUTO_INCREMENT,
  `fecha` date NOT NULL,
  `id_producto` varchar(45) NOT NULL,
  `stock_inicial` int NOT NULL,
  PRIMARY KEY (`id_inventario`),
  UNIQUE KEY `uk_producto_dia` (`fecha`,`id_producto`),
  KEY `id_productofk5_idx` (`id_producto`),
  CONSTRAINT `id_productofk5` FOREIGN KEY (`id_producto`) REFERENCES `producto` (`ID_Producto`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inventario_diario`
--

LOCK TABLES `inventario_diario` WRITE;
/*!40000 ALTER TABLE `inventario_diario` DISABLE KEYS */;
INSERT INTO `inventario_diario` VALUES (1,'2025-11-28','PR1000',4),(2,'2026-04-06','PR1000',2),(3,'2026-04-06','PR1001',12);
/*!40000 ALTER TABLE `inventario_diario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `item`
--

DROP TABLE IF EXISTS `item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `item` (
  `ID_Item` varchar(45) NOT NULL,
  `tipo` enum('membresia','producto','clase','credito','retirar') NOT NULL,
  `ID_UsuarioAdmin` varchar(45) NOT NULL,
  PRIMARY KEY (`ID_Item`),
  KEY `UsuarioAdminFK_idx` (`ID_UsuarioAdmin`),
  CONSTRAINT `ID_UsuarioAdminFK` FOREIGN KEY (`ID_UsuarioAdmin`) REFERENCES `usuarioadministrador` (`ID_UsuarioAdmin`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item`
--

LOCK TABLES `item` WRITE;
/*!40000 ALTER TABLE `item` DISABLE KEYS */;
INSERT INTO `item` VALUES ('AC1000','producto','ADM1000'),('CLA1000','clase','ADM1000'),('CLA1002','clase','ADM1000'),('CLA1006','clase','ADM1000'),('M1000','membresia','ADM1000'),('M1001','membresia','ADM1000'),('M1002','clase','ADM1000'),('M1003','clase','ADM1000'),('M1004','membresia','ADM1000'),('PAD1000','credito','ADM1000'),('PR1000','producto','ADM1000'),('PR1001','producto','ADM1000'),('RC1000','credito','ADM1000');
/*!40000 ALTER TABLE `item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `membresia`
--

DROP TABLE IF EXISTS `membresia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `membresia` (
  `ID_Membresia` varchar(45) NOT NULL,
  `fechaVencimiento` date NOT NULL,
  `ID_Cliente` varchar(45) NOT NULL,
  PRIMARY KEY (`ID_Membresia`),
  KEY `ID_Cliente_idx` (`ID_Cliente`),
  CONSTRAINT `ID_Cliente` FOREIGN KEY (`ID_Cliente`) REFERENCES `cliente` (`ID_Cliente`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ID_Item` FOREIGN KEY (`ID_Membresia`) REFERENCES `item` (`ID_Item`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `membresia`
--

LOCK TABLES `membresia` WRITE;
/*!40000 ALTER TABLE `membresia` DISABLE KEYS */;
INSERT INTO `membresia` VALUES ('M1000','2025-12-28','CLI1000'),('M1001','2025-12-28','CLI1001'),('M1002','2025-12-28','CLI1001'),('M1003','2026-05-06','CLI1000'),('M1004','2026-05-06','CLI1000');
/*!40000 ALTER TABLE `membresia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `paga`
--

DROP TABLE IF EXISTS `paga`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `paga` (
  `ID_Paga` varchar(45) NOT NULL,
  `ID_Cliente` varchar(45) NOT NULL,
  `ID_Item` varchar(45) NOT NULL,
  `ID_UsuarioRecep` varchar(45) NOT NULL,
  `fecha` date NOT NULL,
  `monto` double NOT NULL,
  `porPagar` tinyint NOT NULL,
  `observaciones` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID_Paga`),
  KEY `ID_Clientefk_idx` (`ID_Cliente`),
  KEY `ID_Itemfk4_idx` (`ID_Item`),
  CONSTRAINT `ID_Clientefk` FOREIGN KEY (`ID_Cliente`) REFERENCES `cliente` (`ID_Cliente`),
  CONSTRAINT `ID_Itemfk4` FOREIGN KEY (`ID_Item`) REFERENCES `item` (`ID_Item`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `paga`
--

LOCK TABLES `paga` WRITE;
/*!40000 ALTER TABLE `paga` DISABLE KEYS */;
INSERT INTO `paga` VALUES ('PA1000','CLI1000','M1000','UR1000','2025-11-28',800,0,NULL),('PA1001','CLI69','RC1000','UR1000','2025-11-28',-100,0,'Viáticos (Salida a MXLI)'),('PA1002','CLI1000','PR1000','UR1000','2025-11-28',50,1,NULL),('PA1003','CLI1001','M1001','UR1000','2025-11-28',800,0,NULL),('PA1004','CLI1001','PR1000','UR1000','2025-11-28',275,1,NULL),('PA1005','CLI1001','PAD1000','UR1000','2025-11-28',200,0,NULL),('PA1006','CLI68','AC1000','UR1000','2025-11-28',500,0,NULL),('PA1007','CLI1001','M1002','UR1000','2025-11-28',300,0,NULL),('PA1008','CLI1000','M1003','UR1000','2025-11-28',500,0,NULL),('PA1009','CLI1000','PR1000','UR1000','2026-01-27',50,0,NULL),('PA1010','CLI1000','PR1001','UR1000','2026-02-06',60,0,NULL),('PA1011','CLI1000','M1004','UR1001','2026-04-06',800,0,NULL),('PA1012','CLI1001','PR1000','UR1001','2026-04-06',50,1,NULL),('PA1013','CLI1001','PR1001','UR1001','2026-04-06',240,1,NULL),('PA1014','CLI1001','PAD1000','UR1001','2026-04-06',5000,0,NULL),('PA1015','CLI1000','M1003','UR1001','2026-04-06',500,0,NULL);
/*!40000 ALTER TABLE `paga` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `producto`
--

DROP TABLE IF EXISTS `producto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `producto` (
  `ID_Producto` varchar(45) NOT NULL,
  `nombre` varchar(45) NOT NULL,
  `stock` int NOT NULL,
  `precio` double NOT NULL,
  `proveedor` varchar(45) NOT NULL,
  `status` tinyint NOT NULL,
  PRIMARY KEY (`ID_Producto`),
  CONSTRAINT `ID_Itemfk` FOREIGN KEY (`ID_Producto`) REFERENCES `item` (`ID_Item`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `producto`
--

LOCK TABLES `producto` WRITE;
/*!40000 ALTER TABLE `producto` DISABLE KEYS */;
INSERT INTO `producto` VALUES ('PR1000','Coca-Cola 600ml',0,25,'Coca-Cola SA de CV',1),('PR1001','Pulparindo',0,20,'nestle',1);
/*!40000 ALTER TABLE `producto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reporte`
--

DROP TABLE IF EXISTS `reporte`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reporte` (
  `ID_Reporte` varchar(45) NOT NULL,
  `fechaGeneracion` date NOT NULL,
  `datos` varchar(500) NOT NULL,
  PRIMARY KEY (`ID_Reporte`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reporte`
--

LOCK TABLES `reporte` WRITE;
/*!40000 ALTER TABLE `reporte` DISABLE KEYS */;
/*!40000 ALTER TABLE `reporte` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarioadministrador`
--

DROP TABLE IF EXISTS `usuarioadministrador`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarioadministrador` (
  `ID_UsuarioAdmin` varchar(45) NOT NULL,
  `nombreCompleto` varchar(100) NOT NULL,
  `correo` varchar(60) NOT NULL,
  `contrasena` varchar(300) NOT NULL,
  `estatus` int DEFAULT '1',
  PRIMARY KEY (`ID_UsuarioAdmin`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarioadministrador`
--

LOCK TABLES `usuarioadministrador` WRITE;
/*!40000 ALTER TABLE `usuarioadministrador` DISABLE KEYS */;
INSERT INTO `usuarioadministrador` VALUES ('ADM1000','Farito','farito@rocksi.com','123',1);
/*!40000 ALTER TABLE `usuarioadministrador` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuariorecepcionista`
--

DROP TABLE IF EXISTS `usuariorecepcionista`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuariorecepcionista` (
  `ID_UsuarioRecep` varchar(45) NOT NULL,
  `nombreCompleto` varchar(100) NOT NULL,
  `correo` varchar(60) NOT NULL,
  `contrasena` varchar(300) NOT NULL,
  `estatus` tinyint NOT NULL DEFAULT '1',
  PRIMARY KEY (`ID_UsuarioRecep`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuariorecepcionista`
--

LOCK TABLES `usuariorecepcionista` WRITE;
/*!40000 ALTER TABLE `usuariorecepcionista` DISABLE KEYS */;
INSERT INTO `usuariorecepcionista` VALUES ('UR1000','Farita','farita@rocksi.com','123',1),('UR1001','Oscar','correo@uabc.edu.mx','123',1);
/*!40000 ALTER TABLE `usuariorecepcionista` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-04-12 14:36:06
