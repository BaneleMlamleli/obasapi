package domain.users

import play.api.libs.json.Json

case class UserResults(
                       userResultsId:String,
                       description:String
                      )
object UserResults{
implicit val userResultsFmt = Json.format[UserResults]
}
