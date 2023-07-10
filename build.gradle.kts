plugins {
    id("java")
}

group = "net.mvndicraft.townywaypoints"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
  maven("https://repo.papermc.io/repository/maven-public/")
  maven("https://repo.glaremasters.me/repository/towny/")
}

dependencies {
  compileOnly("io.papermc.paper:paper-api:1.20-R0.1-SNAPSHOT")
  compileOnly("com.palmergames.bukkit.towny:towny:0.99.5.0")
}

java {
  toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}
