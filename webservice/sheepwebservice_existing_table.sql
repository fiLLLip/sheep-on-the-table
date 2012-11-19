-- phpMyAdmin SQL Dump
-- version 3.5.2.2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Nov 19, 2012 at 07:43 PM
-- Server version: 5.5.28-0ubuntu0.12.04.2
-- PHP Version: 5.3.10-1ubuntu3.4

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `knutela_sheep`
--

-- --------------------------------------------------------

--
-- Table structure for table `sheep_farm`
--

CREATE TABLE IF NOT EXISTS `sheep_farm` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `address` varchar(45) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=17 ;

-- --------------------------------------------------------

--
-- Table structure for table `sheep_permissions`
--

CREATE TABLE IF NOT EXISTS `sheep_permissions` (
  `user_id` int(11) NOT NULL,
  `farm_id` int(11) NOT NULL,
  `level` int(1) NOT NULL DEFAULT '0' COMMENT '2 = Owner, 1 = Admin, 0 = view only',
  `SMSAlarmAttack` int(1) NOT NULL DEFAULT '0',
  `SMSAlarmHealth` int(1) NOT NULL DEFAULT '0',
  `SMSAlarmStationary` int(1) NOT NULL DEFAULT '0',
  `EmailAlarmAttack` int(1) NOT NULL DEFAULT '0',
  `EmailAlarmHealth` int(1) NOT NULL DEFAULT '0',
  `EmailAlarmStationary` int(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`user_id`,`farm_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Table structure for table `sheep_sheep`
--

CREATE TABLE IF NOT EXISTS `sheep_sheep` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `farm_id` int(11) NOT NULL,
  `name` varchar(45) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `born` datetime NOT NULL DEFAULT '1970-01-02 00:00:00',
  `deceased` datetime NOT NULL DEFAULT '1970-01-02 00:00:00',
  `comment` text COLLATE utf8_unicode_ci NOT NULL,
  `weight` double NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `fk_sheep_farm_idx` (`farm_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=20093 ;

-- --------------------------------------------------------

--
-- Table structure for table `sheep_updates`
--

CREATE TABLE IF NOT EXISTS `sheep_updates` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sheep_id` int(11) NOT NULL,
  `timestamp` datetime DEFAULT NULL,
  `pos_x` double DEFAULT '0',
  `pos_y` double DEFAULT '0',
  `pulse` int(11) DEFAULT '0',
  `temp` double DEFAULT '0',
  `alarm` int(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `fk_updates_sheep1_idx` (`sheep_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=190478 ;

-- --------------------------------------------------------

--
-- Table structure for table `sheep_user`
--

CREATE TABLE IF NOT EXISTS `sheep_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `un` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `pw` varchar(45) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `name` varchar(45) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `email` varchar(80) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `phone` varchar(8) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `sysadmin` int(1) NOT NULL DEFAULT '0',
  `ip` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `hash` varchar(40) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `un` (`un`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=21 ;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
