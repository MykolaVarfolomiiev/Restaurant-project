CREATE DATABASE  IF NOT EXISTS `alchemyrestaurant` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `alchemyrestaurant`;

-- VERY IMPORTANT !!!!! For real DB creation (not testing) comment out the next 2 lines and uncomment 2 lines above:
-- CREATE DATABASE IF NOT EXISTS `test` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
-- USE `test`;

-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: localhost    Database: alchemyrestaurant
-- ------------------------------------------------------
-- Server version	8.0.43

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
-- Table structure for table `dinning_table`
--

DROP TABLE IF EXISTS `dinning_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dinning_table` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `capacity` int DEFAULT NULL,
  `local_date_time` datetime(6) DEFAULT NULL,
  `table_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dinning_table`
--

LOCK TABLES `dinning_table` WRITE;
/*!40000 ALTER TABLE `dinning_table` DISABLE KEYS */;
INSERT INTO `dinning_table` 
VALUES 
(1,2,'2026-03-11 23:50:47.575444','Table 1'),
(2,4,'2026-03-11 23:50:53.923042','Table 2'),
(3,6,'2026-03-11 23:50:58.257039','Table 3'),
(4,20,'2026-03-11 23:51:22.518179','Wedding'),
(5,4,'2026-03-11 23:51:33.703443','Table 5'),
(6,4,'2026-03-16 22:36:14.494236','Table for 4'),
(7,6,'2026-04-08 22:27:27.521870','Table for Ivan');
/*!40000 ALTER TABLE `dinning_table` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `events`
--

DROP TABLE IF EXISTS `events`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `events` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created` datetime(6) NOT NULL,
  `duration_in_minutes` int NOT NULL,
  `end_date` date NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `is_active` bit(1) NOT NULL,
  `event_name` varchar(255) NOT NULL,
  `price` decimal(38,2) NOT NULL,
  `start_date` date NOT NULL,
  `updated` datetime(6) NOT NULL,
  `version` bigint DEFAULT NULL,
  `is_archived` bit(1) NOT NULL,
  `menu_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_events_menu` (`menu_id`),
  CONSTRAINT `fk_events_menu` FOREIGN KEY (`menu_id`) REFERENCES `menus` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=82 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `events`
--

LOCK TABLES `events` WRITE;
/*!40000 ALTER TABLE `events` DISABLE KEYS */;
INSERT INTO `events` 
VALUES 
(1,'2026-03-09 22:47:16.554552',60,'2026-04-15','Party',_binary '\0','Ivan\'s Birthday',255.00,'2026-04-15','2026-04-12 01:46:02.662297',8,_binary '\0',NULL),
(4,'2026-03-10 00:41:44.874781',10,'2026-04-27','You only live once theme',_binary '\0','20th Anniversary Party ',150.00,'2026-04-27','2026-04-11 22:42:51.416467',10,_binary '\0',22),
(5,'2026-03-10 00:44:02.988121',48,'2026-04-22','',_binary '','Wendy\'s Weeding',1800.00,'2026-04-21','2026-04-11 02:57:30.290451',6,_binary '\0',2),
(6,'2026-03-11 00:00:41.816058',8,'2026-06-26','NBCC student graduation 2026 celebration event',_binary '\0','NBCC Graduation 2026',450.00,'2026-06-26','2026-04-11 03:07:13.572680',10,_binary '\0',NULL),
(7,'2026-03-11 00:03:17.934993',60,'2026-04-29','Black caviar and champagne party!',_binary '','Evan\'s Party',2000.00,'2026-04-28','2026-04-11 23:28:14.875345',12,_binary '\0',42),
(8,'2026-03-11 00:30:44.572086',12,'2026-12-25','',_binary '','Christmas 2026',200.00,'2026-12-25','2026-04-11 02:07:02.718072',4,_binary '\0',26),
(9,'2026-03-11 00:32:11.555962',120,'2025-12-26','IGT Software Devs party',_binary '','Christmas 2025',500.00,'2025-12-25','2026-04-11 23:10:35.171053',3,_binary '\0',26),
(42,'2026-04-03 15:54:07.626476',60,'2026-04-18','',_binary '','Stephen 50 ',180.00,'2026-04-18','2026-04-11 02:55:32.072112',7,_binary '\0',22);
/*!40000 ALTER TABLE `events` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `menu_items`
--

DROP TABLE IF EXISTS `menu_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `menu_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `menu_item_description` varchar(255) NOT NULL,
  `menu_item_name` varchar(255) NOT NULL,
  `version` bigint DEFAULT NULL,
  `menu_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_menuItem_menu` (`menu_id`),
  CONSTRAINT `fk_menuItem_menu` FOREIGN KEY (`menu_id`) REFERENCES `menus` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu_items`
--

LOCK TABLES `menu_items` WRITE;
/*!40000 ALTER TABLE `menu_items` DISABLE KEYS */;
INSERT INTO `menu_items` 
VALUES 
(1,'Soft chocolate','Pudding',30,2),
(3,' A savory sour soup, typically made with pork, fish, or shrimp, Tamarind, and vegetables','Sinigang',13,22),
(15,'Fresh pizza','Pizza Calzone ',0,2),(18,'Huge white frosting cake','Wedding cake',0,10),
(19,'Pasta with parmesan ','Pasta Carbonara',0,2),(21,'soft, jelly','Chocolate Pudding',1,10),
(24,'Rice with vegies','Risotto',1,2),
(31,'Fish eggs (black). Very expensive','Black Caviar',1,42),
(32,'A luxury delicacy consisting of salt-cured, unfertilized sturgeon roe, traditionally sourced from the Caspian and Black Seas','Red Caviar',1,42),
(33,'Sparkling wine x10','Champagne',3,42),
(34,'marinated','Fried tofu',0,34),
(35,'pasta & mushrooms','Mushroom ravioli',0,34),
(36,'fried dumplings','Deep Fried Wonton',0,36),
(37,'soup','Cantonese Chow Mein',0,36),
(38,'stewed pork with sides','Sweet and Sour Pork',0,36),
(39,'bean and cauliflower mix','Chickpea cauliflower curry',1,37),
(40,'only vegan ingredients','Vegan pizza',0,37),
(41,'meatless burger','Lentil burgers',0,37),
(42,'stir-fried thin rice noodles','Pad Tai',0,35),
(43,'coconut-based curry','Green Curry ',1,35),
(44,'spicy basil stir-fry','Pad Gra Pow',0,35),
(45,'spicy and sour soup','Tom Yum',0,35),
(46,'meat stewed in soy sauce/vinegar','Adobo',0,22),
(47,'Thinly sliced beef marinated in soy sauce and calamansi','Bistek Tagalog',1,22),
(48,'Creamy ','Mashed potatoes',0,26),
(49,'chocolate rich and soft','Chocolate mousse',0,26),
(50,'milk and egg yolks mix, sweet drink ','Eggnog',0,26),
(51,'prime turkey, slowly roasted','Roast Turkey',0,26),
(52,'cranberry jelly','Cranberry sauce',0,26),
(53,'spicy pasta','Penne Arrabbiata',0,2),
(54,'meat or cheese lasagna','Lasagna',0,2);
/*!40000 ALTER TABLE `menu_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `menus`
--

DROP TABLE IF EXISTS `menus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `menus` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `menu_description` varchar(255) DEFAULT NULL,
  `menu_name` varchar(255) NOT NULL,
  `version` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menus`
--

LOCK TABLES `menus` WRITE;
/*!40000 ALTER TABLE `menus` DISABLE KEYS */;
INSERT INTO `menus` 
VALUES 
(2,'2026-03-21 23:10:25.657610','All Italian cuisine','Italian menu',34),
(10,'2026-03-23 14:03:52.906870','','Wedding menu',5),
(22,'2026-03-23 22:23:31.469142','Traditional','Philippian menu',1),
(26,'2026-03-24 00:12:10.985785','Traditional North American','Christmas menu',2),
(34,'2026-04-01 00:45:10.751797','','Vegetarian menu',5),
(35,'2026-04-03 02:01:10.435452','','Thai menu',6),
(36,'2026-04-03 17:53:55.995957','Traditional Chinese cuisine','Chinese menu',1),
(37,'2026-04-03 18:01:10.084656','','Vegan menu',7),
(42,'2026-04-11 23:22:15.344673','Very expensive menu','Fancy menu',2);
/*!40000 ALTER TABLE `menus` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reservation_request`
--

DROP TABLE IF EXISTS `reservation_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reservation_request` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `email` varchar(255) NOT NULL,
  `first_name` varchar(255) NOT NULL,
  `group_size` int NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `seating_id` bigint NOT NULL,
  `status` enum('APPROVED','DENIED','PENDING') NOT NULL,
  `uuid` varchar(36) NOT NULL,
  `table_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK83fdtnoyuvqaufnpubpjta3k2` (`uuid`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservation_request`
--

LOCK TABLES `reservation_request` WRITE;
/*!40000 ALTER TABLE `reservation_request` DISABLE KEYS */;
INSERT INTO `reservation_request` 
VALUES 
(1,'2026-04-06 22:44:43.835844','ivan.court@nbcc.ca','Ivan',10,'Court',4,'PENDING','f52ce51b-4227-41a0-b862-dc11348f27ac',NULL),
(2,'2026-04-06 22:46:49.557783','ivan.court@mbcc.ca','Ivan',10,'Court',8,'APPROVED','512761fd-4403-4301-b6ac-a35d39ce271e',3),
(3,'2026-04-08 22:28:38.457536','kid@gmail.com','Kid',10,'Cudi',15,'PENDING','de76424c-a396-42a2-9ee8-36c8e42cdfc5',NULL),
(4,'2026-04-08 22:35:00.180760','kid@gmail.com','Kid',6,'Cudy',17,'DENIED','d8cbb77f-437c-40ce-9d4d-1f8c7a578209',NULL),
(5,'2026-04-11 23:19:58.856247','j.yung@igt.ca','Jason',20,'Yung',20,'APPROVED','f93acac3-6f30-4788-8133-d9319088c597',1);
/*!40000 ALTER TABLE `reservation_request` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `seating`
--

DROP TABLE IF EXISTS `seating`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `seating` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_date` datetime(6) DEFAULT NULL,
  `duration_minutes` int NOT NULL,
  `event_id` bigint DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `start_date_time` datetime(6) DEFAULT NULL,
  `archived` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `seating`
--

LOCK TABLES `seating` WRITE;
/*!40000 ALTER TABLE `seating` DISABLE KEYS */;
INSERT INTO `seating` 
VALUES 
(8,'2026-03-21 20:54:46.864581',10,4,'Fun','2026-03-27 11:58:00.000000',_binary '\0',NULL),
(13,'2026-04-03 01:29:44.438320',20,5,'Wedding set1','2026-03-22 14:00:00.000000',_binary '\0','2026-04-03 01:30:36.418736'),
(14,'2026-04-03 01:32:07.091516',48,5,'Wedding set2','2026-03-21 15:00:00.000000',_binary '\0',NULL),
(15,'2026-04-03 15:54:44.802665',60,42,'Test sits','2026-04-04 18:00:00.000000',_binary '\0',NULL),
(16,'2026-04-08 22:26:28.305549',60,42,'Set of seatings','2026-04-04 22:30:00.000000',_binary '\0','2026-04-08 22:26:56.173989'),
(17,'2026-04-08 22:33:53.399723',10,4,'Test seatings','2026-03-27 14:00:00.000000',_binary '\0',NULL),
(18,'2026-04-11 20:30:03.312336',60,1,'Sits for Ivan','2026-04-15 15:30:00.000000',_binary '\0',NULL),
(20,'2026-04-11 23:18:48.592943',120,9,'Devs sits','2025-12-25 22:00:00.000000',_binary '\0',NULL);
/*!40000 ALTER TABLE `seating` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `seating_tables`
--

DROP TABLE IF EXISTS `seating_tables`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `seating_tables` (
  `seating_id` bigint NOT NULL,
  `table_id` bigint DEFAULT NULL,
  KEY `FK9vons89k04aa90o6839gpkrqe` (`seating_id`),
  CONSTRAINT `FK9vons89k04aa90o6839gpkrqe` FOREIGN KEY (`seating_id`) REFERENCES `seating` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `seating_tables`
--

LOCK TABLES `seating_tables` WRITE;
/*!40000 ALTER TABLE `seating_tables` DISABLE KEYS */;
INSERT INTO `seating_tables` 
VALUES 
(8,3),
(13,1),
(14,2),
(15,1),
(15,3),
(15,5),
(16,1),
(16,2),
(17,5),
(18,7),
(20,1),
(20,2),
(20,3),
(20,5),
(20,6);
/*!40000 ALTER TABLE `seating_tables` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `enabled` tinyint(1) NOT NULL DEFAULT '1',
  `locked` tinyint(1) NOT NULL DEFAULT '0',
  `password` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKob8kqyqqgmefl0aco34akdtpe` (`email`),
  UNIQUE KEY `UKsb8bbouer5wak8vyiiy4pf2bx` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` 
VALUES 
(1,'mike@nbcc.ca',1,0,'$2a$10$LJLjPmRD6QEjlezuZB/cTerguTeR7Lj/3q36namVBQbpp1UoywxDW','Mike'),
(2,'andre@nbcc.ca',1,0,'$2a$10$QaHWyAhEsPgVjKMBcoQS3uYnb.aCKWAXs9xw.k511hYniapTQFoqm','andre');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-04-12  2:07:03
