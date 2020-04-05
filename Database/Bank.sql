-- (personen)
-- PERSOONSNR PRIMARY KEY
-- NAAM
-- VOORLETTERS
-- GEB_DATUM
-- GESLACHT
-- STRAAT
-- HUISNR
-- POSTCODE
-- PLAATS
-- TELEFOON
-- 
-- (accounts)
-- ACCOUNTNR PRIMARY KEY
-- PERSOONSNR
-- BEDRAG
-- BANK
-- 
-- (pinpassen)
-- PASNR PRIMARY KEY
-- PERSOONSNR
-- PASUID
-- PINCODE
-- AANTALPOGINGEN
-- 
-- (transactielogs)
-- TRANSACTIEID PRIMARY KEY
-- VANACCOUNT
-- NAARACCOUNT
-- BEDRAG
-- DATUM
-- TIJD



CREATE DATABASE bank;
USE bank;

-- ----------------------------
--  Table structure for `personen`
-- ----------------------------
DROP TABLE IF EXISTS personen;
CREATE TABLE personen (
	PERSOONSNR INTEGER NOT NULL,
	NAAM CHAR(30) NOT NULL,
	GEB_DATUM DATE,
	GESLACHT CHAR(1) NOT NULL,
	STRAAT VARCHAR(30),
	HUISNR CHAR(4),
	POSTCODE CHAR(6),
	PLAATS VARCHAR(30) NOT NULL,
	TELEFOON CHAR(13),
	PRIMARY KEY (PERSOONSNR)
);

-- ----------------------------
--  Records of `personen`
-- ----------------------------
INSERT INTO `personen` VALUES 
('1', 'Daniël van der Drift', '1999-05-13', 'M', NULL, NULL, NULL, 'Delft', NULL),
('2', 'Zoë Zegers', '2000-01-01', 'F', NULL, NULL, NULL, 'Ergens', NULL);


-- ----------------------------
--  Table structure for `accounts`
-- ----------------------------
DROP TABLE IF EXISTS accounts;
CREATE TABLE accounts(
	ACCOUNTNR INTEGER NOT NULL,
	PERSOONSNR INTEGER NOT NULL,
	BEDRAG INTEGER NOT NULL,
	BANK CHAR(20) NOT NULL,
	PRIMARY KEY (ACCOUNTNR)
);

-- ----------------------------
--  Records of `accounts`
-- ----------------------------
INSERT INTO `accounts` VALUES 
('1', '1', '100', 'Onze bank'),
('2', '2', '200', 'Andere bank');


-- ----------------------------
--  Table structure for `pinpassen`
-- ----------------------------
DROP TABLE IF EXISTS pinpassen;
CREATE TABLE pinpassen(
	PASNR INTEGER NOT NULL,
	PERSOONSNR INTEGER NOT NULL,
	PASUID CHAR(8) NOT NULL,
	PINCODE CHAR(4) NOT NULL,
	AANTALPOGINGEN TINYINT NOT NULL,
	PRIMARY KEY (PASNR)
);

-- ----------------------------
--  Records of `pinpassen`
-- ----------------------------
INSERT INTO `pinpassen` VALUES 
('1', '1', '1A2B3C4D', '1234', '0'),
('2', '2', '5E6F7G8H', '0000', '0');


-- ----------------------------
--  Table structure for `transactielogs`
-- ----------------------------
DROP TABLE IF EXISTS `transactielogs`;
CREATE TABLE transactielogs(
	TRANSACTIEID INTEGER NOT NULL,
	VANACCOUNT INTEGER NOT NULL,
	NAARACCOUNT INTEGER NOT NULL,
	BEDRAG INTEGER NOT NULL,
	DATUM DATE NOT NULL,
	TIJD TIME NOT NULL,
	PRIMARY KEY (TRANSACTIEID)
);

-- ----------------------------
--  Records of `transactielogs`
-- ----------------------------
INSERT INTO `transactielogs` VALUES 
('1', '1', '2', '50', '2020-03-13', '01:00:00');


