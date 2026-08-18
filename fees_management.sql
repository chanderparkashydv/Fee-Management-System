-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Aug 18, 2026 at 12:48 PM
-- Server version: 10.4.22-MariaDB
-- PHP Version: 8.0.13

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `fees_management`
--

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE `admin` (
  `admin_id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `admin`
--

INSERT INTO `admin` (`admin_id`, `username`, `password`) VALUES
(1, 'admin@fms', 'Ad@123_mn-.');

-- --------------------------------------------------------

--
-- Table structure for table `batch`
--

CREATE TABLE `batch` (
  `b_id` int(11) NOT NULL,
  `c_id` int(11) NOT NULL,
  `batch_name` varchar(100) NOT NULL,
  `batch_start_date` date NOT NULL,
  `batch_end_date` date NOT NULL,
  `batch_time` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `batch`
--

INSERT INTO `batch` (`b_id`, `c_id`, `batch_name`, `batch_start_date`, `batch_end_date`, `batch_time`) VALUES
(2, 1, 'April 2026 Morning 10 AM', '2026-04-06', '2026-06-06', '10:00 AM to 12:00 PM'),
(3, 2, 'April 26 Morning 9 AM', '2026-04-10', '2026-06-10', '9:00 AM to 11:00 AM');

-- --------------------------------------------------------

--
-- Table structure for table `course`
--

CREATE TABLE `course` (
  `c_id` int(11) NOT NULL,
  `course_name` varchar(100) NOT NULL,
  `course_duration` varchar(50) NOT NULL,
  `course_fees` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `course`
--

INSERT INTO `course` (`c_id`, `course_name`, `course_duration`, `course_fees`) VALUES
(1, 'C Language', '2', '9500.00'),
(2, 'C++', '2', '10500.00'),
(3, 'Java', '9', '25000.00'),
(4, 'Data Structure L1', '6', '13000.00'),
(5, 'Web D using MERN', '12', '38000.00');

-- --------------------------------------------------------

--
-- Table structure for table `fees_student`
--

CREATE TABLE `fees_student` (
  `fsr_id` int(11) NOT NULL,
  `b_id` int(11) NOT NULL,
  `s_id` int(11) NOT NULL,
  `c_id` int(11) NOT NULL,
  `fees` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `fees_student`
--

INSERT INTO `fees_student` (`fsr_id`, `b_id`, `s_id`, `c_id`, `fees`) VALUES
(1, 2, 1, 1, '9500.00'),
(2, 3, 2, 2, '10500.00');

-- --------------------------------------------------------

--
-- Table structure for table `fees_student_deposited`
--

CREATE TABLE `fees_student_deposited` (
  `fsd_id` int(11) NOT NULL,
  `s_id` int(11) NOT NULL,
  `b_id` int(11) NOT NULL,
  `c_id` int(11) NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `dateandtime` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `fees_student_deposited`
--

INSERT INTO `fees_student_deposited` (`fsd_id`, `s_id`, `b_id`, `c_id`, `amount`, `dateandtime`) VALUES
(1, 1, 2, 1, '4500.00', '2026-04-06 06:40:48'),
(2, 1, 2, 1, '5000.00', '2026-04-06 06:41:28'),
(3, 2, 3, 2, '1000.00', '2026-04-10 12:46:03');

-- --------------------------------------------------------

--
-- Table structure for table `student`
--

CREATE TABLE `student` (
  `s_id` int(11) NOT NULL,
  `firstname` varchar(50) NOT NULL,
  `lastname` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `mobile` varchar(15) NOT NULL,
  `dob` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `student`
--

INSERT INTO `student` (`s_id`, `firstname`, `lastname`, `email`, `mobile`, `dob`) VALUES
(1, 'Tarun', 'Verma', 'tarunverma150988@gmail.com', '7742817777', '1988-09-15'),
(2, 'Ankit', 'Vishwakarma', 'ak@hotmail.com', '8562098987', '2005-08-22');

-- --------------------------------------------------------

--
-- Table structure for table `student_batch`
--

CREATE TABLE `student_batch` (
  `sb_id` int(11) NOT NULL,
  `s_id` int(11) NOT NULL,
  `b_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `student_batch`
--

INSERT INTO `student_batch` (`sb_id`, `s_id`, `b_id`) VALUES
(1, 1, 2),
(2, 2, 3);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`admin_id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- Indexes for table `batch`
--
ALTER TABLE `batch`
  ADD PRIMARY KEY (`b_id`),
  ADD KEY `fk_batch_course` (`c_id`);

--
-- Indexes for table `course`
--
ALTER TABLE `course`
  ADD PRIMARY KEY (`c_id`);

--
-- Indexes for table `fees_student`
--
ALTER TABLE `fees_student`
  ADD PRIMARY KEY (`fsr_id`),
  ADD UNIQUE KEY `uq_fees_student` (`s_id`,`b_id`,`c_id`),
  ADD KEY `fk_feesstudent_batch` (`b_id`),
  ADD KEY `fk_feesstudent_course` (`c_id`);

--
-- Indexes for table `fees_student_deposited`
--
ALTER TABLE `fees_student_deposited`
  ADD PRIMARY KEY (`fsd_id`),
  ADD KEY `fk_feesdeposit_student` (`s_id`),
  ADD KEY `fk_feesdeposit_batch` (`b_id`),
  ADD KEY `fk_feesdeposit_course` (`c_id`);

--
-- Indexes for table `student`
--
ALTER TABLE `student`
  ADD PRIMARY KEY (`s_id`),
  ADD UNIQUE KEY `email` (`email`),
  ADD UNIQUE KEY `mobile` (`mobile`);

--
-- Indexes for table `student_batch`
--
ALTER TABLE `student_batch`
  ADD PRIMARY KEY (`sb_id`),
  ADD UNIQUE KEY `uq_student_batch` (`s_id`,`b_id`),
  ADD KEY `fk_studentbatch_batch` (`b_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `admin`
--
ALTER TABLE `admin`
  MODIFY `admin_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `batch`
--
ALTER TABLE `batch`
  MODIFY `b_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `course`
--
ALTER TABLE `course`
  MODIFY `c_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `fees_student`
--
ALTER TABLE `fees_student`
  MODIFY `fsr_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `fees_student_deposited`
--
ALTER TABLE `fees_student_deposited`
  MODIFY `fsd_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `student`
--
ALTER TABLE `student`
  MODIFY `s_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `student_batch`
--
ALTER TABLE `student_batch`
  MODIFY `sb_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `batch`
--
ALTER TABLE `batch`
  ADD CONSTRAINT `fk_batch_course` FOREIGN KEY (`c_id`) REFERENCES `course` (`c_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `fees_student`
--
ALTER TABLE `fees_student`
  ADD CONSTRAINT `fk_feesstudent_batch` FOREIGN KEY (`b_id`) REFERENCES `batch` (`b_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_feesstudent_course` FOREIGN KEY (`c_id`) REFERENCES `course` (`c_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_feesstudent_student` FOREIGN KEY (`s_id`) REFERENCES `student` (`s_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `fees_student_deposited`
--
ALTER TABLE `fees_student_deposited`
  ADD CONSTRAINT `fk_feesdeposit_batch` FOREIGN KEY (`b_id`) REFERENCES `batch` (`b_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_feesdeposit_course` FOREIGN KEY (`c_id`) REFERENCES `course` (`c_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_feesdeposit_student` FOREIGN KEY (`s_id`) REFERENCES `student` (`s_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `student_batch`
--
ALTER TABLE `student_batch`
  ADD CONSTRAINT `fk_studentbatch_batch` FOREIGN KEY (`b_id`) REFERENCES `batch` (`b_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_studentbatch_student` FOREIGN KEY (`s_id`) REFERENCES `student` (`s_id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
