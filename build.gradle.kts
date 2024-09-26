import net.minecrell.pluginyml.bukkit.BukkitPluginDescription.Permission

plugins {
    id("java")
    id("maven-publish")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.freefair.lombok") version "8.6"
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
}

group = "ac.grim.grimac"
version = "2.3.68"
description = "Libre simulation anticheat designed for 1.21.1 with 1.20.4-1.21.1 support, powered by PacketEvents 2.0."
java.sourceCompatibility = JavaVersion.VERSION_17
java.targetCompatibility = JavaVersion.VERSION_17

// Set to false for debug builds
// You cannot live reload classes if the jar relocates dependencies
var relocate = true;

repositories {
    mavenLocal()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") // Spigot
    maven("https://jitpack.io/") { // Grim API
        content {
            includeGroup("com.github.rinoanticheat")
        }
    }
    maven("https://repo.viaversion.com") // ViaVersion
    maven("https://repo.aikar.co/content/groups/aikar/") // ACF
    maven("https://nexus.scarsz.me/content/repositories/releases") // Configuralize
    maven("https://repo.opencollab.dev/maven-snapshots/") // Floodgate
    maven("https://repo.opencollab.dev/maven-releases/") // Cumulus (for Floodgate)
    maven("https://repo.codemc.io/repository/maven-releases/") // PacketEvents
    maven("https://repo.codemc.io/repository/maven-snapshots/")
    mavenCentral()
    // FastUtil, Discord-Webhooks
}

dependencies {
    implementation("com.github.retrooper:packetevents-spigot:2.5.1-SNAPSHOT")
    implementation("co.aikar:acf-paper:0.5.1-SNAPSHOT")
    implementation("club.minnced:discord-webhooks:0.8.0") // Newer versions include kotlin-stdlib, which leads to incompatibility with plugins that use Kotlin
    implementation("it.unimi.dsi:fastutil:8.5.13")
    implementation("github.scarsz:configuralize:1.4.0")

    implementation("com.github.grimanticheat:grimapi:1193c4fa41")
    // Used for local testing: implementation("ac.grim.grimac:grimapi:1.0")

    implementation("org.jetbrains:annotations:24.1.0")
    compileOnly("org.geysermc.floodgate:api:2.0-SNAPSHOT")
    compileOnly("org.spigotmc:spigot-api:1.18.2-R0.1-SNAPSHOT")
    compileOnly("com.viaversion:viaversion-api:4.9.4-SNAPSHOT")
    //
    compileOnly("io.netty:netty-all:4.1.85.Final")
}

bukkit {
    name = "RinoAC"
    author = "Chest Solutions"
    main = "ac.rino.rino.RinoAC"
    apiVersion = "1.20.4"
    foliaSupported = true

    softDepend = listOf(
        "ProtocolLib",
        "ProtocolSupport",
        "Essentials",
        "ViaVersion",
        "ViaBackwards",
        "ViaRewind",
        "Geyser-Spigot",
        "floodgate",
        "FastLogin"
    )

    permissions {
        register("rino.alerts") {
            description = "Receive alerts for violations"
            default = Permission.Default.OP
        }

        register("rino.alerts.enable-on-join") {
            description = "Enable alerts on join"
            default = Permission.Default.OP
        }

        register("rino.performance") {
            description = "Check performance metrics"
            default = Permission.Default.OP
        }

        register("rino.profile") {
            description = "Check user profile"
            default = Permission.Default.OP
        }

        register("rino.brand") {
            description = "Show client brands on join"
            default = Permission.Default.OP
        }

        register("rino.sendalert") {
            description = "Send cheater alert"
            default = Permission.Default.OP
        }

        register("rino.nosetback") {
            description = "Disable setback"
            default = Permission.Default.FALSE
        }

        register("rino.nomodifypacket") {
            description = "Disable modifying packets"
            default = Permission.Default.FALSE
        }

        register("rino.exempt") {
            description = "Exempt from all checks"
            default = Permission.Default.FALSE
        }
    }
}

tasks.build {
    dependsOn(tasks.shadowJar)
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

publishing.publications.create<MavenPublication>("maven") {
    artifact(tasks["shadowJar"])
}

tasks.shadowJar {
    minimize()
    archiveFileName.set("${project.name}-${project.version}.jar")
    if (relocate) {
        relocate("io.github.retrooper.packetevents", "ac.rino.rinoac.shaded.io.github.retrooper.packetevents")
        relocate("com.github.retrooper.packetevents", "ac.rino.rinoac.shaded.com.github.retrooper.packetevents")
        relocate("co.aikar.commands", "ac.rino.rinoac.shaded.acf")
        relocate("co.aikar.locale", "ac.rino.rinoac.shaded.locale")
        relocate("club.minnced", "ac.rino.rinoac.shaded.discord-webhooks")
        relocate("github.scarsz.configuralize", "ac.rino.rinoac.shaded.configuralize")
        relocate("com.github.puregero", "ac.rino.rinoac.shaded.com.github.puregero")
        relocate("com.google.code.gson", "ac.rino.rinoac.shaded.gson")
        relocate("alexh", "ac.rino.rinoac.shaded.maps")
        relocate("it.unimi.dsi.fastutil", "ac.rino.rinoac.shaded.fastutil")
        relocate("net.kyori", "ac.rino.rinoac.shaded.kyori")
        relocate("okhttp3", "ac.rino.rinoac.shaded.okhttp3")
        relocate("okio", "ac.rino.rinoac.shaded.okio")
        relocate("org.yaml.snakeyaml", "ac.rino.rinoac.shaded.snakeyaml")
        relocate("org.json", "ac.rino.rinoac.shaded.json")
        relocate("org.intellij", "ac.rino.rinoac.shaded.intellij")
        relocate("org.jetbrains", "ac.rino.rinoac.shaded.jetbrains")
    }
}