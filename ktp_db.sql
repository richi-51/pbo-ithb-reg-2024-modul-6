-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 04, 2024 at 04:34 PM
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
-- Database: `ktp_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `data_ktp`
--

CREATE TABLE `data_ktp` (
  `nik` varchar(16) NOT NULL,
  `nama` varchar(100) NOT NULL,
  `tempat_lahir` varchar(50) NOT NULL,
  `tanggal_lahir` date NOT NULL,
  `jenis_kelamin` enum('LAKI-LAKI','PEREMPUAN') NOT NULL,
  `golongan_darah` enum('A','B','AB','O') DEFAULT NULL,
  `alamat` text NOT NULL,
  `rt_rw` varchar(7) NOT NULL,
  `kelurahan` varchar(50) NOT NULL,
  `kecamatan` varchar(50) NOT NULL,
  `agama` enum('ISLAM','KRISTEN','KATOLIK','HINDU','BUDDHA','KONGHUCU') NOT NULL,
  `status_perkawinan` enum('BELUM MENIKAH','MENIKAH','JANDA/DUDA') NOT NULL,
  `pekerjaan` varchar(50) NOT NULL,
  `kewarganegaraan` varchar(25) DEFAULT 'WNI',
  `berlaku_hingga` varchar(50) DEFAULT NULL,
  `tanggal_dikeluarkan` date NOT NULL,
  `tempat_dikeluarkan` varchar(50) NOT NULL,
  `path_foto` varchar(255) NOT NULL,
  `path_tanda_tangan` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `data_ktp`
--

INSERT INTO `data_ktp` (`nik`, `nama`, `tempat_lahir`, `tanggal_lahir`, `jenis_kelamin`, `golongan_darah`, `alamat`, `rt_rw`, `kelurahan`, `kecamatan`, `agama`, `status_perkawinan`, `pekerjaan`, `kewarganegaraan`, `berlaku_hingga`, `tanggal_dikeluarkan`, `tempat_dikeluarkan`, `path_foto`, `path_tanda_tangan`) VALUES
('31313', 'FSAFS', 'DFSFD', '2024-12-25', 'LAKI-LAKI', 'B', 'DSFSDFAF', '09/10', 'DFSFDD', 'FSFSF', 'KRISTEN', 'BELUM MENIKAH', 'PNS, WIRASWASTA', 'WNI', 'dsfsfs', '0016-06-10', 'DSFSF', 'C:\\Users\\hp_az\\OneDrive\\Documents\\11.jpg', 'D:\\Trace Maze-2 DFS.png'),
('42424', 'DFSAF', 'DSFSF', '2024-12-05', 'LAKI-LAKI', 'A', 'SAFASF', 'dsfsfs', 'SFSFSF', 'SFSDF', 'KRISTEN', 'BELUM MENIKAH', 'PNS', 'WNI', 'fdsafdsf', '2024-12-01', 'SAFSFSF', 'C:\\Users\\hp_az\\OneDrive\\Documents\\11.jpg', 'C:\\Users\\hp_az\\OneDrive\\Documents\\11.jpg');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `data_ktp`
--
ALTER TABLE `data_ktp`
  ADD PRIMARY KEY (`nik`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
