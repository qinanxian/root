DELIMITER $$
DROP  FUNCTION  IF  EXISTS  get_latest_task_name $$
CREATE FUNCTION get_latest_task_name(
	procIntId VARCHAR(255)
)
RETURNS varchar(255)
BEGIN
		RETURN (select TASK_NAME from WKFL_TASK WHERE PROC_ID='1840001' AND (FINISH_TYPE is NULL OR FINISH_TIME is null));
END $$
DELIMITER ;