pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google() // Google Maven
        mavenCentral() // Maven Central
        maven { // Only for snapshot artifacts
            name = "ossrh-snapshot"
            url = uri("https://oss.sonatype.org/content/repositories/snapshots")
        }
        mavenLocal()
    }
}

rootProject.name = "9P"
include(":app")
 