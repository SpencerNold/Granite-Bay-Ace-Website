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

    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
}

tasks.run.configure {
    standardInput = System.`in` // Passes gradle stdin to project stdin
}

tasks.test {
    useJUnitPlatform()

    testLogging {
        // Shows ALL tests when affected files are run
        events("passed", "skipped", "failed")
        showStandardStreams = true
    }
}