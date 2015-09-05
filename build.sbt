import Base._
import com.typesafe.sbt.pgp.PgpKeys.publishSigned
import com.typesafe.sbt.SbtSite.SiteKeys._
import com.typesafe.sbt.SbtGhPages.GhPagesKeys._
import sbtunidoc.Plugin.UnidocKeys._
import ReleaseTransformations._
import ScoverageSbtPlugin._

/**
 * These aliases serialise the build for the benefit of Travis-CI, also useful for pre-PR testing.
 * If new projects are added to the build, these must be updated.  
 */
addCommandAlias("buildJVM", ";macrosJVM/compile;coreJVM/compile;lawsJVM/compile;testsJVM/test")
addCommandAlias("validateJVM", ";scalastyle;buildJVM;makeSite")
addCommandAlias("validateJS", ";macrosJS/compile;coreJS/compile;lawsJS/compile;testsJS/test")
addCommandAlias("validate", ";validateJS;validateJVM")

/**
 * Build settings
 */
val home = "https://github.com/banana-rdf/scala-bricks"
val repo = "git@github.com:banana-rdf/scala-bricks.git"
val api = "https://banana-rdf.github.io/scala-bricks/api/"  
val license = ("Apache License", url("http://www.apache.org/licenses/LICENSE-2.0.txt"))

val disciplineVersion = "0.4"
val scalacheckVersion = "1.12.4"
val scalatestVersion = "3.0.0-M7"

lazy val buildSettings = Seq(
  organization := "org.banana-rdf",
  scalaVersion := "2.11.7",
  crossScalaVersions := Seq("2.10.5", "2.11.7")
)

/**
 * Common settings
 */
lazy val commonSettings = Seq(
  scalacOptions ++= commonScalacOptions,
  parallelExecution in Test := false
) ++ warnUnusedImport

lazy val commonJsSettings = Seq(
  scalaJSStage in Global := FastOptStage
)

lazy val commonJvmSettings = Seq(
  testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-oDF")
)

/**
 * Bricks - This is the root project that aggregates the bricksJVM and bricksJS sub projects
 */
lazy val bricksSettings = buildSettings ++ commonSettings ++ publishSettings ++ scoverageSettings

lazy val bricks = project.in(file("."))
  .settings(moduleName := "root")
  .settings(bricksSettings)
  .settings(noPublishSettings)
  .aggregate(bricksJVM, bricksJS)
  .dependsOn(bricksJVM, bricksJS, testsJVM % "test-internal -> test")

lazy val bricksJVM = project.in(file(".bricksJVM"))
  .settings(moduleName := "bricks")
  .settings(bricksSettings)
  .settings(commonJvmSettings)
  .aggregate(macrosJVM, coreJVM, lawsJVM, testsJVM, docs)
  .dependsOn(macrosJVM, coreJVM, lawsJVM, testsJVM % "test-internal -> test")

lazy val bricksJS = project.in(file(".bricksJS"))
  .settings(moduleName := "bricks")
  .settings(bricksSettings)
  .settings(commonJsSettings)
  .aggregate(macrosJS, coreJS, lawsJS, testsJS)
  .dependsOn(macrosJS, coreJS, lawsJS, testsJS % "test-internal -> test")
  .enablePlugins(ScalaJSPlugin)

/**
 * Macros - cross project that defines macros
 */
lazy val macros = crossProject.crossType(CrossType.Pure)
  .settings(moduleName := "bricks-macros")
  .settings(bricksSettings:_*)
  .settings(crossVersionSharedSources:_*)
  .settings(scalaMacroDependencies:_*)
  .jsSettings(commonJsSettings:_*)
  .jvmSettings(commonJvmSettings:_*)

lazy val macrosJVM = macros.jvm 
lazy val macrosJS = macros.js

/**
 * Core - cross project that defines macros is the main project for the build 
 */
lazy val core = crossProject.crossType(CrossType.Dummy)
  .dependsOn(macros)
  .settings(moduleName := "bricks-core")
  .settings(bricksSettings:_*)
  .settings(scalaMacroDependencies:_*)
  .jsSettings(commonJsSettings:_*)
  .jvmSettings(commonJvmSettings:_*)

