plugins {
    id("java-library")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.1")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-csv:2.18.1")

    testImplementation(libs.junit.jupiter)
    testImplementation(libs.assertj)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks {
    assemble {
        dependsOn("uberJar")
    }

    register<Jar>("uberJar") {
        archiveClassifier = "uber"

        from(sourceSets.main.get().output)

        dependsOn(configurations.runtimeClasspath)
        from(configurations.runtimeClasspath.get()
            .filter { it.name.endsWith("jar") }
            .map { zipTree(it) }
        )

        manifest {
            attributes("Main-Class" to "com.github.lipinskipawel.Application")
        }

        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        exclude("META-INF/INDEX.LIST", "META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA")
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
