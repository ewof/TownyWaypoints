import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "net.mvndicraft.townywaypoints"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
  maven("https://jitpack.io")
  maven("https://repo.papermc.io/repository/maven-public/")
  maven("https://repo.glaremasters.me/repository/towny/")
  maven("https://repo.aikar.co/content/groups/aikar/")
}

dependencies {
  compileOnly("io.papermc.paper:paper-api:1.20-R0.1-SNAPSHOT")
  compileOnly("com.palmergames.bukkit.towny:towny:0.99.5.0")
  compileOnly("io.github.townyadvanced.commentedconfiguration:CommentedConfiguration:1.0.0")
  compileOnly("com.github.MilkBowl:VaultAPI:1.7")
  implementation("co.aikar:acf-paper:0.5.1-SNAPSHOT")
}

tasks.named<ShadowJar>("shadowJar") {
  relocate("co.aikar.commands","net.mvndicraft.townywaypoints.acf")
  relocate("co.aikar.locales","net.mvndicraft.townywaypoints.locales")
}

java {
  toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}