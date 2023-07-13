import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "net.mvndicraft.townywaypoints"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
  maven("https://repo.papermc.io/repository/maven-public/")
  maven("https://repo.glaremasters.me/repository/towny/")
  maven("https://jitpack.io")
}

dependencies {
  compileOnly("io.papermc.paper:paper-api:1.20-R0.1-SNAPSHOT")
  compileOnly("com.palmergames.bukkit.towny:towny:0.99.5.0")
  compileOnly("io.github.townyadvanced.commentedconfiguration:CommentedConfiguration:1.0.0")
  implementation("com.github.Anon8281:UniversalScheduler:0.1.5")
}

tasks.named<ShadowJar>("shadowJar") {
  relocate("com.github.Anon8281.universalScheduler","net.mvndicraft.townywaypoints.universalScheduler")
}

java {
  toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}