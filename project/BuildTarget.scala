import sbt._

object BuildTarget {
  private sealed trait DeploymentRuntime
  private case object Kubernetes extends DeploymentRuntime
  private case object Marathon extends DeploymentRuntime

  private val deploymentRuntime: DeploymentRuntime = sys.props.get("buildTarget") match {
     case Some(v) if v.toLowerCase == "kubernetes" =>
      Kubernetes

    case Some(v) if v.toLowerCase == "marathon" =>
      Marathon

    case Some(v) =>
      sys.error(s"The build target $v is not supported. Available: 'kubernetes', 'marathon'")

    case None =>
      Marathon
  }

  val additionalSettings = deploymentRuntime match {
    case Kubernetes =>
      Seq(
        Keys.libraryDependencies ++= Seq(
          Library.serviceLocatorDns
        ),
        Keys.unmanagedResourceDirectories in Compile += Keys.sourceDirectory.value / "main" / "kubernetes-resources"
      )
    case Marathon   =>
      Seq(
        Keys.libraryDependencies ++= Seq(
          Library.serviceLocatorDns, Library.constructr, Library.constructrZooKeeper
        ),
        Keys.unmanagedResourceDirectories in Compile += Keys.sourceDirectory.value / "main" / "marathon-resources"
      )
  }

  val dockerRepository: String = deploymentRuntime match {
    case Kubernetes => "chirper"
    case Marathon   => "chirper-marathon"
  }
}