-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 30, 2024 at 11:42 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `rental_app`
--

-- --------------------------------------------------------

--
-- Table structure for table `properties`
--

CREATE TABLE `properties` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `address` varchar(100) NOT NULL,
  `amount` double NOT NULL,
  `tenant_name` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `properties`
--

INSERT INTO `properties` (`id`, `name`, `address`, `amount`, `tenant_name`) VALUES
(1, 'Teratak Fauziah', 'No 32, Bt:10 kg. Pulau (Teratak Ab. Rahman /Fauziah) 76100 Durian Tunggal, Melaka.', 850, 'Nabil Aqmar'),
(2, 'Melaka Ezyroom', '773F 32 Taman Bukit Beruang Utama, 75450 Ayer Keroh, Melaka', 250, 'Lim Wei Jie'),
(3, 'The Heights Residence Condominium', 'Puncak Muzaffar Hang Tuah Jaya, Taman Muzaffar Heights, 75450 Ayer Keroh, Melaka', 2300, 'Fikri Fadzil');

-- --------------------------------------------------------

--
-- Table structure for table `rent`
--

CREATE TABLE `rent` (
  `id` int(20) NOT NULL,
  `property_id` int(20) NOT NULL,
  `email` varchar(50) NOT NULL,
  `rent_amount` decimal(10,0) NOT NULL,
  `utillity_amount` decimal(10,0) NOT NULL,
  `total_rent` decimal(10,0) NOT NULL,
  `month_year` varchar(20) NOT NULL,
  `status` varchar(20) NOT NULL,
  `payment_date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `rent`
--

INSERT INTO `rent` (`id`, `property_id`, `email`, `rent_amount`, `utillity_amount`, `total_rent`, `month_year`, `status`, `payment_date`) VALUES
(2, 1, 'nabil_aqmar@gmail.com', 850, 130, 980, '', 'Due', '0000-00-00'),
(4, 3, 'fikri_fadzil@gmail.com', 2300, 130, 2430, '', 'Due', '0000-00-00'),
(5, 2, 'lim_wj@gmail.com', 250, 130, 380, 'July-2024', 'Paid', '2024-07-01'),
(6, 2, 'lim_wj@gmail.com', 250, 180, 430, 'August-2024', 'Paid', '2024-07-01');

-- --------------------------------------------------------

--
-- Table structure for table `tenants`
--

CREATE TABLE `tenants` (
  `id` int(20) NOT NULL,
  `name` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(50) NOT NULL,
  `phone` varchar(20) NOT NULL,
  `property_id` int(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `tenants`
--

INSERT INTO `tenants` (`id`, `name`, `email`, `password`, `phone`, `property_id`) VALUES
(1, 'Fikri Fadzil', 'fikri_fadzil@gmail.com', 'test123', '019-283741', 3),
(2, 'Nabil Aqmar', 'nabil_aqmar@gmail.com', 'test123', '018-057847', 1),
(3, 'Lim Wei Jie', 'lim_wj@gmail.com', 'test123', '013-203534', 2),
(4, 'Hisham Salehhudin', 'hishamsalehhudin@gmail.com', 'test123', '019-6357512', NULL),
(5, 'Lukman Hafiz', 'lukmanhafiz@gmail.com', 'test123', '018-3969023', NULL),
(6, 'Khalis Zakwan', 'khalizz@gmail.com', 'test123', '019-239148', NULL),
(7, 'Faris Ghafar', 'fg_kawaii@gmail.com', 'test123', '016-3948523', NULL);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `properties`
--
ALTER TABLE `properties`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `rent`
--
ALTER TABLE `rent`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tenants`
--
ALTER TABLE `tenants`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `properties`
--
ALTER TABLE `properties`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `rent`
--
ALTER TABLE `rent`
  MODIFY `id` int(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `tenants`
--
ALTER TABLE `tenants`
  MODIFY `id` int(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
