-- phpMyAdmin SQL Dump
-- version 4.9.5
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Feb 04, 2026 at 03:04 AM
-- Server version: 5.7.24
-- PHP Version: 7.4.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `proyecto_bdii`
--

DELIMITER $$
--
-- Procedures
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_REGISTRAR_VENTA` (IN `_idVenta` VARCHAR(15), IN `_idCliente` VARCHAR(15), IN `_idProd` VARCHAR(10), IN `_cant` INT, IN `_precio` DECIMAL(10,2))  BEGIN
    DECLARE _subtotal DECIMAL(10,2);
    SET _subtotal = _cant * _precio;

    
    START TRANSACTION;

    
    
    INSERT IGNORE INTO VENTA (IDVENTA, FECHAVENTA, TOTAL, IDCLIENTE) 
    VALUES (_idVenta, CURDATE(), 0, _idCliente);

    
    
    INSERT INTO DETALLEVENTA (IDDETALLE, IDVENTA, IDPRODUCTO, CANTIDAD, PRECIO_UNITARIO, SUBTOTAL)
    VALUES (CONCAT(_idVenta, '_', _idProd, '_', UNIX_TIMESTAMP()), _idVenta, _idProd, _cant, _precio, _subtotal);

    
    UPDATE VENTA SET TOTAL = TOTAL + _subtotal WHERE IDVENTA = _idVenta;

    
    COMMIT;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_REPORTE_ESTADISTICO` (IN `_fechaInicio` DATE, IN `_fechaFin` DATE)  BEGIN
    SELECT 
        SUM(TOTAL) AS TOTAL_INGRESOS,
        (SELECT P.NOMBREPRODUCTO 
         FROM DETALLEVENTA D
         JOIN PRODUCTO P ON D.IDPRODUCTO = P.IDPRODUCTO
         JOIN VENTA V ON D.IDVENTA = V.IDVENTA
         WHERE V.FECHAVENTA BETWEEN _fechaInicio AND _fechaFin
         GROUP BY P.NOMBREPRODUCTO
         ORDER BY SUM(D.CANTIDAD) DESC
         LIMIT 1) AS PRODUCTO_MAS_VENDIDO
    FROM VENTA
    WHERE FECHAVENTA BETWEEN _fechaInicio AND _fechaFin;
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `bitacora_precios`
--

