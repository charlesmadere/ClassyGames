CREATE TABLE IF NOT EXISTS `games` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`user_creator` BIGINT NOT NULL,
	`user_challenged` BIGINT NOT NULL,
	`board` TEXT NOT NULL,
	PRIMARY KEY (`id`),
	FOREIGN KEY (`user_creator`) REFERENCES players(`id`),
	FOREIGN KEY (`user_challenged`) REFERENCES players(`id`)
);
