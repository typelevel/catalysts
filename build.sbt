import Base._

/**
 * These aliases serialise the build for the benefit of Travis-CI, also useful for pre-PR testing.
 * If new projects are added to the build, these must be updated.
 */
addCommandAlias("buildJVM", ";macrosJVM/compile;platformJVM/compile;scalatestJVM/test;specs2/test;testkitJVM/compile;testsJVM/test")
addCommandAlias("validateJVM", ";scalastyle;buildJVM")
addCommandAlias("validateJS", ";macrosJS/compile;platformJS/compile;scalatestJS/test;testkitJS/compile;testsJS/test")
addCommandAlias("validate", ";validateJS;validateJVM")
addCommandAlias("validateAll", s""";++${vers->"scalac"};+clean;+validate;++${vers->"scalac"};docs/makeSite""") 

/**
 * Build settings
 */
val proj    = "catalysts"
val home    = "https://github.com/InTheNow/catalysts"
val repo    = "git@github.com:InTheNow/catalysts.git"
val api     = "https://InTheNow.github.io/catalysts/api/"
val license = ("Apache License", url("http://www.apache.org/licenses/LICENSE-2.0.txt"))
val devs    = Seq(Dev("Alistair Johnson", "inthenow"))

// Example to add/overide versions: val vers = versions + ("discipline" -> "0.3")
val vers = versions

lazy val buildSettings = Seq(
  organization := "org.typelevel",
  scalaVersion := vers->"scalac",
  crossScalaVersions := Seq("2.10.5", scalaVersion.value)
)

/**
 * Common settings
 */
lazy val commonSettings = sharedCommonSettings ++ Seq(
  scalacOptions ++= commonScalacOptions,
  parallelExecution in Test := false
) ++ warnUnusedImport ++ unidocCommonSettings

lazy val commonJsSettings = Seq(
  scalaJSStage in Global := FastOptStage
)

lazy val commonJvmSettings = Seq(
 // testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-oDF)
)

/**
 * catalysts - This is the root project that aggregates the catalystsJVM and catalystsJS sub projects
 */
lazy val catalystsSettings = buildSettings ++ commonSettings ++ publishSettings ++ scoverageSettings

lazy val catalystsConfig = mkConfig(catalystsSettings, commonJvmSettings, commonJsSettings)

lazy val catalysts = project
  .configure(mkRootConfig(catalystsSettings,catalystsJVM))
  .aggregate(catalystsJVM, catalystsJS)
  .dependsOn(catalystsJVM, catalystsJS, testsJVM % "test-internal -> test")

lazy val catalystsJVM = project
  .configure(mkRootJvmConfig(proj, catalystsSettings, commonJvmSettings))
  .aggregate(macrosJVM, platformJVM, scalatestJVM, specs2, testkitJVM, testsJVM, docs)
  .dependsOn(macrosJVM, platformJVM, scalatestJVM, specs2, testkitJVM, testsJVM % "compile;test-internal -> test")

lazy val catalystsJS = project
  .configure(mkRootJsConfig(proj, catalystsSettings, commonJsSettings))
  .aggregate(macrosJS, platformJS, scalatestJS, testkitJS, testsJS)
  .dependsOn(macrosJS, platformJS, scalatestJS, testsJS % "test-internal -> test")

/**
 * Macros - cross project that defines macros
 */
lazy val macros = crossProject.crossType(CrossType.Pure)
  .settings(moduleName := "catalysts-macros")
  .configure(catalystsConfig)
  .settings(libraryDependencies += "org.typelevel" %%% "macro-compat" % (vers->"macro-compat")  % "compile")
  .settings(scalaMacroDependencies(vers->"paradise"):_*)

lazy val macrosJVM = macros.jvm
lazy val macrosJS = macros.js

/**
 * Platform - cross project that provides cross platform support
 */
lazy val platform = crossProject.crossType(CrossType.Dummy)
  .dependsOn(macros)
  .configure(catalystsConfig)
  .settings(moduleName := "catalysts-platform")

lazy val platformJVM = platform.jvm
lazy val platformJS = platform.js

/**
 * Scalatest - cross project that defines test utilities for scalatest
 */
lazy val scalatest = crossProject.crossType(CrossType.Pure)
  .dependsOn(testkit)
  .configure(catalystsConfig)
  .settings(moduleName := "catalysts-scalatest")
  .settings(disciplineDependencies:_*)
  .settings(libraryDependencies += "org.scalatest" %%% "scalatest" % vers.get("scalatest").get )

lazy val scalatestJVM = scalatest.jvm
lazy val scalatestJS = scalatest.js

/**
 * Specs2 - JVM project that defines test utilities for specs2
 */
lazy val specs2 = project
  .dependsOn(testkitJVM, testsJVM % "test-internal -> compile")
  .settings(moduleName := "catalysts-specs2")
  .settings(catalystsSettings:_*)
  .settings(disciplineDependencies:_*)
  .settings(libraryDependencies += "org.specs2" %% "specs2-core" % (vers->"specs2"))
  .settings(libraryDependencies += "org.specs2" %% "specs2-scalacheck" % (vers->"specs2"))
  .settings(commonJvmSettings:_*)

/**
 * Testkit - cross project that defines test utilities that can be re-used in other libraries, as well as 
 *         all the tests for this build.
 */
lazy val testkit = crossProject.crossType(CrossType.Pure)
  .dependsOn(macros, platform)
  .settings(moduleName := "catalysts-testkit")
  .configure(catalystsConfig)

lazy val testkitJVM = testkit.jvm
lazy val testkitJS = testkit.js

/**
 * Tests - cross project that defines test utilities that can be re-used in other libraries, as well as 
 *         all the tests for this build.
 */
lazy val tests = crossProject.crossType(CrossType.Pure)
  .dependsOn(macros, platform, testkit, scalatest % "test-internal -> test")
  .settings(moduleName := "catalysts-tests")
  .configure(catalystsConfig)
  .settings(disciplineDependencies:_*)
  .settings(noPublishSettings:_*)
  .settings(libraryDependencies += "org.scalatest" %%% "scalatest" % (vers->"scalatest") % "test")

lazy val testsJVM = tests.jvm
lazy val testsJS = tests.js

/**
 * Docs - Generates and publishes the scaladoc API documents and the project web site 
 */
lazy val docs = project
  .configure(mkDocConfig(proj, repo, "com.github.inthenow", catalystsSettings, commonJvmSettings,
    platformJVM, macrosJVM, scalatestJVM, specs2, testkitJVM ))

/**
 * Plugin and other settings
 */
lazy val disciplineDependencies = Seq(
  libraryDependencies += "org.scalacheck" %%% "scalacheck" % (vers->"scalacheck"),
  libraryDependencies += "org.typelevel" %%% "discipline" % (vers->"discipline")
)

lazy val publishSettings = sharedPublishSettings(home, repo, api, license, devs) ++ 
    credentialSettings ++ sharedReleaseProcess

lazy val scoverageSettings = sharedScoverageSettings(60)
