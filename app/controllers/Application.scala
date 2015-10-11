package controllers

import play.api._
import play.api.mvc._

class Application extends Controller {

	def index = Action {
		Ok(views.html.index("Your new application is ready."))
	}

	def test = Action {
		val model = new models.Information
		model.get

		Ok(views.html.index("Your new application is ready."))
	}
}
