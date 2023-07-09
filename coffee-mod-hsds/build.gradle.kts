import org.gradle.internal.classpath.Instrumented.systemProperty

// TODO: put the versions into the properties

plugins {
    id("fabric-loom")
    kotlin("jvm").version(System.getProperty("kotlin_version"))
    id("com.google.devtools.ksp").version("${System.getProperty("kotlin_version")}-1.0.11")
}
base { archivesName.set(project.extra["archives_base_name"] as String) }
version = project.extra["mod_version"] as String
group = project.extra["maven_group"] as String


val generatedPath = "src/main/generated/datagen"

loom {
    accessWidenerPath.set(file("src/main/resources/coffee_mod.accesswidener"))

    runs {
        create("datagen") {
            client()
            name("Data Generation")
            vmArgs(
                "-Dfabric-api.datagen",
                "-Dfabric-api.datagen.output-dir=${file(generatedPath)}",
                "-Dfabric-api.datagen.modid=${project.extra["mod_id"] as String}"
            )
            runDir("build/datagen")
        }
    }
}

sourceSets {
    main {
        resources {
            srcDir(generatedPath)
        }
    }
}

repositories {
    maven("https://maven.wispforest.io")
    maven("https://maven.terraformersmc.com/")
    maven("https://maven.kosmx.dev/")
    // hope that there won't be a dependency injection attack someday
}
dependencies {
    minecraft("com.mojang", "minecraft", project.extra["minecraft_version"] as String)
    mappings("net.fabricmc", "yarn", project.extra["yarn_mappings"] as String, null, "v2")
    modImplementation("net.fabricmc", "fabric-loader", project.extra["loader_version"] as String)
    modImplementation("net.fabricmc.fabric-api", "fabric-api", project.extra["fabric_version"] as String)
    modImplementation(
        "net.fabricmc",
        "fabric-language-kotlin",
        project.extra["fabric_language_kotlin_version"] as String
    )

    annotationProcessor("io.wispforest", "owo-lib", project.extra["owo_version"] as String)
    ksp("dev.kosmx.kowoconfig:ksp-owo-config:${project.extra["ksp-owo-config_version"]}")
    modImplementation("io.wispforest", "owo-lib", project.extra["owo_version"] as String)
    include("io.wispforest", "owo-sentinel", project.extra["owo_version"] as String)

    // development
    modLocalRuntime("com.terraformersmc", "modmenu", project.extra["mod_menu_version"] as String)
}
tasks {
    val javaVersion = JavaVersion.toVersion((project.extra["java_version"] as String).toInt())
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        sourceCompatibility = javaVersion.toString()
        targetCompatibility = javaVersion.toString()
        options.release.set(javaVersion.toString().toInt())
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> { kotlinOptions { jvmTarget = javaVersion.toString() } }
    jar {
        from("LICENSE") {
            rename {
                "${it}_${base.archivesName.get()}"
            }
        }
    }

    withType<Jar> {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
    // no idea what I am doing here

    processResources {
        filesMatching("fabric.mod.json") {
            expand(
                mutableMapOf(
                    "version" to project.extra["mod_version"] as String,
                    "fabricloader" to project.extra["loader_version"] as String,
                    "fabric_api" to project.extra["fabric_version"] as String,
                    "fabric_language_kotlin" to project.extra["fabric_language_kotlin_version"] as String,
                    "minecraft" to project.extra["minecraft_version"] as String,
                    "java" to project.extra["java_version"] as String,
                    "mod_id" to project.extra["mod_id"] as String
                )
            )
        }
        filesMatching("assets/${project.extra["mod_id"]}/lang/*.json") {
            expand(
                mutableMapOf(
                    "mod_id" to project.extra["mod_id"] as String
                )
            )
        }
        filesMatching("assets/${project.extra["mod_id"]}/models/*.json") {
            expand(
                mutableMapOf(
                    "mod_id" to project.extra["mod_id"] as String
                )
            )
        }
        filesMatching("*.mixins.json") { expand(mutableMapOf("java" to project.extra["java_version"] as String)) }

        val inputDirectory = project.file("src/main/resources/assets/${project.extra["mod_id"]}/lang/")
        val outputFile = project.file("src/main/resources/assets/${project.extra["mod_id"]}/lang/langs.txt")

        doLast {
            // Get all files under the input directory recursively
            val files = inputDirectory.walkTopDown().filter { it.isFile }.toList()

            outputFile.createNewFile()
            // Write file names to the output file
            outputFile.bufferedWriter().use { writer ->
                files.forEach { file ->
                    writer.write(file.name)
                    writer.newLine()
                }
            }
        }
    }
    java {
        toolchain { languageVersion.set(JavaLanguageVersion.of(javaVersion.toString())) }
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
        withSourcesJar()
    }
}

