package domain.users

import play.api.libs.json.Json

case class UserDemographics(
                             userId: String,
                             genderId: String,
                             raceId: String,
                             titleId: String
                           )

object UserDemographics {
  implicit val userDemographicsFmt = Json.format[UserDemographics]
}
