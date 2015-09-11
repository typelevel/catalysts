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
addCommandAlias("buildJVM", ";macrosJVM/compile;platformJVM/compile;lawsJVM/compile;scalatestJVM/compile;testsJVM/test")
addCommandAlias("validateJVM", ";scalastyle;buildJVM")
addCommandAlias("validateJS", ";macrosJS/compile;platformJS/compile;lawsJS/compile;scalatestJS/compile;testsJS/test")
addCommandAlias("validate", ";validateJS;validateJVM")
addCommandAlias("validateAll", s";++$scalacVersion;+clean;+validate;++$scalacVersion;docs/makeSite") 

/**
 * Build settings
 */
val home = "https://github.com/InTheNow/scala-bricks"
val repo = "git@github.com:InTheNow/scala-bricks.git"
val api = "https://InTheNow.github.io/scala-bricks/api/"
val license = ("Apache License", url("http://www.apache.org/licenses/LICENSE-2.0.txt"))

val disciplineVersion = "0.4"
val macroCompatVersion = "1.0.0"
val paradiseVersion = "2.1.0-M5"
val scalacheckVersion = "1.12.4"
val scalatestVersion = "3.0.0-M7"
val scalacVersion = "2.11.7"

lazy val buildSettings = Seq(
  organization := "InTheNow.github",
  scalaVersion := scalacVersion,
  crossScalaVersions := Seq("2.10.5", scalacVersion)
)

/**
 * Common settings
 */
lazy val commonSettings = sharedCommonSettings ++ Seq(
  scalacOptions ++= commonScalacOptions,
  parallelExecution in Test := false
  // resolvers += Resolver.sonatypeRepo("snapshots")
) ++ warnUnusedImport ++ unidocCommonSettings

lazy val commonJsSettings = Seq(
  scalaJSStage in Global := FastOptStage
)

lazy val commonJvmSettings = Seq(
 // testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-oDF)
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
  .aggregate(macrosJVM, platformJVM, lawsJVM, scalatestJVM, testsJVM, docs)
  .dependsOn(macrosJVM, platformJVM, lawsJVM, testsJVM % "test-internal -> test")

lazy val bricksJS = project.in(file(".bricksJS"))
  .settings(moduleName := "bricks")
  .settings(bricksSettings)
  .settings(commonJsSettings)
  .aggregate(macrosJS, platformJS, lawsJS, testsJS)
  .dependsOn(macrosJS, platformJS, lawsJS, scalatestJS, testsJS % "test-internal -> test")
  .enablePlugins(ScalaJSPlugin)

/**
 * Macros - cross project that defines macros
 */
lazy val macros = crossProject.crossType(CrossType.Pure)
  .settings(moduleName := "bricks-macros")
  .settings(bricksSettings:_*)
  .settings(libraryDependencies += "org.typelevel" %%% "macro-compat" % macroCompatVersion % "compile")
  .settings(scalaMacroDependencies(paradiseVersion):_*)
  .jsSettings(commonJsSettings:_*)
  .jvmSettings(commonJvmSettings:_*)

lazy val macrosJVM = macros.jvm
lazy val macrosJS = macros.js

/**
 * Platform - cross project that provides cross platform support
 */
lazy val platform = crossProject.crossType(CrossType.Dummy)
  .dependsOn(macros)
  .settings(moduleName := "bricks-platform")
  .settings(bricksSettings:_*)
  .settings(scalaMacroDependencies(paradiseVersion):_*)
  .jsSettings(commonJsSettings:_*)
  .jvmSettings(commonJvmSettings:_*)

lazy val platformJVM = platform.jvm
lazy val platformJS = platform.js

/**
 * Laws - cross project that defines macros the laws for all other projects
 */
lazy val laws = crossProject.crossType(CrossType.Pure)
  .dependsOn(macros, platform)
  .settings(moduleName := "bricks-laws")
  .settings(bricksSettings:_*)
  .settings(disciplineDependencies:_*)
  .settings(libraryDependencies += "org.scalatest" %%% "scalatest" % scalatestVersion)
  .jsSettings(commonJsSettings:_*)
  .jvmSettings(commonJvmSettings:_*)

lazy val lawsJVM = laws.jvm
lazy val lawsJS = laws.js

/**
 * Scalatest - cross project that defines test utilities for scalatest
 */
lazy val scalatest = crossProject.crossType(CrossType.Pure)
  //.dependsOn()
  .settings(moduleName := "bricks-scalatest")
  .settings(bricksSettings:_*)
  .settings(libraryDependencies += "org.scalatest" %%% "scalatest" % scalatestVersion)
  .jsSettings(commonJsSettings:_*)
  .jvmSettings(commonJvmSettings:_*)

lazy val scalatestJVM = scalatest.jvm
lazy val scalatestJS = scalatest.js

/**
 * Tests - cross project that defines test utilities that can be re-used in other libraries, as well as 
 *         all the tests for this build.
 */
lazy val tests = crossProject.crossType(CrossType.Pure)
  .dependsOn(macros, platform, laws, scalatest % "test-internal -> test")
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
  .settings(doctestSettings)
  .settings(doctestTestFramework := DoctestTestFramework.ScalaTest)
  .settings(doctestWithDependencies := false)
  .settings(commonJvmSettings)
  .dependsOn(platformJVM, lawsJVM, testsJVM)

lazy val docSettings = Seq(
  autoAPIMappings := true,
  unidocProjectFilter in (ScalaUnidoc, unidoc) := inProjects(platformJVM, lawsJVM, macrosJVM, scalatestJVM, testsJVM),
  site.addMappingsToSiteDir(mappings in (ScalaUnidoc, packageDoc), "api"),
  site.addMappingsToSiteDir(tut, "_tut"),
  ghpagesNoJekyll := false,
  siteMappings += file("CONTRIBUTING.md") -> "contributing.md",
  scalacOptions in (ScalaUnidoc, unidoc) ++= Seq(
    "-doc-source-url", scmInfo.value.get.browseUrl + "/tree/master€{FILE_PATH}.scala",
    "-sourcepath", baseDirectory.in(LocalRootProject).value.getAbsolutePath,
    "-diagrams"
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

lazy val publishSettings = sharedPublishSettings(home, repo, api, license) ++ Seq(
  autoAPIMappings := true,
  pomExtra := (
    <developers>
      <developer>
        <id>inthenow</id>
        <name>Alistair Johnson</name>
        <url>http://github.com/InTheNow/</url>
      </developer>
    </developers>
  )
) ++ credentialSettings ++ sharedReleaseProcess

lazy val scoverageSettings = Seq(
  ScoverageKeys.coverageMinimum := 60,
  ScoverageKeys.coverageFailOnMinimum := false,
  ScoverageKeys.coverageHighlighting := scalaBinaryVersion.value != "2.10"
 // ScoverageKeys.coverageExcludedPackages := "bricks\\.bench\\..*"
)
