plugins {
    java
    application
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

application {
    mainClass.set("com.granitebayace.site.Main")
}

dependencies {
    implementation("com.github.SpencerNold:KWAF:-SNAPSHOT")

    implementation("org.xerial:sqlite-jdbc:3.50.3.0")
}

tasks.run.configure {
    standardInput = System.`in` // Passes gradle stdin to project stdin
}