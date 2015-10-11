package models.db

import slick.driver.MySQLDriver.api._
import slick.backend.StaticDatabaseConfig

class Information {

	@StaticDatabaseConfig("file:conf/application.conf#master")
	def getSelectQuery: DBIO[Seq[(Int, String, java.sql.Timestamp, java.sql.Timestamp, Boolean)]] = {
		tsql"""SELECT * FROM `information` WHERE NOW() BETWEEN start_date AND end_date AND view_flag = 0 ORDER BY id"""
	}
}