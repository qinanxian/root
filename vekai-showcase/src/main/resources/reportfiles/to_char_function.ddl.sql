-- MySQL所需的to_char 仅为了兼容报表demo 生产环境请清除
DROP FUNCTION IF EXISTS to_char ;
DELIMITER //
CREATE FUNCTION to_char(date VARCHAR(20),fomat VARCHAR(20))
RETURNS VARCHAR(255)
BEGIN
RETURN date_format(date,'%Y');
END //
DELIMITER ;