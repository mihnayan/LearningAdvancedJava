-- -----------------------------------------------------
-- Schema dtjdb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `dtjdb` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;

USE `dtjdb` ;

-- -----------------------------------------------------
-- Table `dtjdb`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dtjdb`.`user` (
  `id` VARCHAR(40) NOT NULL DEFAULT '',
  `username` VARCHAR(45) NOT NULL,
  `fullname` VARCHAR(100) NULL,
  PRIMARY KEY (`id`));

USE `dtjdb`;

DELIMITER $$
USE `dtjdb`$$
CREATE DEFINER = CURRENT_USER TRIGGER `dtjdb`.`user_BEFORE_INSERT` BEFORE INSERT ON `user` FOR EACH ROW
    begin
        set new.id = uuid();
    end;$$


DELIMITER ;
