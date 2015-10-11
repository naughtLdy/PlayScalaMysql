package models

import slick.driver.JdbcProfile
import slick.backend.StaticDatabaseConfig
import slick.backend.DatabaseConfig
import scala.concurrent.Await
import scala.concurrent.duration.Duration

class Information {
	@StaticDatabaseConfig("file:conf/application.conf#master")
	def get: String = {

		val dc = DatabaseConfig.forAnnotation[JdbcProfile]
		val db = dc.db
		val test_dao = new models.db.Information
		val data = db.run(test_dao.getSelectQuery)

		Await.result(data, Duration.Inf) foreach println

		db.close()
		return "dummy"
	}

}