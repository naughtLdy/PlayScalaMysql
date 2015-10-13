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
		val test_dao = new models.db.Information
		val data = db.run(test_dao.getSelectQuery)
		val hoge = Await.result(data, Duration.Inf)

		var list: List[InformationData] = List[InformationData]()

		for (fuga <- hoge){
			val piyo = (fuga._1, fuga._2, new org.joda.time.DateTime(fuga._3), new org.joda.time.DateTime(fuga._4), fuga._5 )
			val boko: InformationData = InformationData.tupled(piyo)
			list = boko :: list
			println(fuga)
		}
		list = list.reverse
		val json = Json.toJson( Json.obj("list" -> list))

		db.close()
		return json
	}

	def sqlTimestampToDateTime: java.sql.Timestamp => org.joda.time.DateTime = { ts => new org.joda.time.DateTime(ts.getTime) }
}