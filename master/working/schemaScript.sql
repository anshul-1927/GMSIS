BEGIN TRANSACTION;
CREATE TABLE vehicle_template (
  'ID'                   INTEGER PRIMARY KEY,
  'Type'                 TEXT NOT NULL,
  'template_model'       TEXT NOT NULL,
  'template_make'        TEXT NOT NULL,
  'template_engine_size' TEXT NOT NULL,
  'template_fuel_type'   TEXT NOT NULL
);
INSERT INTO `vehicle_template` (ID,Type,template_model,template_make,template_engine_size,template_fuel_type) VALUES (1,'Car','Golf','VolgsWagen','2.0','Diesel');
INSERT INTO `vehicle_template` (ID,Type,template_model,template_make,template_engine_size,template_fuel_type) VALUES (2,'Car','Polo','VolgsWagen','1.5','Diesel');
INSERT INTO `vehicle_template` (ID,Type,template_model,template_make,template_engine_size,template_fuel_type) VALUES (3,'Car','Maranelo','Ferrari','5.0','Petrol');
INSERT INTO `vehicle_template` (ID,Type,template_model,template_make,template_engine_size,template_fuel_type) VALUES (4,'Car','Fabia','Scoda','3.0','Diesel');
INSERT INTO `vehicle_template` (ID,Type,template_model,template_make,template_engine_size,template_fuel_type) VALUES (5,'Car','S5','Audi','4.5','Diesel');
INSERT INTO `vehicle_template` (ID,Type,template_model,template_make,template_engine_size,template_fuel_type) VALUES (6,'Car','A3','Audi','2.0','Petrol');
INSERT INTO `vehicle_template` (ID,Type,template_model,template_make,template_engine_size,template_fuel_type) VALUES (7,'Car','M5','BMW','2.0','Diesel');
INSERT INTO `vehicle_template` (ID,Type,template_model,template_make,template_engine_size,template_fuel_type) VALUES (8,'Car','325','BMW','2.0','Diesel');
INSERT INTO `vehicle_template` (ID,Type,template_model,template_make,template_engine_size,template_fuel_type) VALUES (9,'Car','XJ','Jaguar','4.2','Diesel');
INSERT INTO `vehicle_template` (ID,Type,template_model,template_make,template_engine_size,template_fuel_type) VALUES (10,'Car','XKR','Jaguar','5.0','Petrol');
INSERT INTO `vehicle_template` (ID,Type,template_model,template_make,template_engine_size,template_fuel_type) VALUES (11,'Truck','Altis','Scania','7.0','Diesel');
INSERT INTO `vehicle_template` (ID,Type,template_model,template_make,template_engine_size,template_fuel_type) VALUES (12,'Car','Race','Saleen','3.0','Diesel');
INSERT INTO `vehicle_template` (ID,Type,template_model,template_make,template_engine_size,template_fuel_type) VALUES (13,'Car','330','Porsche','5.0','Diesel');
INSERT INTO `vehicle_template` (ID,Type,template_model,template_make,template_engine_size,template_fuel_type) VALUES (14,'Truck','Blazer','Chevrolet','5.0','Diesel');
INSERT INTO `vehicle_template` (ID,Type,template_model,template_make,template_engine_size,template_fuel_type) VALUES (15,'Van','Fiorino','Fiat','1.3','Diesel');
INSERT INTO `vehicle_template` (ID,Type,template_model,template_make,template_engine_size,template_fuel_type) VALUES (17,'Truck','Infinity','Scania','2.9','Diesel');
INSERT INTO `vehicle_template` (ID,Type,template_model,template_make,template_engine_size,template_fuel_type) VALUES (18,'Car','XK','Jaguar','4.2','Diesel');
INSERT INTO `vehicle_template` (ID,Type,template_model,template_make,template_engine_size,template_fuel_type) VALUES (19,'Car','SLR','Mercedes','5.5','Petrol');
INSERT INTO `vehicle_template` (ID,Type,template_model,template_make,template_engine_size,template_fuel_type) VALUES (20,'Car','F1','FERRARI','5.9','Petrol');
INSERT INTO `vehicle_template` (ID,Type,template_model,template_make,template_engine_size,template_fuel_type) VALUES (21,'Car','Venus','Bentley','5.5','Diesel');
INSERT INTO `vehicle_template` (ID,Type,template_model,template_make,template_engine_size,template_fuel_type) VALUES (22,'Truck','Mafia','Lexus','3.3','Diesel');
INSERT INTO `vehicle_template` (ID,Type,template_model,template_make,template_engine_size,template_fuel_type) VALUES (23,'Car','Viper','Dodge','1.3','Diesel');
INSERT INTO `vehicle_template` (ID,Type,template_model,template_make,template_engine_size,template_fuel_type) VALUES (24,'Truck','kayen','Porsche','5.3','Diesel');
INSERT INTO `vehicle_template` (ID,Type,template_model,template_make,template_engine_size,template_fuel_type) VALUES (25,'Car','Auris','Toyota','1.5','Diesel');
INSERT INTO `vehicle_template` (ID,Type,template_model,template_make,template_engine_size,template_fuel_type) VALUES (26,'Car','Abart','Fiat','1.8','Diesel');
INSERT INTO `vehicle_template` (ID,Type,template_model,template_make,template_engine_size,template_fuel_type) VALUES (27,'Car','Domine','Hundai','1.2','Diesel');
INSERT INTO `vehicle_template` (ID,Type,template_model,template_make,template_engine_size,template_fuel_type) VALUES (28,'Car','GTI','Fiat','2.0','Diesel');
INSERT INTO `vehicle_template` (ID,Type,template_model,template_make,template_engine_size,template_fuel_type) VALUES (29,'Car','Mount','Fiat','1.3','Diesel');
CREATE TABLE `Vehicle` (
'VehicleType' TEXT NOT NULL,
`VehicleRegNo`TEXT NOT NULL UNIQUE,
`VehicleModel`TEXT NOT NULL,
`VehicleMake`TEXT NOT NULL,
'VehicleMileage' TEXT NOT NULL,
`VehicleEngSize`TEXT NOT NULL,
`VehicleFuelType`TEXT NOT NULL,  
`VehicleColour`TEXT NOT NULL,
`VehicleMoTDate`TEXT NOT NULL,
'LastServiceDate' TEXT,
'WarrantyName' TEXT,
'WarrantyAddress' TEXT,
'WarrantyExpDate' TEXT,
'CustomerID' INTEGER NOT NULL,

  PRIMARY KEY(`VehicleRegNo`),
  FOREIGN KEY(CustomerID) REFERENCES Customer(CustomerID) 
  
  
);
INSERT INTO `Vehicle` (VehicleType,VehicleRegNo,VehicleModel,VehicleMake,VehicleMileage,VehicleEngSize,VehicleFuelType,VehicleColour,VehicleMoTDate,LastServiceDate,WarrantyName,WarrantyAddress,WarrantyExpDate,CustomerID) VALUES ('Car','TAA2523','Polo','VolgsWagen','12349','1.5','Diesel','Blue','2017/05/24',NULL,'ING','St Pauls London','2017/11/05',1);
INSERT INTO `Vehicle` (VehicleType,VehicleRegNo,VehicleModel,VehicleMake,VehicleMileage,VehicleEngSize,VehicleFuelType,VehicleColour,VehicleMoTDate,LastServiceDate,WarrantyName,WarrantyAddress,WarrantyExpDate,CustomerID) VALUES ('Car','ERJ2345','XJ','Jaguar','122221','4.2','Diesel','Green','2017/04/20',NULL,'ING','Belfast','2017/10/05',2);
INSERT INTO `Vehicle` (VehicleType,VehicleRegNo,VehicleModel,VehicleMake,VehicleMileage,VehicleEngSize,VehicleFuelType,VehicleColour,VehicleMoTDate,LastServiceDate,WarrantyName,WarrantyAddress,WarrantyExpDate,CustomerID) VALUES ('Car','TBB2378','Fabia','Scoda','10000','3.0','Diesel','Yellow','2017/04/30',NULL,'AXA','Westminster London','2017/09/04',3);
INSERT INTO `Vehicle` (VehicleType,VehicleRegNo,VehicleModel,VehicleMake,VehicleMileage,VehicleEngSize,VehicleFuelType,VehicleColour,VehicleMoTDate,LastServiceDate,WarrantyName,WarrantyAddress,WarrantyExpDate,CustomerID) VALUES ('Car','KHH9087','GTI','Fiat','100982','2.0','Diesel','White','2017/03/03',NULL,'AIG','Dalston Jackson London','2017/06/03',4);
INSERT INTO `Vehicle` (VehicleType,VehicleRegNo,VehicleModel,VehicleMake,VehicleMileage,VehicleEngSize,VehicleFuelType,VehicleColour,VehicleMoTDate,LastServiceDate,WarrantyName,WarrantyAddress,WarrantyExpDate,CustomerID) VALUES ('Car','KJH7865','Auris','Toyota','1239645','1.5','Diesel','Gray','2018/04/01',NULL,'ING','Canary Wharf London','2017/03/04',5);
INSERT INTO `Vehicle` (VehicleType,VehicleRegNo,VehicleModel,VehicleMake,VehicleMileage,VehicleEngSize,VehicleFuelType,VehicleColour,VehicleMoTDate,LastServiceDate,WarrantyName,WarrantyAddress,WarrantyExpDate,CustomerID) VALUES ('Car','JIK9864','Abart','Fiat','90000','1.8','Diesel','Green','2017/03/17',NULL,'AIG','Covent Garden London','2017/02/04',6);
INSERT INTO `Vehicle` (VehicleType,VehicleRegNo,VehicleModel,VehicleMake,VehicleMileage,VehicleEngSize,VehicleFuelType,VehicleColour,VehicleMoTDate,LastServiceDate,WarrantyName,WarrantyAddress,WarrantyExpDate,CustomerID) VALUES ('Truck','GHJ6753','kayen','Porsche','12000','5.3','Diesel','BLACK','2017/03/11',NULL,'AXA','St Pauls London','2017/10/04',7);
INSERT INTO `Vehicle` (VehicleType,VehicleRegNo,VehicleModel,VehicleMake,VehicleMileage,VehicleEngSize,VehicleFuelType,VehicleColour,VehicleMoTDate,LastServiceDate,WarrantyName,WarrantyAddress,WarrantyExpDate,CustomerID) VALUES ('Car','TAB2435','XKR','Jaguar','120000','5.0','Petrol','Gray','2017/03/05',NULL,'ING','St Pauls London','2017/12/04',8);
INSERT INTO `Vehicle` (VehicleType,VehicleRegNo,VehicleModel,VehicleMake,VehicleMileage,VehicleEngSize,VehicleFuelType,VehicleColour,VehicleMoTDate,LastServiceDate,WarrantyName,WarrantyAddress,WarrantyExpDate,CustomerID) VALUES ('Car','TRY7896','Viper','Dodge','1297563','1.3','Diesel','Blue','2017/03/12',NULL,'ING','Knightsbridge London','03/03/2017',9);
INSERT INTO `Vehicle` (VehicleType,VehicleRegNo,VehicleModel,VehicleMake,VehicleMileage,VehicleEngSize,VehicleFuelType,VehicleColour,VehicleMoTDate,LastServiceDate,WarrantyName,WarrantyAddress,WarrantyExpDate,CustomerID) VALUES ('Car','TYU3545','Venus','Bentley','190832','5.5','Diesel','Black','2017/04/02',NULL,'AXA','St Pauls London','05/05/2018',10);
INSERT INTO `Vehicle` (VehicleType,VehicleRegNo,VehicleModel,VehicleMake,VehicleMileage,VehicleEngSize,VehicleFuelType,VehicleColour,VehicleMoTDate,LastServiceDate,WarrantyName,WarrantyAddress,WarrantyExpDate,CustomerID) VALUES ('Truck','GHY6756','Mafia','Lexus','1238993','3.3','Diesel','Black','2017/03/04',NULL,'ING','Westmister London','2017/01/23',1);
INSERT INTO `Vehicle` (VehicleType,VehicleRegNo,VehicleModel,VehicleMake,VehicleMileage,VehicleEngSize,VehicleFuelType,VehicleColour,VehicleMoTDate,LastServiceDate,WarrantyName,WarrantyAddress,WarrantyExpDate,CustomerID) VALUES ('Car','JUI78987','SLR','Mercedes','2897546','5.5','Petrol','Black','2017/03/04',NULL,'ING','Canary Wharf London','2017/03/23',2);
INSERT INTO `Vehicle` (VehicleType,VehicleRegNo,VehicleModel,VehicleMake,VehicleMileage,VehicleEngSize,VehicleFuelType,VehicleColour,VehicleMoTDate,LastServiceDate,WarrantyName,WarrantyAddress,WarrantyExpDate,CustomerID) VALUES ('Truck','AKI8976','Infinity','Scania','124900','2.9','Diesel','Green','2017/03/18',NULL,'AXA','St Pauls London','2017/05/04',6);
INSERT INTO `Vehicle` (VehicleType,VehicleRegNo,VehicleModel,VehicleMake,VehicleMileage,VehicleEngSize,VehicleFuelType,VehicleColour,VehicleMoTDate,LastServiceDate,WarrantyName,WarrantyAddress,WarrantyExpDate,CustomerID) VALUES ('Car','AJI8907','Maranelo','Ferrari','120999','5.0','Petrol','Blue','2017/03/04',NULL,'ING','St Pauls London','2017/03/23',3);
INSERT INTO `Vehicle` (VehicleType,VehicleRegNo,VehicleModel,VehicleMake,VehicleMileage,VehicleEngSize,VehicleFuelType,VehicleColour,VehicleMoTDate,LastServiceDate,WarrantyName,WarrantyAddress,WarrantyExpDate,CustomerID) VALUES ('Car','AKI8954','Golf','VolgsWagen','123900','2.0','Diesel','Black','2017/03/19',NULL,'AIG','Canary Wharf London','2017/09/23',1);
INSERT INTO `Vehicle` (VehicleType,VehicleRegNo,VehicleModel,VehicleMake,VehicleMileage,VehicleEngSize,VehicleFuelType,VehicleColour,VehicleMoTDate,LastServiceDate,WarrantyName,WarrantyAddress,WarrantyExpDate,CustomerID) VALUES ('Car','AIO8977','Polo','VolgsWagen','1234500','1.5','Diesel','Green','2017/03/10',NULL,'ING','Canary Wharf London','2017/06/26',6);
INSERT INTO `Vehicle` (VehicleType,VehicleRegNo,VehicleModel,VehicleMake,VehicleMileage,VehicleEngSize,VehicleFuelType,VehicleColour,VehicleMoTDate,LastServiceDate,WarrantyName,WarrantyAddress,WarrantyExpDate,CustomerID) VALUES ('Car','UIO8976','A3','Audi','12099','2.0','Petrol','Bluew','2017/03/10',NULL,NULL,NULL,NULL,9);
INSERT INTO `Vehicle` (VehicleType,VehicleRegNo,VehicleModel,VehicleMake,VehicleMileage,VehicleEngSize,VehicleFuelType,VehicleColour,VehicleMoTDate,LastServiceDate,WarrantyName,WarrantyAddress,WarrantyExpDate,CustomerID) VALUES ('Van','AKI9087','Fiorino','Fiat','12897','1.3','Diesel','White','2017/03/10',NULL,NULL,NULL,NULL,6);
INSERT INTO `Vehicle` (VehicleType,VehicleRegNo,VehicleModel,VehicleMake,VehicleMileage,VehicleEngSize,VehicleFuelType,VehicleColour,VehicleMoTDate,LastServiceDate,WarrantyName,WarrantyAddress,WarrantyExpDate,CustomerID) VALUES ('Car ','AKI5632','Maranelo','Ferrari','120000','5.0','Petrol','Red','2017/03/10',NULL,NULL,NULL,NULL,4);
INSERT INTO `Vehicle` (VehicleType,VehicleRegNo,VehicleModel,VehicleMake,VehicleMileage,VehicleEngSize,VehicleFuelType,VehicleColour,VehicleMoTDate,LastServiceDate,WarrantyName,WarrantyAddress,WarrantyExpDate,CustomerID) VALUES ('Car','KKK9806','S5','Audi','890000','4.5','Diesel','Red','2017/03/03',NULL,NULL,NULL,NULL,5);
CREATE TABLE `User` (
`UserID` INTEGER PRIMARY KEY,
`UserFirstName`TEXT NOT NULL,
`UserLastName`TEXT NOT NULL,
`UserPassword`TEXT NOT NULL,
`UserIsAdmin`INTEGER NOT NULL

);
INSERT INTO `User` (UserID,UserFirstName,UserLastName,UserPassword,UserIsAdmin) VALUES (10000,'Efthymios','Chatziathanasiadis','password',1);
INSERT INTO `User` (UserID,UserFirstName,UserLastName,UserPassword,UserIsAdmin) VALUES (10001,'Ilyas','Mohamed','password',1);
INSERT INTO `User` (UserID,UserFirstName,UserLastName,UserPassword,UserIsAdmin) VALUES (10002,'Julius','Chiu','wew',1);
INSERT INTO `User` (UserID,UserFirstName,UserLastName,UserPassword,UserIsAdmin) VALUES (10003,'Mario','Tawfelis','password',1);
INSERT INTO `User` (UserID,UserFirstName,UserLastName,UserPassword,UserIsAdmin) VALUES (10004,'Loyd','Kennett','password',1);
INSERT INTO `User` (UserID,UserFirstName,UserLastName,UserPassword,UserIsAdmin) VALUES (10005,'Alex','Chardouvelis','password',0);
INSERT INTO `User` (UserID,UserFirstName,UserLastName,UserPassword,UserIsAdmin) VALUES (10006,'Daniel','Moreno','cyclelord',0);
INSERT INTO `User` (UserID,UserFirstName,UserLastName,UserPassword,UserIsAdmin) VALUES (10007,'Nasaussap','Saqqaa','icefjord',0);
INSERT INTO `User` (UserID,UserFirstName,UserLastName,UserPassword,UserIsAdmin) VALUES (10008,'Eric','Ro','koearn',0);
INSERT INTO `User` (UserID,UserFirstName,UserLastName,UserPassword,UserIsAdmin) VALUES (10009,'Ingar','Kolloen','jegkunne',0);
INSERT INTO `User` (UserID,UserFirstName,UserLastName,UserPassword,UserIsAdmin) VALUES (10010,'Ants','Oras','tartu',0);
INSERT INTO `User` (UserID,UserFirstName,UserLastName,UserPassword,UserIsAdmin) VALUES (10011,'Cameron','Davis','trakD',0);
INSERT INTO `User` (UserID,UserFirstName,UserLastName,UserPassword,UserIsAdmin) VALUES (10012,'Matt','Peacock','aussy73',0);
INSERT INTO `User` (UserID,UserFirstName,UserLastName,UserPassword,UserIsAdmin) VALUES (10013,'Tyrone','DeShawn','heyhey22',0);
INSERT INTO `User` (UserID,UserFirstName,UserLastName,UserPassword,UserIsAdmin) VALUES (10014,'Michael','Spacek','czechmoto',0);
CREATE TABLE "StockWithdrawals" (
	`PartID`	INTEGER NOT NULL,
	`Date`	TEXT NOT NULL,
	`Quantity`	INTEGER NOT NULL
);
INSERT INTO `StockWithdrawals` (PartID,Date,Quantity) VALUES (6,'18/03/2017',1);
INSERT INTO `StockWithdrawals` (PartID,Date,Quantity) VALUES (3,'23/03/2017',1);
INSERT INTO `StockWithdrawals` (PartID,Date,Quantity) VALUES (7,'02/04/2017',1);
INSERT INTO `StockWithdrawals` (PartID,Date,Quantity) VALUES (10,'30/03/2017',1);
INSERT INTO `StockWithdrawals` (PartID,Date,Quantity) VALUES (6,'26/03/2017',1);
INSERT INTO `StockWithdrawals` (PartID,Date,Quantity) VALUES (1,'21/03/2017',1);
INSERT INTO `StockWithdrawals` (PartID,Date,Quantity) VALUES (2,'22/03/2017',1);
INSERT INTO `StockWithdrawals` (PartID,Date,Quantity) VALUES (3,'22/03/2017',1);
INSERT INTO `StockWithdrawals` (PartID,Date,Quantity) VALUES (4,'23/03/2017',1);
INSERT INTO `StockWithdrawals` (PartID,Date,Quantity) VALUES (5,'29/03/2017',1);
INSERT INTO `StockWithdrawals` (PartID,Date,Quantity) VALUES (6,'08/04/2017',1);
INSERT INTO `StockWithdrawals` (PartID,Date,Quantity) VALUES (7,'30/03/2017',1);
INSERT INTO `StockWithdrawals` (PartID,Date,Quantity) VALUES (8,'19/04/2017',1);
INSERT INTO `StockWithdrawals` (PartID,Date,Quantity) VALUES (9,'06/04/2017',1);
INSERT INTO `StockWithdrawals` (PartID,Date,Quantity) VALUES (10,'06/04/2017',1);
INSERT INTO `StockWithdrawals` (PartID,Date,Quantity) VALUES (1,'29/03/2017',1);
INSERT INTO `StockWithdrawals` (PartID,Date,Quantity) VALUES (2,'02/04/2017',1);
INSERT INTO `StockWithdrawals` (PartID,Date,Quantity) VALUES (3,'05/04/2017',1);
INSERT INTO `StockWithdrawals` (PartID,Date,Quantity) VALUES (4,'25/05/2017',1);
INSERT INTO `StockWithdrawals` (PartID,Date,Quantity) VALUES (8,'15/03/2017',1);
INSERT INTO `StockWithdrawals` (PartID,Date,Quantity) VALUES (4,'29/03/2017',1);
INSERT INTO `StockWithdrawals` (PartID,Date,Quantity) VALUES (10,'02/04/2017',1);
INSERT INTO `StockWithdrawals` (PartID,Date,Quantity) VALUES (7,'07/04/2017',1);
INSERT INTO `StockWithdrawals` (PartID,Date,Quantity) VALUES (4,'24/05/2017',1);
INSERT INTO `StockWithdrawals` (PartID,Date,Quantity) VALUES (1,'25/03/2017',1);
CREATE TABLE "StockDeliveries" (
	`PartID`	INTEGER NOT NULL,
	`Date`	TEXT NOT NULL,
	`Quantity`	INTEGER NOT NULL
);
INSERT INTO `StockDeliveries` (PartID,Date,Quantity) VALUES (1,'2017-03-20',2);
INSERT INTO `StockDeliveries` (PartID,Date,Quantity) VALUES (2,'2017-03-17',6);
INSERT INTO `StockDeliveries` (PartID,Date,Quantity) VALUES (3,'2017-03-20',2);
INSERT INTO `StockDeliveries` (PartID,Date,Quantity) VALUES (4,'2017-03-20',3);
INSERT INTO `StockDeliveries` (PartID,Date,Quantity) VALUES (5,'2017-03-20',1);
INSERT INTO `StockDeliveries` (PartID,Date,Quantity) VALUES (6,'2017-03-17',3);
INSERT INTO `StockDeliveries` (PartID,Date,Quantity) VALUES (7,'2017-03-20',1);
INSERT INTO `StockDeliveries` (PartID,Date,Quantity) VALUES (8,'2017-03-20',2);
INSERT INTO `StockDeliveries` (PartID,Date,Quantity) VALUES (9,'2017-03-20',1);
INSERT INTO `StockDeliveries` (PartID,Date,Quantity) VALUES (10,'2017-03-20',2);
CREATE TABLE "SpecialistRepair" (
	`SRID`	INTEGER NOT NULL UNIQUE,
	`SRReturnDate`	TEXT NOT NULL,
	`SRDeliveryDate`	TEXT NOT NULL,
	`SRCost`	REAL,
	`PartID`	INTEGER,
	`BookingID`	INTEGER NOT NULL,
	`SPCID`	INTEGER NOT NULL,
	PRIMARY KEY(`SRID`),
	FOREIGN KEY(`PartID`) REFERENCES `Part`(`PartID`),
	FOREIGN KEY(`BookingID`) REFERENCES `Booking`(`BookingID`),
	FOREIGN KEY(`SPCID`) REFERENCES `SPC`(`SPCID`)
);
INSERT INTO `SpecialistRepair` (SRID,SRReturnDate,SRDeliveryDate,SRCost,PartID,BookingID,SPCID) VALUES (1,'2016-11-15','2016-11-12',143.67,2,1,7);
INSERT INTO `SpecialistRepair` (SRID,SRReturnDate,SRDeliveryDate,SRCost,PartID,BookingID,SPCID) VALUES (2,'2016-11-30','2016-11-26',126.1,5,3,10);
INSERT INTO `SpecialistRepair` (SRID,SRReturnDate,SRDeliveryDate,SRCost,PartID,BookingID,SPCID) VALUES (3,'2017-01-23','2017-01-15',250.0,NULL,4,5);
INSERT INTO `SpecialistRepair` (SRID,SRReturnDate,SRDeliveryDate,SRCost,PartID,BookingID,SPCID) VALUES (4,'2017-01-27','2017-01-20',150.78,1,7,6);
INSERT INTO `SpecialistRepair` (SRID,SRReturnDate,SRDeliveryDate,SRCost,PartID,BookingID,SPCID) VALUES (5,'2017-02-02','2017-01-28',123.45,NULL,9,5);
INSERT INTO `SpecialistRepair` (SRID,SRReturnDate,SRDeliveryDate,SRCost,PartID,BookingID,SPCID) VALUES (6,'2017-02-15','2017-02-10',321.56,NULL,10,2);
INSERT INTO `SpecialistRepair` (SRID,SRReturnDate,SRDeliveryDate,SRCost,PartID,BookingID,SPCID) VALUES (7,'2017-03-15','2017-03-05',102.99,3,15,8);
INSERT INTO `SpecialistRepair` (SRID,SRReturnDate,SRDeliveryDate,SRCost,PartID,BookingID,SPCID) VALUES (8,'2017-01-29','2017-01-10',267.87,5,2,10);
INSERT INTO `SpecialistRepair` (SRID,SRReturnDate,SRDeliveryDate,SRCost,PartID,BookingID,SPCID) VALUES (9,'2017-01-25','2017-01-07',150.67,6,6,5);
INSERT INTO `SpecialistRepair` (SRID,SRReturnDate,SRDeliveryDate,SRCost,PartID,BookingID,SPCID) VALUES (10,'2017-02-20','2017-02-08',198.37,9,12,2);
INSERT INTO `SpecialistRepair` (SRID,SRReturnDate,SRDeliveryDate,SRCost,PartID,BookingID,SPCID) VALUES (11,'2018-06-30','2018-06-20',120.87,5,16,10);
INSERT INTO `SpecialistRepair` (SRID,SRReturnDate,SRDeliveryDate,SRCost,PartID,BookingID,SPCID) VALUES (12,'2018-02-20','2018-02-12',230.92,3,26,8);
INSERT INTO `SpecialistRepair` (SRID,SRReturnDate,SRDeliveryDate,SRCost,PartID,BookingID,SPCID) VALUES (13,'2017-09-26','2017-09-11',183.61,8,21,9);
INSERT INTO `SpecialistRepair` (SRID,SRReturnDate,SRDeliveryDate,SRCost,PartID,BookingID,SPCID) VALUES (14,'2018-03-22','2018-03-13',299.99,NULL,23,5);
INSERT INTO `SpecialistRepair` (SRID,SRReturnDate,SRDeliveryDate,SRCost,PartID,BookingID,SPCID) VALUES (15,'2018-04-03','2018-03-07',238.67,1,19,6);
INSERT INTO `SpecialistRepair` (SRID,SRReturnDate,SRDeliveryDate,SRCost,PartID,BookingID,SPCID) VALUES (16,'2018-05-30','2018-05-15',150.0,NULL,20,2);
INSERT INTO `SpecialistRepair` (SRID,SRReturnDate,SRDeliveryDate,SRCost,PartID,BookingID,SPCID) VALUES (17,'2018-06-30','2018-06-13',180.0,2,28,7);
INSERT INTO `SpecialistRepair` (SRID,SRReturnDate,SRDeliveryDate,SRCost,PartID,BookingID,SPCID) VALUES (18,'2018-07-31','2018-07-04',123.45,9,26,5);
INSERT INTO `SpecialistRepair` (SRID,SRReturnDate,SRDeliveryDate,SRCost,PartID,BookingID,SPCID) VALUES (19,'2019-05-30','2019-03-06',432.98,7,25,6);
INSERT INTO `SpecialistRepair` (SRID,SRReturnDate,SRDeliveryDate,SRCost,PartID,BookingID,SPCID) VALUES (20,'2018-05-25','2018-05-02',285.5,5,28,10);
CREATE TABLE "SPC" (
	`SPCID`	INTEGER NOT NULL UNIQUE,
	`SPCName`	TEXT NOT NULL,
	`SPCAddress`	TEXT NOT NULL,
	`SPCPhoneNo`	TEXT NOT NULL,
	`SPCEmail`	TEXT NOT NULL,
	`SPCInUse`	INTEGER NOT NULL,
	PRIMARY KEY(`SPCID`)
);
INSERT INTO `SPC` (SPCID,SPCName,SPCAddress,SPCPhoneNo,SPCEmail,SPCInUse) VALUES (1,'BWM Repairs','70 Henriques St, E1 1LZ','07123456789','bmwrepairs@hotmail.com',1);
INSERT INTO `SPC` (SPCID,SPCName,SPCAddress,SPCPhoneNo,SPCEmail,SPCInUse) VALUES (2,'Audi Repairs','15 Vallance Rd, E1 5HS','07987654321','audirepairs@hotmail.com
',1);
INSERT INTO `SPC` (SPCID,SPCName,SPCAddress,SPCPhoneNo,SPCEmail,SPCInUse) VALUES (3,'Bentley Repairs','40 Holly St, E8 3XS','07847298164','bentleyrepairs@yahoo.co.uk',1);
INSERT INTO `SPC` (SPCID,SPCName,SPCAddress,SPCPhoneNo,SPCEmail,SPCInUse) VALUES (4,'Ferrari Repairs','3 Hawkwood Mount, E5 9EQ','07123475681','ferrari@gmail.com',1);
INSERT INTO `SPC` (SPCID,SPCName,SPCAddress,SPCPhoneNo,SPCEmail,SPCInUse) VALUES (5,'Jaguar Repairs','33 Kedleston Walk, E2 9RP','07918378654','jagrepairs@hotmail.com',1);
INSERT INTO `SPC` (SPCID,SPCName,SPCAddress,SPCPhoneNo,SPCEmail,SPCInUse) VALUES (6,'Fiat Repairs','4 Western Gateway, E16 5TB','07543298286','fiatrepairs@gmail.com',1);
INSERT INTO `SPC` (SPCID,SPCName,SPCAddress,SPCPhoneNo,SPCEmail,SPCInUse) VALUES (7,'Engine Specialist','58 Solander Gardens, E1 0DW','07854388761','specialengines@hotmail.com',1);
INSERT INTO `SPC` (SPCID,SPCName,SPCAddress,SPCPhoneNo,SPCEmail,SPCInUse) VALUES (8,'Turbo Specialist','55 Wellington Rd, E11 2AS','07234234654','specialturbos@hotmail.com',1);
INSERT INTO `SPC` (SPCID,SPCName,SPCAddress,SPCPhoneNo,SPCEmail,SPCInUse) VALUES (9,'Bumpers Specialist','26 Adelaide Rd, E11 2AS','07981346541','bumpsss@yahoo.com',1);
INSERT INTO `SPC` (SPCID,SPCName,SPCAddress,SPCPhoneNo,SPCEmail,SPCInUse) VALUES (10,'Brakes Specialist','77 Capworth St, E10 5AF','07019123751','bestbrakes@gmail.com',1);
CREATE TABLE 'Parts_Installed'(
	
'PartID' INTEGER NOT NULL,
'VehicleRegNo' TEXT NOT NULL,
'PartInstalledDate' TEXT NOT NULL,
'BookingID' INTEGER NOT NULL,
'WarantyExpDate' TEXT NOT NULL,

FOREIGN KEY(`VehicleRegNo`) REFERENCES `Vehicle`(`VehicleRegNo`),
FOREIGN KEY(`PartID`) REFERENCES `Part`(`PartID`),
FOREIGN KEY('BookingID') REFERENCES 'Booking'('BookingID')
	
);
INSERT INTO `Parts_Installed` (PartID,VehicleRegNo,PartInstalledDate,BookingID,WarantyExpDate) VALUES (1,'AIO8977','21/03/2017',1,'21/03/2018');
INSERT INTO `Parts_Installed` (PartID,VehicleRegNo,PartInstalledDate,BookingID,WarantyExpDate) VALUES (2,'AIO8977','22/03/2017',1,'22/03/2018');
INSERT INTO `Parts_Installed` (PartID,VehicleRegNo,PartInstalledDate,BookingID,WarantyExpDate) VALUES (3,'AKI9087','22/03/2017',2,'22/03/2018');
INSERT INTO `Parts_Installed` (PartID,VehicleRegNo,PartInstalledDate,BookingID,WarantyExpDate) VALUES (4,'AKI9087','23/03/2017',2,'23/03/2018');
INSERT INTO `Parts_Installed` (PartID,VehicleRegNo,PartInstalledDate,BookingID,WarantyExpDate) VALUES (5,'GHJ6753','29/03/2017',3,'29/03/2018');
INSERT INTO `Parts_Installed` (PartID,VehicleRegNo,PartInstalledDate,BookingID,WarantyExpDate) VALUES (6,'GHJ6753','08/04/2017',3,'08/04/2018');
INSERT INTO `Parts_Installed` (PartID,VehicleRegNo,PartInstalledDate,BookingID,WarantyExpDate) VALUES (7,'TAB2435','30/03/2017',4,'30/03/2018');
INSERT INTO `Parts_Installed` (PartID,VehicleRegNo,PartInstalledDate,BookingID,WarantyExpDate) VALUES (8,'TAB2435','19/04/2017',4,'19/04/2018');
INSERT INTO `Parts_Installed` (PartID,VehicleRegNo,PartInstalledDate,BookingID,WarantyExpDate) VALUES (9,'KJH7865','06/04/2017',5,'06/04/2018');
INSERT INTO `Parts_Installed` (PartID,VehicleRegNo,PartInstalledDate,BookingID,WarantyExpDate) VALUES (10,'KJH7865','06/04/2017',5,'06/04/2018');
INSERT INTO `Parts_Installed` (PartID,VehicleRegNo,PartInstalledDate,BookingID,WarantyExpDate) VALUES (1,'TAB2435','29/03/2017',6,'29/03/2018');
INSERT INTO `Parts_Installed` (PartID,VehicleRegNo,PartInstalledDate,BookingID,WarantyExpDate) VALUES (2,'TAB2435','02/04/2017',6,'02/04/2018');
INSERT INTO `Parts_Installed` (PartID,VehicleRegNo,PartInstalledDate,BookingID,WarantyExpDate) VALUES (3,'KHH9087','05/04/2017',7,'05/04/2018');
INSERT INTO `Parts_Installed` (PartID,VehicleRegNo,PartInstalledDate,BookingID,WarantyExpDate) VALUES (4,'KHH9087','25/05/2017',7,'25/05/2018');
INSERT INTO `Parts_Installed` (PartID,VehicleRegNo,PartInstalledDate,BookingID,WarantyExpDate) VALUES (8,'AKI9087','15/03/2017',8,'15/03/2018');
INSERT INTO `Parts_Installed` (PartID,VehicleRegNo,PartInstalledDate,BookingID,WarantyExpDate) VALUES (4,'AKI9087','29/03/2017',8,'29/03/2018');
INSERT INTO `Parts_Installed` (PartID,VehicleRegNo,PartInstalledDate,BookingID,WarantyExpDate) VALUES (10,'AKI9087','02/04/2017',8,'02/04/2018');
INSERT INTO `Parts_Installed` (PartID,VehicleRegNo,PartInstalledDate,BookingID,WarantyExpDate) VALUES (7,'TAB2435','07/04/2017',9,'07/04/2018');
INSERT INTO `Parts_Installed` (PartID,VehicleRegNo,PartInstalledDate,BookingID,WarantyExpDate) VALUES (4,'TAB2435','24/05/2017',9,'24/05/2018');
INSERT INTO `Parts_Installed` (PartID,VehicleRegNo,PartInstalledDate,BookingID,WarantyExpDate) VALUES (1,'TAB2435','25/03/2017',9,'25/03/2018');
CREATE TABLE `Part` (
`PartID` INTEGER PRIMARY KEY AUTOINCREMENT,
`PartName`TEXT NOT NULL,
`PartDescription`TEXT NOT NULL,
`PartQuantity`INTEGER NOT NULL,
'PartCost' DOUBLE NOT NULL

);
INSERT INTO `Part` (PartID,PartName,PartDescription,PartQuantity,PartCost) VALUES (1,'Handbrakes','High-quality hydraulic handbrakes.',19,50.0);
INSERT INTO `Part` (PartID,PartName,PartDescription,PartQuantity,PartCost) VALUES (2,'Engines','Super powerful engine with varied displacement volumes',18,200.0);
INSERT INTO `Part` (PartID,PartName,PartDescription,PartQuantity,PartCost) VALUES (3,'Turbo','Super powerful turbo',20,100.0);
INSERT INTO `Part` (PartID,PartName,PartDescription,PartQuantity,PartCost) VALUES (4,'Nitrous','Super powerful nitrous.',19,100.0);
INSERT INTO `Part` (PartID,PartName,PartDescription,PartQuantity,PartCost) VALUES (5,'Brakes','High-quality brakes.',20,50.0);
INSERT INTO `Part` (PartID,PartName,PartDescription,PartQuantity,PartCost) VALUES (6,'Rear Mirrors','Clear vision mirrors',19,25.0);
INSERT INTO `Part` (PartID,PartName,PartDescription,PartQuantity,PartCost) VALUES (7,'Hoods','Automatic high quality hoods.',19,75.0);
INSERT INTO `Part` (PartID,PartName,PartDescription,PartQuantity,PartCost) VALUES (8,'Bumpers','Protective high quality bumpers',20,125.0);
INSERT INTO `Part` (PartID,PartName,PartDescription,PartQuantity,PartCost) VALUES (9,'Doors','High quality powered doors',20,150.0);
INSERT INTO `Part` (PartID,PartName,PartDescription,PartQuantity,PartCost) VALUES (10,'Tail Lights','High quality LED lights',20,25.0);
CREATE TABLE "Mechanic" (
	`MechanicID`	INTEGER PRIMARY KEY AUTOINCREMENT,
	`MechanicFirstName`	TEXT NOT NULL,
	`MechanicLastName`	TEXT NOT NULL,
	`MechanicHourlyRate`	INTEGER NOT NULL
	
);
INSERT INTO `Mechanic` (MechanicID,MechanicFirstName,MechanicLastName,MechanicHourlyRate) VALUES (1,'Neshanathan','Amageus',13);
INSERT INTO `Mechanic` (MechanicID,MechanicFirstName,MechanicLastName,MechanicHourlyRate) VALUES (2,'Jonathan','Warm',15);
INSERT INTO `Mechanic` (MechanicID,MechanicFirstName,MechanicLastName,MechanicHourlyRate) VALUES (3,'Mo','Ragheart',14);
INSERT INTO `Mechanic` (MechanicID,MechanicFirstName,MechanicLastName,MechanicHourlyRate) VALUES (4,'Yarn','Boulderthinn',13);
INSERT INTO `Mechanic` (MechanicID,MechanicFirstName,MechanicLastName,MechanicHourlyRate) VALUES (5,'Rosh','Dunno',10);
INSERT INTO `Mechanic` (MechanicID,MechanicFirstName,MechanicLastName,MechanicHourlyRate) VALUES (6,'Sunnee','Rateelel',20);
INSERT INTO `Mechanic` (MechanicID,MechanicFirstName,MechanicLastName,MechanicHourlyRate) VALUES (7,'Jacksern','Rozz',15);
INSERT INTO `Mechanic` (MechanicID,MechanicFirstName,MechanicLastName,MechanicHourlyRate) VALUES (8,'Slowmo','DaKosta',16);
INSERT INTO `Mechanic` (MechanicID,MechanicFirstName,MechanicLastName,MechanicHourlyRate) VALUES (9,'Rahee','Ahlarm',17);
INSERT INTO `Mechanic` (MechanicID,MechanicFirstName,MechanicLastName,MechanicHourlyRate) VALUES (10,'Kenni','Boo',20);
INSERT INTO `Mechanic` (MechanicID,MechanicFirstName,MechanicLastName,MechanicHourlyRate) VALUES (11,'Timothi','Roarlins',10);
INSERT INTO `Mechanic` (MechanicID,MechanicFirstName,MechanicLastName,MechanicHourlyRate) VALUES (12,'Dayvid','Illmans',15);
INSERT INTO `Mechanic` (MechanicID,MechanicFirstName,MechanicLastName,MechanicHourlyRate) VALUES (13,'Marcelyo','Wew',11);
INSERT INTO `Mechanic` (MechanicID,MechanicFirstName,MechanicLastName,MechanicHourlyRate) VALUES (14,'Elsa','Who',12);
INSERT INTO `Mechanic` (MechanicID,MechanicFirstName,MechanicLastName,MechanicHourlyRate) VALUES (15,'Moostapha','Bosscert',25);
CREATE TABLE 'Customer' (
	
`CustomerID` INTEGER PRIMARY KEY,
'CustomerType' TEXT NOT NULL,
`CustomerFirstName`TEXT NOT NULL,
`CustomerLastName`TEXT NOT NULL,
`CustomerAddress`TEXT NOT NULL,
`CustomerPostcode`TEXT NOT NULL,
`CustomerPhoneNo`TEXT NOT NULL,
`CustomerEmail`TEXT NOT NULL

);
INSERT INTO `Customer` (CustomerID,CustomerType,CustomerFirstName,CustomerLastName,CustomerAddress,CustomerPostcode,CustomerPhoneNo,CustomerEmail) VALUES (1,'Private','John','Smith','10 Millbrae, Bunessan, Isle of Mull','PA67 6DQ','07700900749','johnsmith@gmail.com');
INSERT INTO `Customer` (CustomerID,CustomerType,CustomerFirstName,CustomerLastName,CustomerAddress,CustomerPostcode,CustomerPhoneNo,CustomerEmail) VALUES (2,'Business','Russel','Westbrook','3 Leopold Gardens, Warren Street','IP4 4RW','07478237612','tripledouble@hotmail.co.uk');
INSERT INTO `Customer` (CustomerID,CustomerType,CustomerFirstName,CustomerLastName,CustomerAddress,CustomerPostcode,CustomerPhoneNo,CustomerEmail) VALUES (3,'Private','James','Brown','79 Hampton Gardens, Southend-on-Sea','SS14 N1E','02078937123','j.brown@yahoo.com');
INSERT INTO `Customer` (CustomerID,CustomerType,CustomerFirstName,CustomerLastName,CustomerAddress,CustomerPostcode,CustomerPhoneNo,CustomerEmail) VALUES (4,'Private','Dwayne','Johnson','29 Park Ave, Prudhoe','NE42 5BB','07456123422','dwayne@gmail.com');
INSERT INTO `Customer` (CustomerID,CustomerType,CustomerFirstName,CustomerLastName,CustomerAddress,CustomerPostcode,CustomerPhoneNo,CustomerEmail) VALUES (5,'Business','Frank','Marshsall','22 Drummohr Ave, Wallyford, Musselburgh','EH21 8BP','02076751236','frank_marshal@yahoo.com');
INSERT INTO `Customer` (CustomerID,CustomerType,CustomerFirstName,CustomerLastName,CustomerAddress,CustomerPostcode,CustomerPhoneNo,CustomerEmail) VALUES (6,'Private','Warren','Buffet','3 W Gate Farm Cottages, Hardley, Norwich','NR14 6BY','07809435431','Wobbuffet@gmail.com');
INSERT INTO `Customer` (CustomerID,CustomerType,CustomerFirstName,CustomerLastName,CustomerAddress,CustomerPostcode,CustomerPhoneNo,CustomerEmail) VALUES (7,'Private','Ash','Ketchum','2 Pallet Town','PKMN 3MS','01298643633','ask_ketchum@pokemon.jp');
INSERT INTO `Customer` (CustomerID,CustomerType,CustomerFirstName,CustomerLastName,CustomerAddress,CustomerPostcode,CustomerPhoneNo,CustomerEmail) VALUES (8,'Private','Alexis','Sanchez','Hornsey Rd, London','N7 7AJ','01777165717','alexis.alejandro.sanchez@arsenal.co.uk');
INSERT INTO `Customer` (CustomerID,CustomerType,CustomerFirstName,CustomerLastName,CustomerAddress,CustomerPostcode,CustomerPhoneNo,CustomerEmail) VALUES (9,'Business','Adi','Dassler','Adi-Dassler-Strasse 1 91074 Herzogenaurach','9107 HER','01230984123','adi@adidas.de');
INSERT INTO `Customer` (CustomerID,CustomerType,CustomerFirstName,CustomerLastName,CustomerAddress,CustomerPostcode,CustomerPhoneNo,CustomerEmail) VALUES (10,'Business','Antoinne','Griezmann','Paseo de la Virgen del Puerto','M28 005','07948771028','griezmann@atleti.es');
CREATE TABLE `Booking` (
	`BookingID`	INTEGER NOT NULL UNIQUE,
	`BookingType`	TEXT NOT NULL,
	`BookingDate`	TEXT NOT NULL,
	`BookingDuration`	TEXT NOT NULL,
	`VehicleRegNo`	TEXT NOT NULL,
	`CustomerID`	INTEGER NOT NULL,
	`MechanicID`	INTEGER NOT NULL,
	`MechanicDuration`	TEXT NOT NULL,
	PRIMARY KEY(`BookingID`),
	FOREIGN KEY(`VehicleRegNo`) REFERENCES `Vehicle`(`VehicleRegNo`),
	FOREIGN KEY(`CustomerID`) REFERENCES `Customer`(`CustomerID`),
	FOREIGN KEY(`MechanicID`) REFERENCES `Mechanic`(`MechanicID`)
);
INSERT INTO `Booking` (BookingID,BookingType,BookingDate,BookingDuration,VehicleRegNo,CustomerID,MechanicID,MechanicDuration) VALUES (1,'Repair','2016-11-16 13:00','03:00','AIO8977',6,12,'03:00');
INSERT INTO `Booking` (BookingID,BookingType,BookingDate,BookingDuration,VehicleRegNo,CustomerID,MechanicID,MechanicDuration) VALUES (2,'Maintenance','2016-11-17 09:00','01:00','AKI9087',6,1,'01:00');
INSERT INTO `Booking` (BookingID,BookingType,BookingDate,BookingDuration,VehicleRegNo,CustomerID,MechanicID,MechanicDuration) VALUES (3,'Repair','2016-12-01 10:00','05:00','GHJ6753',7,11,'05:00');
INSERT INTO `Booking` (BookingID,BookingType,BookingDate,BookingDuration,VehicleRegNo,CustomerID,MechanicID,MechanicDuration) VALUES (4,'Maintenance','2017-01-04 16:00','01:00','TAB2435',8,3,'01:00');
INSERT INTO `Booking` (BookingID,BookingType,BookingDate,BookingDuration,VehicleRegNo,CustomerID,MechanicID,MechanicDuration) VALUES (5,'Maintenance','2017-01-12 09:00','06:00','KJH7865',5,15,'06:00');
INSERT INTO `Booking` (BookingID,BookingType,BookingDate,BookingDuration,VehicleRegNo,CustomerID,MechanicID,MechanicDuration) VALUES (6,'Repair','2017-01-25 11:00','06:00','TAB2435',8,3,'06:00');
INSERT INTO `Booking` (BookingID,BookingType,BookingDate,BookingDuration,VehicleRegNo,CustomerID,MechanicID,MechanicDuration) VALUES (7,'Maintenance','2017-01-28 09:00','03:00','KHH9087',4,6,'03:00');
INSERT INTO `Booking` (BookingID,BookingType,BookingDate,BookingDuration,VehicleRegNo,CustomerID,MechanicID,MechanicDuration) VALUES (8,'Maintenance','2017-02-01 10:00','02:00','AKI9087',6,14,'02:00');
INSERT INTO `Booking` (BookingID,BookingType,BookingDate,BookingDuration,VehicleRegNo,CustomerID,MechanicID,MechanicDuration) VALUES (9,'Repair','2017-02-04 15:00','02:00','TAB2435',8,13,'02:00');
INSERT INTO `Booking` (BookingID,BookingType,BookingDate,BookingDuration,VehicleRegNo,CustomerID,MechanicID,MechanicDuration) VALUES (10,'Maintenance','2017-02-06 09:00','05:00','UIO8976',9,11,'05:00');
INSERT INTO `Booking` (BookingID,BookingType,BookingDate,BookingDuration,VehicleRegNo,CustomerID,MechanicID,MechanicDuration) VALUES (11,'Maintenance','2017-02-08 16:00','01:00','UIO8976',9,8,'01:00');
INSERT INTO `Booking` (BookingID,BookingType,BookingDate,BookingDuration,VehicleRegNo,CustomerID,MechanicID,MechanicDuration) VALUES (12,'Repair','2017-02-16 09:00','02:00','UIO8976',9,9,'02:00');
INSERT INTO `Booking` (BookingID,BookingType,BookingDate,BookingDuration,VehicleRegNo,CustomerID,MechanicID,MechanicDuration) VALUES (13,'Maintenance','2017-02-18 10:00','01:00','KKK9806',5,12,'01:00');
INSERT INTO `Booking` (BookingID,BookingType,BookingDate,BookingDuration,VehicleRegNo,CustomerID,MechanicID,MechanicDuration) VALUES (14,'Repair','2017-03-01 12:00','01:00','KHH9087',4,13,'01:00');
INSERT INTO `Booking` (BookingID,BookingType,BookingDate,BookingDuration,VehicleRegNo,CustomerID,MechanicID,MechanicDuration) VALUES (15,'Maintenance','2017-03-03 09:00','06:00','ERJ2345',2,9,'06:00');
INSERT INTO `Booking` (BookingID,BookingType,BookingDate,BookingDuration,VehicleRegNo,CustomerID,MechanicID,MechanicDuration) VALUES (16,'Repair','2017-05-05 10:00','01:00','AKI8954',1,12,'00:00');
INSERT INTO `Booking` (BookingID,BookingType,BookingDate,BookingDuration,VehicleRegNo,CustomerID,MechanicID,MechanicDuration) VALUES (17,'Repair','2017-05-20 09:00','02:00','GHY6756',1,13,'00:00');
INSERT INTO `Booking` (BookingID,BookingType,BookingDate,BookingDuration,VehicleRegNo,CustomerID,MechanicID,MechanicDuration) VALUES (18,'Maintenance','2017-06-16 15:00','01:00','AIO8977',6,12,'00:00');
INSERT INTO `Booking` (BookingID,BookingType,BookingDate,BookingDuration,VehicleRegNo,CustomerID,MechanicID,MechanicDuration) VALUES (19,'Maintenance','2017-07-06 11:00','06:00','KHH9087',4,13,'00:00');
INSERT INTO `Booking` (BookingID,BookingType,BookingDate,BookingDuration,VehicleRegNo,CustomerID,MechanicID,MechanicDuration) VALUES (20,'Maintenance','2017-08-17 09:00','08:00','UIO8976',9,15,'00:00');
INSERT INTO `Booking` (BookingID,BookingType,BookingDate,BookingDuration,VehicleRegNo,CustomerID,MechanicID,MechanicDuration) VALUES (21,'Maintenance','2017-09-09 11:00','01:00','TBB2378',3,8,'00:00');
INSERT INTO `Booking` (BookingID,BookingType,BookingDate,BookingDuration,VehicleRegNo,CustomerID,MechanicID,MechanicDuration) VALUES (22,'Repair','2017-10-12 10:00','07:00','GHJ6753',7,6,'00:00');
INSERT INTO `Booking` (BookingID,BookingType,BookingDate,BookingDuration,VehicleRegNo,CustomerID,MechanicID,MechanicDuration) VALUES (23,'Maintenance','2017-11-25 09:00','01:00','TAB2435',8,1,'00:00');
INSERT INTO `Booking` (BookingID,BookingType,BookingDate,BookingDuration,VehicleRegNo,CustomerID,MechanicID,MechanicDuration) VALUES (24,'Repair','2017-12-27 10:00','07:00','TAA2523',1,1,'00:00');
INSERT INTO `Booking` (BookingID,BookingType,BookingDate,BookingDuration,VehicleRegNo,CustomerID,MechanicID,MechanicDuration) VALUES (25,'Repair','2018-01-04 13:00','03:00','AKI9087',6,12,'00:00');
INSERT INTO `Booking` (BookingID,BookingType,BookingDate,BookingDuration,VehicleRegNo,CustomerID,MechanicID,MechanicDuration) VALUES (26,'Maintenance','2018-02-22 10:00','01:00','GHY6756',1,7,'00:00');
INSERT INTO `Booking` (BookingID,BookingType,BookingDate,BookingDuration,VehicleRegNo,CustomerID,MechanicID,MechanicDuration) VALUES (27,'Maintenance','2018-03-20 14:00','02:00','JUI78987',2,1,'00:00');
INSERT INTO `Booking` (BookingID,BookingType,BookingDate,BookingDuration,VehicleRegNo,CustomerID,MechanicID,MechanicDuration) VALUES (28,'Maintenance','2018-04-14 09:00','03:00','TRY7896',9,7,'00:00');
INSERT INTO `Booking` (BookingID,BookingType,BookingDate,BookingDuration,VehicleRegNo,CustomerID,MechanicID,MechanicDuration) VALUES (29,'Repair','2018-05-19 09:00','02:00','TBB2378',3,8,'00:00');
INSERT INTO `Booking` (BookingID,BookingType,BookingDate,BookingDuration,VehicleRegNo,CustomerID,MechanicID,MechanicDuration) VALUES (30,'Repair','2018-06-05 14:00','03:00','JIK9864',6,5,'00:00');
CREATE TABLE "Bill" (
	`BookingID`	INTEGER,
	`MechanicCost`	REAL,
	`PartsCost`	REAL,
	`SPCCost`	REAL,
	`IsSettled`	INTEGER,
	PRIMARY KEY(`BookingID`),
	FOREIGN KEY(`BookingID`) REFERENCES `Booking`(`BookingID`)
);
INSERT INTO `Bill` (BookingID,MechanicCost,PartsCost,SPCCost,IsSettled) VALUES (1,45.0,250.0,143.67,1);
INSERT INTO `Bill` (BookingID,MechanicCost,PartsCost,SPCCost,IsSettled) VALUES (2,13.0,200.0,267.87,1);
INSERT INTO `Bill` (BookingID,MechanicCost,PartsCost,SPCCost,IsSettled) VALUES (3,50.0,75.0,126.1,1);
INSERT INTO `Bill` (BookingID,MechanicCost,PartsCost,SPCCost,IsSettled) VALUES (4,14.0,200.0,250.0,1);
INSERT INTO `Bill` (BookingID,MechanicCost,PartsCost,SPCCost,IsSettled) VALUES (5,150.0,175.0,0.0,1);
INSERT INTO `Bill` (BookingID,MechanicCost,PartsCost,SPCCost,IsSettled) VALUES (6,84.0,250.0,150.67,1);
INSERT INTO `Bill` (BookingID,MechanicCost,PartsCost,SPCCost,IsSettled) VALUES (7,60.0,200.0,150.78,1);
INSERT INTO `Bill` (BookingID,MechanicCost,PartsCost,SPCCost,IsSettled) VALUES (8,24.0,250.0,0.0,1);
INSERT INTO `Bill` (BookingID,MechanicCost,PartsCost,SPCCost,IsSettled) VALUES (9,22.0,225.0,123.45,1);
INSERT INTO `Bill` (BookingID,MechanicCost,PartsCost,SPCCost,IsSettled) VALUES (10,50.0,0.0,321.56,1);
INSERT INTO `Bill` (BookingID,MechanicCost,PartsCost,SPCCost,IsSettled) VALUES (11,16.0,0.0,0.0,1);
INSERT INTO `Bill` (BookingID,MechanicCost,PartsCost,SPCCost,IsSettled) VALUES (12,34.0,0.0,198.37,1);
INSERT INTO `Bill` (BookingID,MechanicCost,PartsCost,SPCCost,IsSettled) VALUES (13,15.0,0.0,0.0,1);
INSERT INTO `Bill` (BookingID,MechanicCost,PartsCost,SPCCost,IsSettled) VALUES (14,11.0,0.0,0.0,1);
INSERT INTO `Bill` (BookingID,MechanicCost,PartsCost,SPCCost,IsSettled) VALUES (15,102.0,0.0,102.99,1);
INSERT INTO `Bill` (BookingID,MechanicCost,PartsCost,SPCCost,IsSettled) VALUES (16,0.0,0.0,120.87,0);
INSERT INTO `Bill` (BookingID,MechanicCost,PartsCost,SPCCost,IsSettled) VALUES (17,0.0,0.0,0.0,0);
INSERT INTO `Bill` (BookingID,MechanicCost,PartsCost,SPCCost,IsSettled) VALUES (18,0.0,0.0,0.0,0);
INSERT INTO `Bill` (BookingID,MechanicCost,PartsCost,SPCCost,IsSettled) VALUES (19,0.0,0.0,238.67,0);
INSERT INTO `Bill` (BookingID,MechanicCost,PartsCost,SPCCost,IsSettled) VALUES (20,0.0,0.0,150.0,0);
INSERT INTO `Bill` (BookingID,MechanicCost,PartsCost,SPCCost,IsSettled) VALUES (21,0.0,0.0,183.61,0);
INSERT INTO `Bill` (BookingID,MechanicCost,PartsCost,SPCCost,IsSettled) VALUES (22,0.0,0.0,0.0,0);
INSERT INTO `Bill` (BookingID,MechanicCost,PartsCost,SPCCost,IsSettled) VALUES (23,0.0,0.0,299.99,0);
INSERT INTO `Bill` (BookingID,MechanicCost,PartsCost,SPCCost,IsSettled) VALUES (24,0.0,0.0,0.0,0);
INSERT INTO `Bill` (BookingID,MechanicCost,PartsCost,SPCCost,IsSettled) VALUES (25,0.0,0.0,432.98,0);
INSERT INTO `Bill` (BookingID,MechanicCost,PartsCost,SPCCost,IsSettled) VALUES (26,0.0,0.0,354.37,0);
INSERT INTO `Bill` (BookingID,MechanicCost,PartsCost,SPCCost,IsSettled) VALUES (27,0.0,0.0,0.0,0);
INSERT INTO `Bill` (BookingID,MechanicCost,PartsCost,SPCCost,IsSettled) VALUES (28,0.0,0.0,465.5,0);
INSERT INTO `Bill` (BookingID,MechanicCost,PartsCost,SPCCost,IsSettled) VALUES (29,0.0,0.0,0.0,0);
INSERT INTO `Bill` (BookingID,MechanicCost,PartsCost,SPCCost,IsSettled) VALUES (30,0.0,0.0,0.0,0);
CREATE TRIGGER DELETE_Vehicle
AFTER DELETE
ON Vehicle

BEGIN
DELETE FROM Booking WHERE VehicleRegNo =old.VehicleRegNo;

END;
CREATE TRIGGER DELETE_Customer 
AFTER DELETE
ON Customer

BEGIN

DELETE FROM Vehicle WHERE CustomerID = old.CustomerID;


END;
CREATE TRIGGER DELETE_Booking
AFTER DELETE
ON Booking
BEGIN
DELETE FROM Bill Where BookingID=old.BookingID;
DELETE FROM Parts_Installed WHERE BookingID=old.BookingID;
DELETE FROM SpecialistRepair WHERE BookingID=old.BookingID;
END;
COMMIT;