lazy val coreJVM = core.jvm
lazy val coreJS = core.js

/**
 * Laws - cross project that defines macros the laws for all other projects
 */
lazy val laws = crossProject.crossType(CrossType.Pure)
  .dependsOn(macros, core)
  .settings(moduleName := "bricks-laws")
  .settings(bricksSettings:_*)
  .settings(disciplineDependencies:_*)
  .settings(libraryDependencies += "org.scalatest" %%% "scalatest" % scalatestVersion)
  .jsSettings(commonJsSettings:_*)
  .jvmSettings(commonJvmSettings:_*)

lazy val lawsJVM = laws.jvm
lazy val lawsJS = laws.js

/**
 * Tests - cross project that defines test utilities that can be re-used in other libraries, as well as 
 *         all the tests for this build. 
 */
lazy val tests = crossProject.crossType(CrossType.Pure)
  .dependsOn(macros, core, laws)
  .settings(moduleName := "bricks-tests")
  .settings(bricksSettings:_*)
  .settings(disciplineDependencies:_*)
  .settings(noPublishSettings:_*)
  .settings(libraryDependencies += "org.scalatest" %%% "scalatest" % scalatestVersion % "test")
  .jsSettings(commonJsSettings:_*)
  .jvmSettings(commonJvmSettings:_*)

lazy val testsJVM = tests.jvm
lazy val testsJS = tests.js

/**
 * Docs - Generates and publishes the scaladoc API documents and the project web site 
 */
lazy val docs = project
  .settings(moduleName := "bricks-docs")
  .settings(bricksSettings)
  .settings(noPublishSettings)
  .settings(unidocSettings)
  .settings(site.settings)
  .settings(ghpages.settings)
  .settings(docSettings)
  .settings(tutSettings)
  .settings(tutScalacOptions ~= (_.filterNot(Set("-Ywarn-unused-import", "-Ywarn-dead-code"))))
  .settings(commonJvmSettings)
  .dependsOn(coreJVM, lawsJVM, testsJVM)

lazy val docSettings = Seq(
  autoAPIMappings := true,
  unidocProjectFilter in (ScalaUnidoc, unidoc) := inProjects(coreJVM, lawsJVM),
  site.addMappingsToSiteDir(mappings in (ScalaUnidoc, packageDoc), "api"),
  site.addMappingsToSiteDir(tut, "_tut"),
  ghpagesNoJekyll := false,
  siteMappings += file("CONTRIBUTING.md") -> "contributing.md",
  scalacOptions in (ScalaUnidoc, unidoc) ++= Seq(
    "-doc-source-url", scmInfo.value.get.browseUrl + "/tree/master€{FILE_PATH}.scala",
    "-sourcepath", baseDirectory.in(LocalRootProject).value.getAbsolutePath
  ),
  git.remoteRepo := repo,
  includeFilter in makeSite := "*.html" | "*.css" | "*.png" | "*.jpg" | "*.gif" | "*.js" | "*.swf" | "*.yml" | "*.md"
)

/**
 * Plugin and other settings
 */
lazy val disciplineDependencies = Seq(
  libraryDependencies += "org.scalacheck" %%% "scalacheck" % scalacheckVersion,
  libraryDependencies += "org.typelevel" %%% "discipline" % disciplineVersion
)

lazy val publishSettings = Seq(
  homepage := Some(url(home)),
  licenses += license,
  scmInfo :=  Some(ScmInfo(url(home), "scm:git:" + repo)),
  autoAPIMappings := true,
  apiURL := Some(url(api)),
  pomExtra := (
    <developers>
      <developer>
        <id>inthenow</id>
        <name>Alistair Johnson</name>
        <url>http://github.com/InTheNow/</url>
      </developer>
    </developers>
  )
) ++ credentialSettings ++ sharedPublishSettings ++ sharedReleaseProcess 

lazy val scoverageSettings = Seq(
  ScoverageKeys.coverageMinimum := 60,
  ScoverageKeys.coverageFailOnMinimum := false,
  ScoverageKeys.coverageHighlighting := scalaBinaryVersion.value != "2.10"
 // ScoverageKeys.coverageExcludedPackages := "bricks\\.bench\\..*"
)
