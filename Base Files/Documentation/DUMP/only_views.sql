-- MySQL dump 10.19  Distrib 10.3.32-MariaDB, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: dbinfox
-- ------------------------------------------------------
-- Server version	10.3.32-MariaDB-0ubuntu0.20.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Temporary table structure for view `vw_tbos`
--

DROP TABLE IF EXISTS `vw_tbos`;
/*!50001 DROP VIEW IF EXISTS `vw_tbos`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `vw_tbos` (
  `os` tinyint NOT NULL,
  `data_os` tinyint NOT NULL,
  `tipo` tinyint NOT NULL,
  `situacao` tinyint NOT NULL,
  `equipamento` tinyint NOT NULL,
  `defeito` tinyint NOT NULL,
  `servico` tinyint NOT NULL,
  `tecnico` tinyint NOT NULL,
  `valor` tinyint NOT NULL,
  `idcli` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `vw_os`
--

DROP TABLE IF EXISTS `vw_os`;
/*!50001 DROP VIEW IF EXISTS `vw_os`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `vw_os` (
  `OS` tinyint NOT NULL,
  `Data` tinyint NOT NULL,
  `Tipo` tinyint NOT NULL,
  `Situação` tinyint NOT NULL,
  `Equipamento` tinyint NOT NULL,
  `Defeito` tinyint NOT NULL,
  `Serviço` tinyint NOT NULL,
  `Técnico` tinyint NOT NULL,
  `Valor` tinyint NOT NULL,
  `ID` tinyint NOT NULL,
  `Cliente` tinyint NOT NULL,
  `Endereço` tinyint NOT NULL,
  `Telefone` tinyint NOT NULL,
  `Email` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `vw_servicos`
--

DROP TABLE IF EXISTS `vw_servicos`;
/*!50001 DROP VIEW IF EXISTS `vw_servicos`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `vw_servicos` (
  `OS` tinyint NOT NULL,
  `Data` tinyint NOT NULL,
  `Tipo` tinyint NOT NULL,
  `Situação` tinyint NOT NULL,
  `Equipamento` tinyint NOT NULL,
  `Valor` tinyint NOT NULL,
  `Cliente` tinyint NOT NULL,
  `Telefone` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Final view structure for view `vw_tbos`
--

/*!50001 DROP TABLE IF EXISTS `vw_tbos`*/;
/*!50001 DROP VIEW IF EXISTS `vw_tbos`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `vw_tbos` AS select `tbos`.`os` AS `os`,date_format(`tbos`.`data_os`,'%d/%m/%Y - %h:%i') AS `data_os`,`tbos`.`tipo` AS `tipo`,`tbos`.`situacao` AS `situacao`,`tbos`.`equipamento` AS `equipamento`,`tbos`.`defeito` AS `defeito`,`tbos`.`servico` AS `servico`,`tbos`.`tecnico` AS `tecnico`,`tbos`.`valor` AS `valor`,`tbos`.`idcli` AS `idcli` from `tbos` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `vw_os`
--

/*!50001 DROP TABLE IF EXISTS `vw_os`*/;
/*!50001 DROP VIEW IF EXISTS `vw_os`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `vw_os` AS select `os`.`os` AS `OS`,`os`.`data_os` AS `Data`,`os`.`tipo` AS `Tipo`,`os`.`situacao` AS `Situação`,`os`.`equipamento` AS `Equipamento`,`os`.`defeito` AS `Defeito`,`os`.`servico` AS `Serviço`,`os`.`tecnico` AS `Técnico`,`os`.`valor` AS `Valor`,`os`.`idcli` AS `ID`,`cli`.`nomecli` AS `Cliente`,`cli`.`endcli` AS `Endereço`,`cli`.`fonecli` AS `Telefone`,`cli`.`emailcli` AS `Email` from (`tbos` `os` join `tbclientes` `cli` on(`cli`.`idcli` = `os`.`idcli`)) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `vw_servicos`
--

/*!50001 DROP TABLE IF EXISTS `vw_servicos`*/;
/*!50001 DROP VIEW IF EXISTS `vw_servicos`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `vw_servicos` AS select `os`.`os` AS `OS`,`os`.`data_os` AS `Data`,`os`.`tipo` AS `Tipo`,`os`.`situacao` AS `Situação`,`os`.`equipamento` AS `Equipamento`,`os`.`valor` AS `Valor`,`cli`.`nomecli` AS `Cliente`,`cli`.`fonecli` AS `Telefone` from (`tbos` `os` join `tbclientes` `cli` on(`cli`.`idcli` = `os`.`idcli`)) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-02-15 15:34:36
