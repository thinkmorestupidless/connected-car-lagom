organization in ThisBuild := "com.lightbend"
version in ThisBuild := "1.0-SNAPSHOT"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.12.4"

lazy val `connected-car-lagom` = (project in file("."))
  .aggregate(`connected-car-kafka-api`, `connected-car-lagom-api`, `connected-car-lagom-impl`)

lazy val `connected-car-kafka-api` = (project in file("connected-car-kafka-api"))
  .settings(common: _*)
  .settings(
    libraryDependencies ++= Seq(
      lagomJavadslApi,
      lagomJavadslKafkaClient,
      lombok
    )
  )

lazy val `connected-car-lagom-api` = (project in file("connected-car-lagom-api"))
  .settings(common: _*)
  .settings(
    libraryDependencies ++= Seq(
      lagomJavadslApi,
      lombok
    )
  )

lazy val `connected-car-lagom-impl` = (project in file("connected-car-lagom-impl"))
  .enablePlugins(LagomJava)
  .settings(common: _*)
  .settings(
    libraryDependencies ++= Seq(
      lagomJavadslPersistenceCassandra,
      lagomJavadslKafkaBroker,
      lagomLogback,
      lagomJavadslTestKit,
      lombok
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`connected-car-lagom-api`, `connected-car-kafka-api`)

val lombok = "org.projectlombok" % "lombok" % "1.16.18"

def common = Seq(
  javacOptions in compile += "-parameters"
)
