import org.jetbrains.changelog.closure
import org.jetbrains.changelog.markdownToHTML
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("org.jetbrains.changelog") version "0.4.0"
  id("org.jetbrains.intellij") version "0.4.22"
  id("org.jetbrains.kotlin.jvm") version "1.4.0"
}

// variables from gradle.properties file
val pluginVersion: String by project
val pluginSinceBuild: String by project
val pluginUntilBuild: String by project
val platformVersion: String by project

group = "nerro.codeowners"
description = "IntelliJ CODEOWNERS"
version = pluginVersion

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-stdlib")
}

intellij {
  version = platformVersion
}

repositories {
  jcenter()
}

tasks {
  withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
    kotlinOptions.languageVersion = "1.4"
  }

  buildSearchableOptions {
    enabled = false
  }

  patchPluginXml {
    version(pluginVersion)
    sinceBuild(pluginSinceBuild)
    untilBuild(pluginUntilBuild)

    pluginDescription(closure {
      File("./README.md").readText().lines().run {
        val start = "<!-- Plugin description start -->"
        val end = "<!-- Plugin description end -->"

        if (!containsAll(listOf(start, end))) {
          throw GradleException("Plugin description section not found in README.md file:\n$start ... $end")
        }
        subList(indexOf(start) + 1, indexOf(end))
      }.joinToString("\n").run { markdownToHTML(this) }
    })
  }
}
