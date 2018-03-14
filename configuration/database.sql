DROP DATABASE IF EXISTS `textprocessor`;
CREATE DATABASE `textprocessor`
  DEFAULT CHARACTER SET utf8
  DEFAULT COLLATE utf8_general_ci;

USE `textprocessor`;



DROP TABLE IF EXISTS `test`;
CREATE TABLE `test` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `small_text` LONGTEXT CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 CHARACTER SET utf8 COLLATE utf8_unicode_ci;



DROP TABLE IF EXISTS `web_pages`;
CREATE TABLE `web_pages` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `uuid` VARCHAR(64) NOT NULL,
  `url` VARCHAR(512) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `content` LONGTEXT CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `removed_tags` LONGTEXT CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL, 
  `language` VARCHAR(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `number_of_words_in_text` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  UNIQUE INDEX `uuid_UNIQUE` (`uuid` ASC),
  UNIQUE INDEX `url_UNIQUE` (`url` ASC)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 CHARACTER SET utf8 COLLATE utf8_unicode_ci;



DROP TABLE IF EXISTS `words`;
CREATE TABLE `words` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `uuid` VARCHAR(64) NOT NULL,
  `word` VARCHAR(512) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `word_lower_case` VARCHAR(512) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `word_lower_case_no_diacritics` VARCHAR(512) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `stemmed_word_lower_case` VARCHAR(512) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `stemmed_word_lower_case_no_diacritics` VARCHAR(512) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `number_of_word_occurences_in_text` int(10) unsigned DEFAULT NULL,
  `term_frequency` VARCHAR(512) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `tfidf` VARCHAR(512) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 CHARACTER SET utf8 COLLATE utf8_unicode_ci;
