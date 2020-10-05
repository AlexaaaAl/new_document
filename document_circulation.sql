-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Хост: 127.0.0.1:3306
-- Время создания: Сен 10 2020 г., 15:30
-- Версия сервера: 10.3.22-MariaDB
-- Версия PHP: 7.1.33

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- База данных: `document_circulation`
--

-- --------------------------------------------------------

--
-- Структура таблицы `answer_recirient`
--

CREATE TABLE `answer_recirient` (
  `id` bigint(20) NOT NULL,
  `id_doc` bigint(255) UNSIGNED NOT NULL,
  `path` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `document` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `comments` text COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `date` date NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Дамп данных таблицы `answer_recirient`
--

INSERT INTO `answer_recirient` (`id`, `id_doc`, `path`, `document`, `comments`, `date`) VALUES
(2, 13, 'C:/Users/Джулай Александра/Desktop', 'Лист Microsoft Excel.xlsx', '', '2020-09-09'),
(4, 9, 'C:/Users/Джулай Александра/Desktop/', 'ПодтвержденныйЛист Microsoft Excel.exe', NULL, '2020-09-10'),
(5, 1, 'C:/Users/Джулай Александра/Desktop/', 'Подтвержденныйdfrh.docx', NULL, '2020-09-10'),
(6, 3, 'C:/Users/Джулай Александра/Desktop/', 'Подтвержденныйlogo.png', NULL, '2020-09-10'),
(7, 6, 'C:/Users/Джулай Александра/Desktop/', 'ПодтвержденныйНовый текстовый документ.txt', NULL, '2020-09-10'),
(8, 9, 'C:/Users/Джулай Александра/Desktop/', 'ПодтвержденныйЛист Microsoft Excel.exe', NULL, '2020-09-10'),
(9, 3, 'C:/Users/Джулай Александра/Desktop/', 'logo.png', NULL, '2020-09-10');

-- --------------------------------------------------------

--
-- Структура таблицы `documents`
--

CREATE TABLE `documents` (
  `id_document` bigint(255) UNSIGNED NOT NULL,
  `number` bigint(20) UNSIGNED NOT NULL,
  `path` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `document` text CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `id_sender` bigint(255) UNSIGNED NOT NULL,
  `id_recipient` bigint(255) UNSIGNED NOT NULL,
  `outline` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `comments` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `date_added` date NOT NULL DEFAULT current_timestamp(),
  `date` date DEFAULT NULL,
  `status` enum('в ожидании','выполняется','подтверждено') COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Дамп данных таблицы `documents`
--

INSERT INTO `documents` (`id_document`, `number`, `path`, `document`, `id_sender`, `id_recipient`, `outline`, `comments`, `date_added`, `date`, `status`) VALUES
(1, 1, 'C:/Users/Джулай Александра/Desktop/', 'dfrh.docx', 1, 2, '?', 'Что-то там лежит..', '2020-09-03', '2020-09-04', 'подтверждено'),
(3, 3, 'C:/Users/Джулай Александра/Desktop/', 'logo.png', 2, 3, 'п', 'jgbfv', '2020-09-03', NULL, 'подтверждено'),
(4, 4, 'C:/Users/Джулай Александра/Desktop', 'j.docx', 1, 3, '', 'lkjhgfd', '2020-09-03', NULL, 'в ожидании'),
(5, 5, 'C:/Users/Джулай Александра/Desktop/', 'Лист Microsoft Excel.xlsx', 1, 3, 'н', 'fgx', '2020-09-03', NULL, 'выполняется'),
(6, 6, 'C:/Users/Джулай Александра/Desktop/', 'Новый текстовый документ.txt', 2, 3, 'р', 'hd', '2020-09-03', NULL, 'подтверждено'),
(8, 7, 'W:/domains/j', 'u.docx', 1, 3, 'не', 'sr', '2020-09-03', NULL, 'в ожидании'),
(9, 8, 'C:/Users/Джулай Александра/Desktop/', 'Лист Microsoft Excel.exe', 1, 2, 'ty', 'f', '2020-09-03', '2020-10-10', 'подтверждено'),
(10, 10, 'C:/Users/Джулай Александра/Desktop/', 'j.docx', 1, 3, 'yt', 'ykrf', '2020-09-03', NULL, 'в ожидании'),
(13, 13, '', 'hhtp:j.docx', 1, 3, 'ty', 'drg', '2020-09-03', NULL, 'подтверждено'),
(14, 14, 'C:/Users/Джулай Александра/Desktop/', 'Лист Microsoft Excel.xlsx', 1, 3, 'gty', 'tf', '2020-09-03', NULL, 'в ожидании'),
(15, 15, 'C:/Users/Джулай Александра/Desktop/', 'u.docx', 1, 3, 'ty', 'szb', '2020-09-03', '2020-09-11', 'в ожидании'),
(16, 16, 'C:/Users/Джулай Александра/Desktop/', 'u.docx', 1, 3, 'fr', 'tggggggggggggggggggggggggggggggg', '2020-09-03', NULL, 'в ожидании'),
(17, 17, '//192.168.50.250/сетевая/server/', 's.docx', 1, 3, 'nm', '34t', '2020-09-03', NULL, 'в ожидании'),
(18, 18, '//192.168.50.250/сетевая/server/', 'j.docx', 1, 3, 'df', 'f', '2020-09-03', NULL, 'в ожидании'),
(19, 19, '//192.168.50.250/сетевая/server/', 'Новый текстовый документ.txt', 2, 3, 'i8', 't', '2020-09-03', NULL, 'в ожидании'),
(42, 20, '//192.168.50.250/сетевая/server/', 'j.docx', 3, 2, 'dvd', 'kkk', '2020-09-03', '2020-09-09', 'выполняется'),
(43, 21, '//192.168.50.250/сетевая/server/ОИЭ', 's.docx', 2, 4, 'h', 'jjj', '2020-09-03', '2020-09-03', 'в ожидании'),
(44, 22, '//192.168.50.250/сетевая/server/оиэ', 'u.docx', 1, 3, 'ds', 'sbrfazd', '2020-09-03', NULL, 'выполняется'),
(45, 23, '//192.168.50.250/сетевая/server/ОИЭ/2020', 'logo.png', 1, 4, 'kikk', 'bdf', '2020-09-03', NULL, 'в ожидании');

-- --------------------------------------------------------

--
-- Структура таблицы `log`
--

CREATE TABLE `log` (
  `id_user` bigint(255) UNSIGNED NOT NULL,
  `login` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `date` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Дамп данных таблицы `log`
--

INSERT INTO `log` (`id_user`, `login`, `password`, `date`) VALUES
(1, 'admin', 'admin', '2020-08-01 13:25:02'),
(2, 'user', 'user', '2020-08-21 10:24:39'),
(3, 'sergey', 'sergey', '2020-08-19 10:27:26');

-- --------------------------------------------------------

--
-- Структура таблицы `roles`
--

CREATE TABLE `roles` (
  `ID` bigint(255) UNSIGNED NOT NULL,
  `RIGHTS` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Дамп данных таблицы `roles`
--

INSERT INTO `roles` (`ID`, `RIGHTS`) VALUES
(1, 'admin'),
(2, 'user');

-- --------------------------------------------------------

--
-- Структура таблицы `users`
--

CREATE TABLE `users` (
  `ID` bigint(255) UNSIGNED NOT NULL,
  `FIRST_NAME` varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL,
  `LAST_NAME` varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL,
  `POSITION` varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL,
  `ip_server` varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL,
  `DEPARTMENT` varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL,
  `date` date NOT NULL DEFAULT current_timestamp(),
  `E_MAIL` varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL,
  `ROLE_ID` bigint(255) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Дамп данных таблицы `users`
--

INSERT INTO `users` (`ID`, `FIRST_NAME`, `LAST_NAME`, `POSITION`, `ip_server`, `DEPARTMENT`, `date`, `E_MAIL`, `ROLE_ID`) VALUES
(1, 'Александра', 'Джулай', 'developer', '192.168.50.250', 'OИЭС', '0000-00-00', 'gu@mail.ru', 1),
(2, 'Иван', 'Иванов', 'лаборант', '192.168.50.250', '____', '0000-00-00', 'fu@mail.ru', 2),
(3, 'Сергей', 'Игнатов', 'сопроводитель', '192.168.50.250', 'оиэ', '0000-00-00', 'sgh@gail.com', 2),
(4, 'Андрей', 'Васильевич', 'а_', '192.168.50.250', 'ОИЭ', '2020-09-02', 'пто', 2);

--
-- Индексы сохранённых таблиц
--

--
-- Индексы таблицы `answer_recirient`
--
ALTER TABLE `answer_recirient`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_doc` (`id_doc`);

--
-- Индексы таблицы `documents`
--
ALTER TABLE `documents`
  ADD PRIMARY KEY (`id_document`),
  ADD UNIQUE KEY `id_document` (`id_document`),
  ADD KEY `id_sender` (`id_sender`),
  ADD KEY `id_recipient` (`id_recipient`);

--
-- Индексы таблицы `log`
--
ALTER TABLE `log`
  ADD KEY `id_user` (`id_user`);

--
-- Индексы таблицы `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`ID`),
  ADD UNIQUE KEY `ID` (`ID`),
  ADD KEY `ID_2` (`ID`);

--
-- Индексы таблицы `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `ROLE_ID` (`ROLE_ID`);

--
-- AUTO_INCREMENT для сохранённых таблиц
--

--
-- AUTO_INCREMENT для таблицы `answer_recirient`
--
ALTER TABLE `answer_recirient`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT для таблицы `documents`
--
ALTER TABLE `documents`
  MODIFY `id_document` bigint(255) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=46;

--
-- AUTO_INCREMENT для таблицы `roles`
--
ALTER TABLE `roles`
  MODIFY `ID` bigint(255) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT для таблицы `users`
--
ALTER TABLE `users`
  MODIFY `ID` bigint(255) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Ограничения внешнего ключа сохраненных таблиц
--

--
-- Ограничения внешнего ключа таблицы `answer_recirient`
--
ALTER TABLE `answer_recirient`
  ADD CONSTRAINT `answer_recirient_ibfk_3` FOREIGN KEY (`id_doc`) REFERENCES `documents` (`id_document`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ограничения внешнего ключа таблицы `documents`
--
ALTER TABLE `documents`
  ADD CONSTRAINT `documents_ibfk_1` FOREIGN KEY (`id_sender`) REFERENCES `users` (`ID`) ON DELETE NO ACTION ON UPDATE CASCADE,
  ADD CONSTRAINT `documents_ibfk_4` FOREIGN KEY (`id_recipient`) REFERENCES `users` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Ограничения внешнего ключа таблицы `log`
--
ALTER TABLE `log`
  ADD CONSTRAINT `log_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `users` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ограничения внешнего ключа таблицы `users`
--
ALTER TABLE `users`
  ADD CONSTRAINT `users_ibfk_1` FOREIGN KEY (`ROLE_ID`) REFERENCES `roles` (`ID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