CREATE TABLE `bitacora_precios` (
  `ID_BITACORA` int(11) NOT NULL,
  `IDPRODUCTO` varchar(10) DEFAULT NULL,
  `PRECIO_ANTERIOR` decimal(10,2) DEFAULT NULL,
  `PRECIO_NUEVO` decimal(10,2) DEFAULT NULL,
  `FECHA` datetime DEFAULT NULL,
  `USUARIO` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `bitacora_precios`
--

INSERT INTO `bitacora_precios` (`ID_BITACORA`, `IDPRODUCTO`, `PRECIO_ANTERIOR`, `PRECIO_NUEVO`, `FECHA`, `USUARIO`) VALUES
(1, 'P9', '1000.00', '999.00', '2026-01-29 12:56:05', 'root@localhost'),
(2, 'P7', '17679.00', '16000.00', '2026-01-29 13:47:43', 'root@localhost'),
(3, 'P2', '500.00', '520.00', '2026-01-30 08:51:24', 'root@localhost'),
(4, 'P4', '32000.00', '4.00', '2026-01-30 10:14:38', 'root@localhost'),
(5, 'P9', '999.00', '1500.00', '2026-01-30 18:36:31', 'root@localhost');

-- --------------------------------------------------------

--
-- Table structure for table `cliente`
--

CREATE TABLE `cliente` (
  `IDCLIENTE` varchar(10) NOT NULL,
  `NOMBRECLIENTE` varchar(100) DEFAULT NULL,
  `APELLIDOCLIENTE` varchar(100) DEFAULT NULL,
  `TELEFONO` varchar(10) DEFAULT NULL,
  `DIRECCION` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `cliente`
--

INSERT INTO `cliente` (`IDCLIENTE`, `NOMBRECLIENTE`, `APELLIDOCLIENTE`, `TELEFONO`, `DIRECCION`) VALUES
('C1', 'ANA MARÍA', 'SANDOVAL ESQUIVEL', '9712478658', 'AV. JOAQUIN AMARO #56 COLONIA SAN PEDRO, CD. IXTEPEC OAXACA'),
('C2', 'LUIS AMARO', 'FELIX CASTELLANO', '9711224659', 'CALLE VICENTE GUERRERO #40 COLONIA ESTACIÓN, CD. IXTEPEC OAXACA'),
('C3', 'GUILLERMO', 'HERRERA LLANEZ', '9711456790', 'UNIDAD MILITAR º15, LOTE 82 Nº 25, CD. JUAREZ'),
('C4', 'FELIPE', 'ENRIQUEZ GUESS', '9711265987', 'AV. MORELOS #47 COLONIA MODERNA, JUCHITAN DE ZARAGOZA OAXACA '),
('C5', 'MIRELLA', 'GOMEZ LOPEZ', '9711436848', 'AV. 16 SEPTIEMBRE #46 COLONIA NOGALES CD. OBREGON'),
('C6', 'JOSÉ ANTONIO', 'CARRASCO HERNANDEZ', '9711564763', 'AV. FRANCISCO I. MADERO #47'),
('C7', 'CARLOS', 'BENITEZ GONZALES', '9711267890', 'CALLE NICOLAS BRAVO #57, COLONIA LAURELES, CD. JUAREZ'),
('C8', 'JOSE', 'MARTINEZ GUTIERREZ', '9711458765', 'CALLE FRANCISCO I MADERO');

-- --------------------------------------------------------

--
-- Table structure for table `compra`
--

CREATE TABLE `compra` (
  `FK_IDVENTA` varchar(2) NOT NULL,
  `FK_IDCLIENTE` varchar(2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `detalleventa`
--

CREATE TABLE `detalleventa` (
  `IDDETALLE` varchar(25) NOT NULL,
  `IDVENTA` varchar(15) DEFAULT NULL,
  `IDPRODUCTO` varchar(10) DEFAULT NULL,
  `CANTIDAD` int(11) NOT NULL,
  `PRECIO_UNITARIO` decimal(10,2) DEFAULT NULL,
  `SUBTOTAL` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `detalleventa`
--

INSERT INTO `detalleventa` (`IDDETALLE`, `IDVENTA`, `IDPRODUCTO`, `CANTIDAD`, `PRECIO_UNITARIO`, `SUBTOTAL`) VALUES
('D1-V10', 'V10', 'P2', 2, '500.00', 1000),
('D1-V2', 'V2', 'P4', 5, '32000.00', 160000),
('D1-V3', 'V3', 'P6', 3, '997.00', 2991),
('D1-V4', 'V4', 'P3', 6, '17599.00', 105594),
('D1-V5', 'V5', 'P5', 6, '2500.00', 15000),
('D1-V6', 'V6', 'P4', 4, '32000.00', 128000),
('D2-V2', 'V2', 'P3', 5, '17599.00', 87995),
('D2-V3', 'V3', 'P6', 1, '997.00', 997),
('D2-V4', 'V4', 'P3', 3, '17599.00', 52797),
('DV3', 'V5', 'P1', 1, '200.00', 200),
('D_PRUEBA_FINAL', 'V8', 'P1', 2, '10.00', 20),
('V1000_P1_1769789739', 'V1000', 'P1', 6, '7000.00', 42000),
('V1000_P4_1769821462', 'V1000', 'P4', 3, '4.00', 12),
('V1000_P5_1769821532', 'V1000', 'P5', 2, '2500.00', 5000),
('V1000_P7_1769821456', 'V1000', 'P7', 3, '16000.00', 48000),
('V100_P1_1769714937', 'V100', 'P1', 2, '50.00', 100),
('V12-D1', 'V12', 'P9', 2, '1000.00', 2000),
('V13-D1', 'V13', 'P9', 2, '1000.00', 2000),
('V14-D1', 'V14', 'P9', 2, '1000.00', 2000),
('V7-D1', 'V7', 'P6', 1, '997.00', 997),
('V8-D1', 'V8', 'P1', 1, '7000.00', 7000),
('V8-D2', 'V8', 'P5', 1, '2500.00', 2500),
('V8-D3', 'V8', 'P7', 1, '17679.00', 17679),
('V9-D1', 'V9', 'P9', 3, '1000.00', 3000);

--
-- Triggers `detalleventa`
--
DELIMITER $$
CREATE TRIGGER `TRG_ACTUALIZAR_STOCK` AFTER INSERT ON `detalleventa` FOR EACH ROW BEGIN
    
    UPDATE PRODUCTO 
    SET STOCK = STOCK - NEW.CANTIDAD 
    WHERE IDPRODUCTO = NEW.IDPRODUCTO;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `TRG_VALIDAR_STOCK` BEFORE INSERT ON `detalleventa` FOR EACH ROW BEGIN
    DECLARE stock_actual INT;

    
    SELECT STOCK INTO stock_actual 
    FROM PRODUCTO 
    WHERE IDPRODUCTO = NEW.IDPRODUCTO;

    
    IF stock_actual < NEW.CANTIDAD THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Error: Existencias insuficientes para este producto';
    END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `incluye`
--

CREATE TABLE `incluye` (
  `FK_IDPRODUCTO` varchar(2) NOT NULL,
  `FK_IDVENTA` varchar(2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `producto`
--

CREATE TABLE `producto` (
  `IDPRODUCTO` varchar(10) NOT NULL,
  `NOMBREPRODUCTO` varchar(100) DEFAULT NULL,
  `DESCRIPCION` varchar(100) DEFAULT NULL,
  `PRECIO` decimal(10,2) DEFAULT NULL,
  `STOCK` int(11) DEFAULT NULL,
  `CODIGOBARRAS` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `producto`
--

INSERT INTO `producto` (`IDPRODUCTO`, `NOMBREPRODUCTO`, `DESCRIPCION`, `PRECIO`, `STOCK`, `CODIGOBARRAS`) VALUES
('P1', 'MONITOR LG', '32\'\' PANEL IPS 4K', '7000.00', 0, NULL),
('P10', 'LAPTOP', '2TB RAM , 1T SSD, ', '30000.00', 20, NULL),
('P2', 'MOUSE AJAZZ', 'INALAMBRICO 2.4Gz', '520.00', 194, NULL),
('P3', 'LAPTOP LENOVO', '16GB RAM, 512GB SSD, 15\'\' COLOR NEGRO', '17599.00', 0, NULL),
('P4', 'GOOGLE PIXEL 10PRO XL', 'COLOR BLANCO, 16GB RAM, 1TB MEMORIA INTERNA', '4.00', 12, NULL),
('P5', 'TECLADO RED DRAGON', 'MECANICO, GAMER, COLOR NEGRO, RGB, ALAMBRICO', '2500.00', 0, NULL),
('P6', 'AUDIFONOS JBL', 'INALAMBRICOS, NEGROS, CANCELACION RUIDO', '997.00', 0, NULL),
('P7', 'IMPRESORA HP', 'FUNCIONES: IMPRESION, ESCANEO Y COPIADO. COLOR NEGRO', '16000.00', 27, NULL),
('P8', 'MINI PC ACEMAGIC', 'RYZEN 9 9240, 32GB RAM, 1TR SSD', '19599.00', 20, NULL),
('P9', 'TECLADO AJAZZ', 'MAGNÉTICO, GAMER, RGB PROGRAMABLE, 60%', '1500.00', 6, NULL);

--
-- Triggers `producto`
--
DELIMITER $$
CREATE TRIGGER `TRG_AUDIT_PRECIO` BEFORE UPDATE ON `producto` FOR EACH ROW BEGIN
    
    IF OLD.PRECIO <> NEW.PRECIO THEN
        
        
        INSERT INTO BITACORA_PRECIOS (IDPRODUCTO, PRECIO_ANTERIOR, PRECIO_NUEVO, FECHA, USUARIO)
        VALUES (OLD.IDPRODUCTO, OLD.PRECIO, NEW.PRECIO, NOW(), USER());
        
    END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `venta`
--

CREATE TABLE `venta` (
  `IDVENTA` varchar(15) NOT NULL,
  `FECHAVENTA` date DEFAULT NULL,
  `TOTAL` decimal(10,2) DEFAULT NULL,
  `IDCLIENTE` varchar(15) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `venta`
--

INSERT INTO `venta` (`IDVENTA`, `FECHAVENTA`, `TOTAL`, `IDCLIENTE`) VALUES
('V10', '2026-01-21', '1000.00', NULL),
('V100', '2026-01-29', '100.00', 'C1'),
('V1000', '2026-01-29', '95012.00', 'C6'),
('V12', '2026-01-28', '2000.00', NULL),
('V13', '2026-01-28', '2000.00', NULL),
('V14', '2026-01-28', '2000.00', NULL),
('V2', '2025-11-25', '247995.00', NULL),
('V3', '2025-11-25', '3988.00', NULL),
('V4', '2025-11-25', '158391.00', NULL),
('V5', '2025-11-25', '15000.00', NULL),
('V6', '2026-01-15', '128000.00', NULL),
('V7', '2026-01-20', '997.00', NULL),
('V8', '2026-01-21', '27179.00', NULL),
('V9', '2026-01-21', '3000.00', NULL),
('V999', '2026-01-29', '0.00', 'C1');

-- --------------------------------------------------------

--
-- Stand-in structure for view `vista_catalogo_publico`
-- (See below for the actual view)
--
CREATE TABLE `vista_catalogo_publico` (
`idProducto` varchar(10)
,`nombreProducto` varchar(100)
,`precio` decimal(10,2)
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `vista_total_ventas`
-- (See below for the actual view)
--
CREATE TABLE `vista_total_ventas` (
`producto` varchar(100)
,`TotalVendido` decimal(32,0)
,`ngresoTotal` decimal(42,2)
);

-- --------------------------------------------------------

--
-- Structure for view `vista_catalogo_publico`
--
DROP TABLE IF EXISTS `vista_catalogo_publico`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `vista_catalogo_publico`  AS  select `producto`.`IDPRODUCTO` AS `idProducto`,`producto`.`NOMBREPRODUCTO` AS `nombreProducto`,`producto`.`PRECIO` AS `precio` from `producto` where (`producto`.`STOCK` > 0) ;

-- --------------------------------------------------------

--
-- Structure for view `vista_total_ventas`
--
DROP TABLE IF EXISTS `vista_total_ventas`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `vista_total_ventas`  AS  select `producto`.`NOMBREPRODUCTO` AS `producto`,sum(`detalleventa`.`CANTIDAD`) AS `TotalVendido`,sum((`detalleventa`.`CANTIDAD` * `producto`.`PRECIO`)) AS `ngresoTotal` from (`detalleventa` join `producto` on((`detalleventa`.`IDPRODUCTO` = `producto`.`IDPRODUCTO`))) group by `producto`.`NOMBREPRODUCTO` ;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `bitacora_precios`
--
ALTER TABLE `bitacora_precios`
  ADD PRIMARY KEY (`ID_BITACORA`);

--
-- Indexes for table `cliente`
--
ALTER TABLE `cliente`
  ADD PRIMARY KEY (`IDCLIENTE`),
  ADD KEY `idx_nombre_cliente` (`NOMBRECLIENTE`);

--
-- Indexes for table `compra`
--
ALTER TABLE `compra`
  ADD PRIMARY KEY (`FK_IDVENTA`,`FK_IDCLIENTE`),
  ADD KEY `COMPRA_FK_IDCLIENTE` (`FK_IDCLIENTE`);

--
-- Indexes for table `detalleventa`
--
ALTER TABLE `detalleventa`
  ADD PRIMARY KEY (`IDDETALLE`),
  ADD KEY `IDVENTA` (`IDVENTA`),
  ADD KEY `IDPRODUCTO` (`IDPRODUCTO`);

--
-- Indexes for table `incluye`
--
ALTER TABLE `incluye`
  ADD PRIMARY KEY (`FK_IDPRODUCTO`,`FK_IDVENTA`),
  ADD KEY `INCLUYE_FK_IDVENTA` (`FK_IDVENTA`);

--
-- Indexes for table `producto`
--
ALTER TABLE `producto`
  ADD PRIMARY KEY (`IDPRODUCTO`),
  ADD UNIQUE KEY `CODIGOBARRAS` (`CODIGOBARRAS`);

--
-- Indexes for table `venta`
--
ALTER TABLE `venta`
  ADD PRIMARY KEY (`IDVENTA`),
  ADD KEY `idx_fecha_venta` (`FECHAVENTA`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `bitacora_precios`
--
ALTER TABLE `bitacora_precios`
  MODIFY `ID_BITACORA` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `compra`
--
ALTER TABLE `compra`
  ADD CONSTRAINT `COMPRA_FK_IDCLIENTE` FOREIGN KEY (`FK_IDCLIENTE`) REFERENCES `cliente` (`IDCLIENTE`) ON DELETE CASCADE,
  ADD CONSTRAINT `COMPRA_FK_IDVENTA` FOREIGN KEY (`FK_IDVENTA`) REFERENCES `venta` (`IDVENTA`) ON DELETE CASCADE;

--
-- Constraints for table `detalleventa`
--
ALTER TABLE `detalleventa`
  ADD CONSTRAINT `detalleventa_ibfk_1` FOREIGN KEY (`IDVENTA`) REFERENCES `venta` (`IDVENTA`),
  ADD CONSTRAINT `detalleventa_ibfk_2` FOREIGN KEY (`IDPRODUCTO`) REFERENCES `producto` (`IDPRODUCTO`);

--
-- Constraints for table `incluye`
--
ALTER TABLE `incluye`
  ADD CONSTRAINT `INCLUYE_FK_IDPRODUCTO` FOREIGN KEY (`FK_IDPRODUCTO`) REFERENCES `producto` (`IDPRODUCTO`) ON DELETE CASCADE,
  ADD CONSTRAINT `INCLUYE_FK_IDVENTA` FOREIGN KEY (`FK_IDVENTA`) REFERENCES `venta` (`IDVENTA`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
