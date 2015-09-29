import sbt._
import sbt.Keys._

import com.typesafe.sbt.pgp.PgpKeys
import com.typesafe.sbt.pgp.PgpKeys.publishSigned
import com.typesafe.sbt.SbtSite.SiteKeys._
import com.typesafe.sbt.SbtSite.site
import com.typesafe.sbt.SbtGit._

import com.typesafe.sbt.SbtGhPages.GhPagesKeys._
import com.typesafe.sbt.SbtGhPages.ghpages

import tut.Plugin._
import pl.project13.scala.sbt.SbtJmh._
import sbtunidoc.Plugin.UnidocKeys._
import sbtunidoc.Plugin._
import sbtrelease.ReleasePlugin.autoImport._
import ReleaseTransformations._
import org.scalajs.sbtplugin.ScalaJSPlugin
import ScalaJSPlugin.autoImport._
import scoverage.ScoverageSbtPlugin._
import org.scalajs.sbtplugin.cross.CrossProject

object Base {

  implicit class VersionsOps(v: Map[String, String]) {
    def ->(s: String) = v.get(s).get
  }

  val versions = Map[String, String](
    "discipline" -> "0.4",
    "macro-compat" -> "1.0.0",
    "paradise" -> "2.1.0-M5",
    "scalacheck" -> "1.12.4",
    "scalatest" -> "3.0.0-M7",
    "scalac" -> "2.11.7",
    "specs2" -> "3.6.4"
  )


  lazy val noPublishSettings = Seq(
    publish := (),
    publishLocal := (),
    publishArtifact := false
  )
  
  lazy val rootSettings = noPublishSettings ++ Seq(
    moduleName := "root"
  )
  
  lazy val crossVersionSharedSources: Seq[Setting[_]] =
    Seq(Compile, Test).map { sc =>
      (unmanagedSourceDirectories in sc) ++= {
        (unmanagedSourceDirectories in sc ).value.map {
          dir:File => new File(dir.getPath + "_" + scalaBinaryVersion.value)
        }
      }
    }

  def scalaMacrosParadise(version: String): Seq[Setting[_]] = Seq(
    libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value % "provided",
    libraryDependencies += compilerPlugin("org.scalamacros" %% "paradise" % version cross CrossVersion.full)
  )

  def scalaMacroDependencies(version: String): Seq[Setting[_]] = Seq(
    libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value % "provided",
    libraryDependencies ++= {
      CrossVersion.partialVersion(scalaVersion.value) match {
        // if scala 2.11+ is used, quasiquotes are merged into scala-reflect
        case Some((2, scalaMajor)) if scalaMajor >= 11 => Seq()
        // in Scala 2.10, quasiquotes are provided by macro paradise
        case Some((2, 10)) =>
          Seq(
            compilerPlugin("org.scalamacros" %% "paradise" % version cross CrossVersion.full),
                "org.scalamacros" %% "quasiquotes" % version cross CrossVersion.binary
          )
      } 
    }
  )

  lazy val commonScalacOptions = Seq(
    "-deprecation",
    "-encoding", "UTF-8",
    "-feature",
    "-language:existentials",
    "-language:higherKinds",
    "-language:implicitConversions",
    "-language:experimental.macros",
    "-unchecked",
    "-Xfatal-warnings",
    "-Xlint",
    "-Yinline-warnings",
    "-Yno-adapted-args",
    "-Ywarn-dead-code",
    "-Ywarn-numeric-widen",
    "-Ywarn-value-discard",
    "-Xfuture"
  )

  lazy val sharedCommonSettings = Seq(
    updateOptions := updateOptions.value.withCachedResolution(true)
  )

  def sharedPublishSettings(home: String, repo: String, api: String, license: (String, URL), devs: Seq[Dev]): Seq[Setting[_]] = Seq(
    homepage := Some(url(home)),
    licenses += license,
    scmInfo :=  Some(ScmInfo(url(home), "scm:git:" + repo)),
    apiURL := Some(url(api)),
    releaseCrossBuild := true,
    releasePublishArtifactsAction := PgpKeys.publishSigned.value,
    publishMavenStyle := true,
    publishArtifact in Test := false,
    pomIncludeRepository := Function.const(false),
    publishTo := {
      val nexus = "https://oss.sonatype.org/"
      if (isSnapshot.value)
        Some("Snapshots" at nexus + "content/repositories/snapshots")
      else
        Some("Releases" at nexus + "service/local/staging/deploy/maven2")
    },
    autoAPIMappings := true,
    pomExtra := ( <developers> { devs.map(_.pomExtra) } </developers> )
  )

  lazy val sharedReleaseProcess = Seq(
    releaseProcess := Seq[ReleaseStep](
      checkSnapshotDependencies,
      inquireVersions,
      runClean,
      runTest,
      setReleaseVersion,
      commitReleaseVersion,
      tagRelease,
      publishArtifacts,
      setNextVersion,
      commitNextVersion,
      ReleaseStep(action = Command.process("sonatypeReleaseAll", _)),
      pushChanges)
  )

