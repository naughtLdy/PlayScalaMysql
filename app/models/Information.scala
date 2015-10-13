package models

import play.api.libs.json._
import play.api.libs.functional.syntax._
import slick.driver.JdbcProfile
import slick.backend.StaticDatabaseConfig
import slick.backend.DatabaseConfig
import scala.concurrent.Await
import scala.concurrent.duration.Duration

class Information {

	case class InformationData(id:Int, message:String, start_time:org.joda.time.DateTime, end_time:org.joda.time.DateTime, view_flag:Boolean)

	implicit val InformationDataWrites: Writes[InformationData] = (
		(JsPath \ "id").write[Int] and
		(JsPath \ "message").write[String] and
		(JsPath \ "start_time").write[org.joda.time.DateTime] and
		(JsPath \ "end_time").write[org.joda.time.DateTime] and
		(JsPath \ "view_flag").write[Boolean]
	)(unlift(InformationData.unapply))

	@StaticDatabaseConfig("file:conf/application.conf#master")
	def get: play.api.libs.json.JsValue = {

		val dc = DatabaseConfig.forAnnotation[JdbcProfile]
		val db = dc.db
		val info_dao = new models.db.Information
		val info_data = db.run(info_dao.getSelectQuery)
		val table_data = Await.result(info_data, Duration.Inf)
		val list = table_data.map((data) => {
			val convert_data = (data._1, data._2, new org.joda.time.DateTime(data._3), new org.joda.time.DateTime(data._4), data._5 )
			InformationData.tupled(convert_data)
		})

		val json = Json.toJson( Json.obj("list" -> list))

		db.close()
		return json
	}

	def sqlTimestampToDateTime: java.sql.Timestamp => org.joda.time.DateTime = { ts => new org.joda.time.DateTime(ts.getTime) }
}