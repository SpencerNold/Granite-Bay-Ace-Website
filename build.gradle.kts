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
}