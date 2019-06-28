/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`jdbc-realm` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `jdbc-realm`;

/*Table structure for table `account` */

DROP TABLE IF EXISTS `account`;

CREATE TABLE `account` (
  `ID` bigint(10) NOT NULL,
  `name` varchar(256) default NULL,
  `password` varchar(256) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `account` */

insert  into `account`(`ID`,`name`,`password`) values (1,'zhangsan','zs1234');

/*Table structure for table `account_role` */

DROP TABLE IF EXISTS `account_role`;

CREATE TABLE `account_role` (
  `userId` bigint(10) default NULL,
  `roleId` bigint(10) default NULL,
  KEY `FK_Account_Role` (`roleId`),
  KEY `FK_Account_Role1` (`userId`),
  CONSTRAINT `FK_Account_Role` FOREIGN KEY (`roleId`) REFERENCES `role` (`ID`),
  CONSTRAINT `FK_Account_Role1` FOREIGN KEY (`userId`) REFERENCES `account` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `account_role` */

insert  into `account_role`(`userId`,`roleId`) values (1,1);

/*Table structure for table `permission` */

DROP TABLE IF EXISTS `permission`;

CREATE TABLE `permission` (
  `ID` bigint(10) NOT NULL,
  `name` varchar(256) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `permission` */

insert  into `permission`(`ID`,`name`) values (1,'read'),(2,'write');

/*Table structure for table `permission_role` */

DROP TABLE IF EXISTS `permission_role`;

CREATE TABLE `permission_role` (
  `permissionId` bigint(10) default NULL,
  `roleId` bigint(10) default NULL,
  KEY `FK_PerMission_Role` (`roleId`),
  KEY `FK_PerMission_Role1` (`permissionId`),
  CONSTRAINT `FK_PerMission_Role` FOREIGN KEY (`roleId`) REFERENCES `role` (`ID`),
  CONSTRAINT `FK_PerMission_Role1` FOREIGN KEY (`permissionId`) REFERENCES `permission` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `permission_role` */

insert  into `permission_role`(`permissionId`,`roleId`) values (1,1),(2,1);

/*Table structure for table `role` */

DROP TABLE IF EXISTS `role`;

CREATE TABLE `role` (
  `ID` bigint(10) NOT NULL,
  `name` varchar(256) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `role` */

insert  into `role`(`ID`,`name`) values (1,'admin');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
