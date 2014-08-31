name := "busman"

version := "1.0-SNAPSHOT"

val myRepo = "My Maven Repo" at "https://github.com/CUTR-at-USF/cutr-mvn-repo/raw/master/releases"
val siri = "org.onebusaway" % "onebusaway-siri-api-v13" % "1.0.1"

resolvers += {
  val r = new org.apache.ivy.plugins.resolver.IBiblioResolver
  r.setM2compatible(true)
  r.setName("My repor")
  r.setRoot("http://nexus.onebusaway.org/content/groups/public/")
  r.setCheckconsistency(false)
  new RawRepository(r)
}

libraryDependencies ++= Seq(
  siri,
  javaJdbc,
  javaEbean,
  cache
)     

play.Project.playJavaSettings

