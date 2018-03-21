import java.nio.file.{Files, StandardCopyOption}

import com.typesafe.sbt.web.SbtWeb
import com.typesafe.sbt.packager.docker._

organization in ThisBuild := "com.lightbend"
version in ThisBuild := "1.0.3-SNAPSHOT"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.12.4"

lazy val buildVersion = sys.props.getOrElse("buildVersion", "1.0.0-SNAPSHOT")

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
    version := buildVersion,
    version in Docker := buildVersion,
    libraryDependencies ++= Seq(
      lagomJavadslPersistenceCassandra,
      lagomJavadslKafkaBroker,
      lagomLogback,
      lagomJavadslTestKit,
      akkaClusterBootstrap,
      akkaDiscoveryMarathonApi,
      akkaManagementClusterHttp,
      Library.serviceLocatorDns,
      lombok
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`connected-car-lagom-api`, `connected-car-kafka-api`)

//lazy val `dummy-api` = (project in file("connected-car-dummy-api"))
//  .settings(common: _*)
//  .settings(
//    libraryDependencies ++= Seq(
//      lagomJavadslApi,
//      lombok
//    )
//  )
//
//lazy val `dummy-impl` = (project in file("connected-car-dummy-impl"))
//  .enablePlugins(LagomJava)
//    .settings(
//      version := buildVersion,
//      version in Docker := buildVersion,
//      dockerBaseImage := "openjdk:8-jre-alpine",
//      dockerRepository := Some(BuildTarget.dockerRepository),
//      dockerUpdateLatest := true,
//      dockerEntrypoint ++= """-Dhttp.address="$(eval "echo $DUMMYSERVICE_BIND_IP")" -Dhttp.port="$(eval "echo $DUMMYSERVICE_BIND_PORT")" -Dakka.remote.netty.tcp.hostname="$(eval "echo $AKKA_REMOTING_HOST")" -Dakka.remote.netty.tcp.bind-hostname="$(eval "echo $AKKA_REMOTING_BIND_HOST")" -Dakka.remote.netty.tcp.port="$(eval "echo $AKKA_REMOTING_PORT")" -Dakka.remote.netty.tcp.bind-port="$(eval "echo $AKKA_REMOTING_BIND_PORT")" $(IFS=','; I=0; for NODE in $AKKA_SEED_NODES; do echo "-Dakka.cluster.seed-nodes.$I=akka.tcp://dummyservice@$NODE"; I=$(expr $I + 1); done)""".split(" ").toSeq,
//      dockerCommands :=
//        dockerCommands.value.flatMap {
//          case ExecCmd("ENTRYPOINT", args @ _*) => Seq(Cmd("ENTRYPOINT", args.mkString(" ")))
//          case c @ Cmd("FROM", _) => Seq(c, ExecCmd("RUN", "/bin/sh", "-c", "apk add --no-cache bash && ln -sf /bin/bash /bin/sh"))
//          case v => Seq(v)
//        }
//    )
//  .settings(common: _*)
//  .settings(
//    libraryDependencies ++= Seq(
//      lagomJavadslPersistenceCassandra,
//      lagomJavadslKafkaBroker,
//      lagomLogback,
//      lagomJavadslTestKit,
//      lombok
//    )
//  )
//  .settings(lagomForkedTestSettings: _*)
//  .dependsOn(`dummy-api`)

val lombok = "org.projectlombok" % "lombok" % "1.16.18"
val akkaManagementVersion = "0.10.0"
val akkaClusterBootstrap = "com.lightbend.akka.management" %% "akka-management-cluster-bootstrap" % akkaManagementVersion
val akkaDiscoveryDns = "com.lightbend.akka.discovery" %% "akka-discovery-dns" % akkaManagementVersion
val akkaDiscoveryMarathonApi = "com.lightbend.akka.discovery" %% "akka-discovery-marathon-api" % akkaManagementVersion
val akkaManagementClusterHttp = "com.lightbend.akka.management" %% "akka-management-cluster-http" % akkaManagementVersion

def common = Seq(
  javacOptions in compile += "-parameters"
)