  def sharedScoverageSettings(min: Int = 80) = Seq(
    ScoverageKeys.coverageMinimum := min,
    ScoverageKeys.coverageFailOnMinimum := false,
    ScoverageKeys.coverageHighlighting := scalaBinaryVersion.value != "2.10"
      // ScoverageKeys.coverageExcludedPackages := "catalysts\\.bench\\..*"
  )

  lazy val unidocCommonSettings = Seq(
    scalacOptions in (ScalaUnidoc, unidoc) += "-Ymacro-no-expand"
  )

  lazy val warnUnusedImport = Seq(
    scalacOptions ++= {
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((2, 10)) =>
          Seq()
        case Some((2, n)) if n >= 11 =>
          Seq("-Ywarn-unused-import")
      }
    },
    scalacOptions in (Compile, console) ~= {_.filterNot("-Ywarn-unused-import" == _)},
    scalacOptions in (Test, console) <<= (scalacOptions in (Compile, console))
  )

  lazy val credentialSettings = Seq(
    // For Travis CI - see http://www.cakesolutions.net/teamblogs/publishing-artefacts-to-oss-sonatype-nexus-using-sbt-and-travis-ci
    credentials ++= (for {
      username <- Option(System.getenv().get("SONATYPE_USERNAME"))
      password <- Option(System.getenv().get("SONATYPE_PASSWORD"))
    } yield Credentials("Sonatype Nexus Repository Manager", "oss.sonatype.org", username, password)).toSeq
  )

  // From https://github.com/typelevel/sbt-typelevel/blob/master/src/main/scala/Developer.scala
  case class Dev(name: String, id: String) {
    def pomExtra: xml.NodeSeq = (
      <developer>
        <id>{ id }</id>
        <name>{ name }</name>
        <url>http://github.com/{ id }</url>
          </developer>
    )
  }

  def mkConfig(projSettings: Seq[sbt.Setting[_]], jvmSettings: Seq[sbt.Setting[_]],
    jsSettings: Seq[sbt.Setting[_]] ): CrossProject ⇒ CrossProject =
    _.settings(projSettings:_*)
      .jsSettings(jsSettings:_*)
      .jvmSettings(jvmSettings:_*)

  def mkRootConfig(projSettings: Seq[sbt.Setting[_]] , projJVM:Project): Project ⇒ Project =
    _.in(file("."))
      .settings(rootSettings)
      .settings(projSettings)
      .settings(console <<= console in (projJVM, Compile))

  def mkRootJvmConfig(s: String, projSettings: Seq[sbt.Setting[_]] , jvmSettings: Seq[sbt.Setting[_]]): Project ⇒ Project =
    _.settings(moduleName := s)
      .settings(projSettings)
      .settings(jvmSettings)
      .in(file("." + s + "JVM"))

  def mkRootJsConfig(s: String, projSettings: Seq[sbt.Setting[_]] , jsSettings: Seq[sbt.Setting[_]]): Project ⇒ Project =
    _.settings(moduleName := s)
      .settings(projSettings)
      .settings(jsSettings)
      .in(file("." + s + "JS"))
      .enablePlugins(ScalaJSPlugin)

  def mkDocConfig(proj: String, repo: String, org:String,  projSettings: Seq[sbt.Setting[_]] , jvmSettings: Seq[sbt.Setting[_]],
      deps: Project*): Project ⇒ Project =

    _.settings(projSettings)
    .settings(moduleName := proj + "-docs")
    .settings(noPublishSettings)
    .settings(unidocSettings)
    .settings(site.settings)
    .settings(tutSettings)
    .settings(ghpages.settings)
    .settings(jvmSettings)
    .dependsOn(deps.map( ClasspathDependency(_, Some("compile;test->test")))  :_*) //platformJVM, macrosJVM, scalatestJVM, specs2, testkitJVM)
    .settings(
       organization  := org,
       autoAPIMappings := true,
       unidocProjectFilter in (ScalaUnidoc, unidoc) := inProjects(deps.map(_.project)  :_*),
       site.addMappingsToSiteDir(mappings in (ScalaUnidoc, packageDoc), "api"),
       ghpagesNoJekyll := false,
       site.addMappingsToSiteDir(tut, "_tut"),
       tutScalacOptions ~= (_.filterNot(Set("-Ywarn-unused-import", "-Ywarn-dead-code"))),
      
       scalacOptions in (ScalaUnidoc, unidoc) ++= Seq(
         "-doc-source-url", scmInfo.value.get.browseUrl + "/tree/master€{FILE_PATH}.scala",
         "-sourcepath", baseDirectory.in(LocalRootProject).value.getAbsolutePath,
         "-diagrams"
       ),
       git.remoteRepo := repo,
       includeFilter in makeSite := "*.html" | "*.css" | "*.png" | "*.jpg" | "*.gif" | "*.js" | "*.swf" | "*.yml" | "*.md")
}
